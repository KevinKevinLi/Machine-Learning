package Network;
import Activation.*;
import java.util.*;
import Listeners.*;
import LossFunction.LossFunction;
import Network.Conponent.LayerConf;
import Network.Conponent.LayerKernel;
import Network.Conponent.Weightinit;

public class FeedForwardKernel {
//    private static ArrayList<Neuron> NeuronList = new ArrayList<Neuron>();
//    private static ArrayList<Double> WeightList = new ArrayList<Double>();
//    private static ArrayList<Double> Bias = new ArrayList<Double>();
//    private Neuron NeuronMap[][];
//    private double WeightMap[][];
//    private double MomentumMap[][];
//    private double WeightMap_backup[][];

    private static int input_num;
    private ArrayList<Double> inputlist = new ArrayList<Double>();
    private static int output_num;
//    private static int hiddenneurons_num[];
//    private int neuron_num;
//    private int weight_num;
    private static int layers_num;
//    private static int hiddenlayers_num;

    private static double learningrate;
    private static double momentum=0.0;
    private static LossFunction lossfunction;
    private static double bias=0.0;

//    private static double error=0;
    private Listener Statistic;

    private LayerKernel NetworkLayer;

    public FeedForwardKernel(){
    }

    public void setbase(int layer_num,int input_num,int output_num,double learningrate){
        this.layers_num=layer_num;
        this.input_num=input_num;
        this.output_num=output_num;
        this.learningrate=learningrate;
    }

    //overload
    public void setbase(int layer_num,int input_num,int output_num,double learningrate,double momentum){
        this.layers_num=layer_num;
        this.input_num=input_num;
        this.output_num=output_num;
        this.learningrate=learningrate;
        this.momentum=momentum;
    }

    public void setlayers(LayerConf inputlayer, ArrayList<LayerConf> hiddenlayers, LayerConf outputlayer){
        if(momentum!=0.0){
            NetworkLayer.useMomentumAlgorithm(momentum);
        }
        this.lossfunction=outputlayer.getLossfunction();
        NetworkLayer=new LayerKernel(inputlayer,hiddenlayers,outputlayer);
    }

    public void setListener(Listener L){
        Statistic=L;
    }

    public double feedforward(double [] inputset){
        double totalerror=0;
        for(int i=0;i<input_num;i++){
            inputlist.add(inputset[i]);
        }
        int layer_number=0;
        //init input layers
        for(int i=0;i<input_num;i++)
        {
            NetworkLayer.updateNeuronOutput(0,i,inputset[i]);
        }
        layer_number++;
        //cal input->hidden->output layer
        while(layer_number<layers_num) {
            //cal hidden->output layer
            for (int i = 0; i < NetworkLayer.getNeuronLineLength(layer_number); i++) {
                //record weights number
                int num = i;
                double neuron_input = 0;
                for (int j = 0; j < NetworkLayer.getNeuronLineLength(layer_number - 1); j++) {
                    //calculate weight*input
                    neuron_input += NetworkLayer.getWeight(layer_number-1, num) * NetworkLayer.getNeuronOutput(layer_number - 1, j);
                    num += NetworkLayer.getNeuronLineLength(layer_number);
                }
                //System.out.println(neuron_input+NetworkLayer.getBias(1,i));
                NetworkLayer.updateNeuronOutput(layer_number, i, neuron_input + NetworkLayer.getBias(layer_number-1, i));
            }
            NetworkLayer.CheckUpdate(layer_number);
            layer_number++;
        }
        //cal error
        for(int i=0;i<output_num;i++) {
            //cal error
            NetworkLayer.setNeuronTarget(layers_num-1, i, inputset[input_num + i]);
            //System.out.println(i+" "+NetworkLayer.getNeuronOutput(1,i));
            NetworkLayer.setNeuronError(layers_num-1, i, lossfunction.exec(inputset[input_num + i], NetworkLayer.getNeuronOutput(layers_num-1, i), output_num));
            totalerror += NetworkLayer.getNeuronError(layers_num-1, i);
        }
        //System.out.println(totalerror);
        Statistic.addError(totalerror);
        Statistic.addSampleNum();
        return totalerror;
    }

    public void backpropagation(){
        NetworkLayer.cloneWeightMap();
        //update risiduals first
        //residual in output layer only need to be setted once because it is only related to output and target_output
        double residual = 0;
        for (int j = 0; j < NetworkLayer.getNeuronLineLength(layers_num-1); j++) {
            //residual only need to be setted once because it is only related to output and target_output
            residual = NetworkLayer.getNeuronDerivative(layers_num-1,j) * lossfunction.execDrivative(NetworkLayer.getNeuronTarget(layers_num-1,j), NetworkLayer.getNeuronOutput(layers_num-1,j), output_num);
            //NeuronMap[1][j].setResidual(residual);
            NetworkLayer.setNeuronResidual(layers_num-1,j,residual);
        }

        int weight_num = 0;
        //cal out->hidden layer
        //Using Partial derivative and chain derivative rule
        //w+=w-learningrate*residual*output_hiddenlayer_neuron
        //residual=activation_derivative*loss_derivative
        weight_num = 0;
        for (int i = 0; i < NetworkLayer.getNeuronLineLength(layers_num-2); i++) {
            for (int j = 0; j < NetworkLayer.getNeuronLineLength(layers_num-1); j++) {
                residual = NetworkLayer.getNeuronResidual(layers_num-1,j);
                NetworkLayer.updateWeight(layers_num-2,weight_num,-learningrate * residual * NetworkLayer.getNeuronOutput(layers_num-2,i));
                //update bias
                if(i==0){
                    NetworkLayer.updateBias(layers_num-2,weight_num,-learningrate * residual);
                }
                weight_num++;
            }
        }

        //cal hiddens->input layer
        //loss dirivative is more complex than before because hidden neurons are affected by multi output neurons
        //loss dirivative=loss_dirivative_outputs_sum
        //loss_dirivative_output_sum=sum(output_residual*w)
        //w+=w-learningrate*residual*input
        int outputweight_num = 0;
        for(int m=layers_num-1-1;m>0;m--) {
            weight_num = 0;
            for (int i = 0; i < NetworkLayer.getNeuronLineLength(m-1); i++) {
                for (int j = 0; j < NetworkLayer.getNeuronLineLength(m); j++) {
                    if (i == 0) {
                        double sum_loss = 0;
                        for (int k = 0; k < NetworkLayer.getNeuronLineLength(m+1); k++) {
                            sum_loss += NetworkLayer.getNeuronResidual(m+1, k) * NetworkLayer.getBackupWeight(m, outputweight_num);
                            outputweight_num++;
                        }
                        residual = NetworkLayer.getNeuronDerivative(m, j) * sum_loss;
                        NetworkLayer.setNeuronResidual(m, j, residual);
                        NetworkLayer.updateWeight(m-1, weight_num, -learningrate * residual * NetworkLayer.getNeuronOutput(m-1,i));
                        //update bias
                        NetworkLayer.updateBias(m-1, weight_num, -learningrate * residual);
                    } else {
                        residual = NetworkLayer.getNeuronResidual(m, j);
                        NetworkLayer.updateWeight(m-1, weight_num, -learningrate * residual * NetworkLayer.getNeuronOutput(m-1,i));
                    }
                    weight_num++;
                }
                outputweight_num = 0;
            }
        }
        //init
        inputlist.clear();
    }

    public void printNetwork(){
        for(int i=0;i<NetworkLayer.getWeightRowLength();i++){
            for(int j=0;j<NetworkLayer.getWeightLineLength(i);j++) {
                System.out.println("("+i+","+j+") W:"+NetworkLayer.getWeight(i,j)+" M:"+NetworkLayer.getMomentum(i,j));
            }
        }
        for(int i=0;i<NetworkLayer.getWeightRowLength();i++){
            for(int j=0;j<NetworkLayer.getNeuronLineLength(i+1);j++){
                System.out.println("("+i+","+j+") B:"+NetworkLayer.getBias(i,j));
            }
        }
        for(int i=0;i<NetworkLayer.getNeuronRowLength();i++){
            for(int j=0;j<NetworkLayer.getNeuronLineLength(i);j++){
                System.out.println("Neuron("+i+","+j+"): "+NetworkLayer.getNeuronActivation(i,j));
            }
        }
    }

    public void setLossfunction(LossFunction los){
        this.lossfunction=los;
    }
}

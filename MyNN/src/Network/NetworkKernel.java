package Network;
import Activation.*;
import java.util.*;
import Listeners.*;
import LossFunction.LossFunction;
import Network.Conponent.LayerConf;
import Network.Conponent.LayerKernel;
import Network.Conponent.Weightinit;

public class NetworkKernel {
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

    public NetworkKernel(){
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
        //assume we only have 3 layers input,hidden,output
        if(layers_num==3){
            //cal input->hidden layer
            for(int i=0;i<NetworkLayer.getHiddenNeuronsNum(0);i++) {
                //record weights number
                int num=i;
                double neuron_input=0;
                for(int j=0;j<input_num;j++){
                    //calculate weight*input
                    neuron_input+=NetworkLayer.getWeight(0,num)*inputset[j];
                    //System.out.println(neuron_input+" "+NetworkLayer.getWeight(0,num));
                    num+=NetworkLayer.getHiddenNeuronsNum(0);
                    //inputlist.add(inputset[j]);
                }
                NetworkLayer.updateNeuronOutput(0,i,neuron_input+NetworkLayer.getBias(0,i));
            }
            //cal hidden->output layer
            for(int i=0;i<output_num;i++) {
                //record weights number
                int num=i;
                double neuron_input=0;
                for(int j=0;j<NetworkLayer.getHiddenNeuronsNum(0);j++){
                    //calculate weight*input
                    neuron_input+=NetworkLayer.getWeight(1,num)*NetworkLayer.getNeuronOutput(0,j);
                    num+=output_num;
                }
                NetworkLayer.updateNeuronOutput(1,i,neuron_input+NetworkLayer.getBias(1,i));
                //System.out.println(NetworkLayer.getNeuronOutput(1,i));
                //cal error
                NetworkLayer.setNeuronTarget(1,i,inputset[input_num+i]);
                NetworkLayer.setNeuronError(1,i,lossfunction.exec(inputset[input_num+i],NetworkLayer.getNeuronOutput(1,i),output_num));
                totalerror+=NetworkLayer.getNeuronError(1,i);
            }
            //System.out.println(totalerror);
            Statistic.addError(totalerror);
            Statistic.addSampleNum();
        }
        return totalerror;
    }

    public void backpropagation(){
        if(layers_num==3) {
            //update risiduals first
            //residual in output layer only need to be setted once because it is only related to output and target_output
            double residual = 0;
            for (int j = 0; j < NetworkLayer.getNeuronLineLength(1); j++) {
                //residual only need to be setted once because it is only related to output and target_output
                residual = NetworkLayer.getNeuronDerivative(1,j) * lossfunction.execDrivative(NetworkLayer.getNeuronTarget(1,j), NetworkLayer.getNeuronOutput(1,j), output_num);
                //NeuronMap[1][j].setResidual(residual);
                NetworkLayer.setNeuronResidual(1,j,residual);
            }

            //cal hidden->input layer
            //loss dirivative is more complex than before because hidden neurons are affected by multi output neurons
            //loss dirivative=loss_dirivative_outputs_sum
            //loss_dirivative_output_sum=sum(output_residual*w)
            //w+=w-learningrate*residual*input
            int weight_num = 0;
            int outputweight_num = 0;
            for (int i = 0; i < input_num; i++) {
                for (int j = 0; j < NetworkLayer.getNeuronLineLength(0); j++) {
                    double sum_loss = 0;
                    for (int k = 0; k < output_num; k++) {
                        sum_loss += NetworkLayer.getNeuronResidual(1,k) * NetworkLayer.getWeight(1,outputweight_num);
                        outputweight_num++;
                    }
                    residual = NetworkLayer.getNeuronDerivative(0,j) * sum_loss;
                    NetworkLayer.updateWeight(0,weight_num,-learningrate * residual * inputlist.get(i));
                    //update bias
                    if(i==0){
                        NetworkLayer.updateBias(0,weight_num,-learningrate * residual);
                    }
                    weight_num++;
                }
                outputweight_num = 0;
            }

            //cal out->hidden layer
            //Using Partial derivative and chain derivative rule
            //w+=w-learningrate*residual*output_hiddenlayer_neuron
            //residual=activation_derivative*loss_derivative
            weight_num = 0;
            for (int i = 0; i < NetworkLayer.getNeuronLineLength(0); i++) {
                for (int j = 0; j < NetworkLayer.getNeuronLineLength(1); j++) {
                    residual = NetworkLayer.getNeuronResidual(1,j);
                    NetworkLayer.updateWeight(1,weight_num,-learningrate * residual * NetworkLayer.getNeuronOutput(0,i));
                    //update bias
                    if(i==0){
                        NetworkLayer.updateBias(1,weight_num,-learningrate * residual);
                    }
                    weight_num++;
                }
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
        for(int i=0;i<1;i++){
            for(int j=0;j<20;j++){
                System.out.println(NetworkLayer.getBias(i,j));
            }
        }
    }
}

package Network;
import Activation.*;
import java.util.*;
import Listeners.*;

public class NetworkKernel {
//    private static ArrayList<Neuron> NeuronList = new ArrayList<Neuron>();
//    private static ArrayList<Double> WeightList = new ArrayList<Double>();
//    private static ArrayList<Double> Bias = new ArrayList<Double>();
    private Neuron NeuronMap[][];
    private double WeightMap[][];
    private double WeightMap_backup[][];

    private static int input_num;
    private ArrayList<Double> inputlist = new ArrayList<Double>();
    private static int output_num;
    private static int hiddenneurons_num;
    private int neuron_num;
    private int weight_num;
    private static int layers_num=3;
    private static int hiddenlayers_num=1;

    private static double learningrate;
    private static Weight weight_function;
    private static LossFunction lossfunction;

    private static double error=0;
    private Listener Statistic;

    public NetworkKernel(int input, int output, int hiddenneurons, double learningrate, Weight weight, ActivationFrame activationFrame, LossFunction lossfunction){
        this.input_num=input;
        this.output_num=output;
        this.hiddenneurons_num=hiddenneurons;
        this.learningrate=learningrate;
        this.weight_function = weight;
        this.lossfunction=lossfunction;

        //init neurons
        neuron_num=input+output+hiddenneurons;
        NeuronMap=new Neuron[layers_num-1][];
        if(layers_num==3) {
            NeuronMap[0]=new Neuron[hiddenneurons];
            NeuronMap[1]=new Neuron[output_num];
            for(int i=0;i<hiddenneurons;i++){
                NeuronMap[0][i]=activationFrame.getNeuron();
            }
            for(int i=0;i<output_num;i++){
                NeuronMap[1][i]=activationFrame.getNeuron();
            }
        }

        //init weight
        weight_num=(input+output)*hiddenneurons;
        WeightMap=new double[layers_num-1][];
        if(layers_num==3) {
            WeightMap[0] = new double [input*hiddenneurons];
            WeightMap[1] = new double [output*hiddenneurons];
            for(int i=0;i<2;i++){
                for(int j=0;j<WeightMap[i].length;j++) {
                    WeightMap[i][j]= weight.init();
                }
            }
        }
    };

    public void setListener(Listener L){
        Statistic=L;
    }

    public void feedforward(double [] inputset){
        //assume we only have 3 layers input,hidden,output
        if(layers_num==3){
            //cal input->hidden layer
            for(int i=0;i<hiddenneurons_num;i++) {
                //record weights number
                int num=i;
                double neuron_input=0;
                for(int j=0;j<input_num;j++){
                    //calculate weight*input
                    neuron_input+=WeightMap[0][num]*inputset[j];
                    num+=hiddenneurons_num;
                    inputlist.add(inputset[j]);
                }
                NeuronMap[0][i].execOutput(neuron_input);
                //System.out.println(NeuronMap[0][i].getoutput());
            }
            //cal hidden->output layer
            double totalerror=0;
            for(int i=0;i<output_num;i++) {
                //record weights number
                int num=i;
                double neuron_input=0;
                for(int j=0;j<hiddenneurons_num;j++){
                    //calculate weight*input
                    neuron_input+=WeightMap[1][num]*NeuronMap[0][j].getoutput();
                    num+=output_num;
                }
                NeuronMap[1][i].execOutput(neuron_input);
                //System.out.println(NeuronMap[1][i].getoutput());
                //cal error
                NeuronMap[1][i].setTarget(inputset[input_num+i]);
                NeuronMap[1][i].setError(lossfunction.exec(inputset[input_num+i],NeuronMap[1][i].getoutput()));
                totalerror+=NeuronMap[1][i].getError();
            }
            //System.out.println(totalerror);
            Statistic.addError(totalerror);
            Statistic.addSampleNum();
        }
    }

    public void backpropagation(){
        //update risiduals first
        //residual in output layer only need to be setted once because it is only related to output and target_output
        double residual=0;
        for(int j=0;j<NeuronMap[1].length;j++){
            //residual only need to be setted once because it is only related to output and target_output
                residual = NeuronMap[1][j].execDerivative() * lossfunction.execDrivative(NeuronMap[1][j].getTarget(), NeuronMap[1][j].getoutput());
                NeuronMap[1][j].setResidual(residual);
        }

        //cal hidden->input layer
        //loss dirivative is more complex than before because hidden neurons are affected by multi output neurons
        //loss dirivative=loss_dirivative_outputs_sum
        //loss_dirivative_output_sum=sum(output_residual*w)
        //w+=w-learningrate*residual*input
        int weight_num=0;
        int outputweight_num=0;
        for(int i=0;i<input_num;i++){
            for(int j=0;j<NeuronMap[0].length;j++){
                double sum_loss=0;
                for(int k=0;k<output_num;k++) {
                    sum_loss += NeuronMap[1][k].getResidual()*WeightMap[1][outputweight_num];
                    outputweight_num++;
                }
                residual=NeuronMap[0][j].execDerivative()*sum_loss;
                WeightMap[0][weight_num]+=-learningrate*residual*inputlist.get(i);
                weight_num++;
            }
            outputweight_num=0;
        }

        //cal out->hidden layer
        //Using Partial derivative and chain derivative rule
        //w+=w-learningrate*residual*output_hiddenlayer_neuron
        //residual=activation_derivative*loss_derivative
        weight_num=0;
        for(int i=0;i<NeuronMap[0].length;i++){
            for(int j=0;j<NeuronMap[1].length;j++){

                residual=NeuronMap[1][j].getResidual();
                WeightMap[1][weight_num]+= -learningrate*residual*NeuronMap[0][i].getoutput();
                weight_num++;
            }
        }

        //init
        inputlist.clear();
    }


    public void printNetwork(){
        for(int i=0;i<WeightMap.length;i++){
            for(int j=0;j<WeightMap[i].length;j++) {
                System.out.println(WeightMap[i][j]);
            }
        }
    }
}

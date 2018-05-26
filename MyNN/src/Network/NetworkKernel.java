package Network;
import Activation.*;
import java.util.*;
import Listeners.*;
import LossFunction.LossFunction;

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
    private static int hiddenneurons_num[];
    private int neuron_num;
    private int weight_num;
    private static int layers_num;
    private static int hiddenlayers_num;

    private static double learningrate;
    private static Weight weight_function;
    private static LossFunction lossfunction;
    private static double bias=1.0;

    private static double error=0;
    private Listener Statistic;

    public NetworkKernel(){
    }

    //old init function only support 3 layers
    public NetworkKernel(int input, int output, int hiddenneurons, double learningrate, Weight weight, ActivationFrame activationFrame, LossFunction lossfunction){
        this.input_num=input;
        this.output_num=output;
        layers_num=3;
        this.hiddenneurons_num=new int [layers_num-1];
        hiddenneurons_num[0]=hiddenneurons;
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

    public void setbase(int layer_num,int input_num,int output_num,double learningrate,Weight weight){
        this.layers_num=layer_num;
        this.input_num=input_num;
        this.output_num=output_num;
        this.learningrate=learningrate;
        this.weight_function=weight;
        this.hiddenlayers_num=layer_num-2;
    }

    public void setlayers(Layer inputlayer,ArrayList<Layer> hiddenlayers,Layer outputlayer){
        this.lossfunction=outputlayer.getLossfunction();
        //init neurons
        NeuronMap=new Neuron[layers_num-1][];
        this.hiddenneurons_num=new int [layers_num-2];
        for(int i=0;i<hiddenlayers_num;i++) {
            hiddenneurons_num[i]=hiddenlayers.get(i).getHidden_num();
            NeuronMap[i]=new Neuron[hiddenneurons_num[i]];
        }
        NeuronMap[layers_num-2]=new Neuron[output_num];
        for(int i=0;i<hiddenlayers_num;i++){
            for(int j=0;j<hiddenneurons_num[i];j++) {
                NeuronMap[i][j] = hiddenlayers.get(i).getActivation().getNeuron();
            }
        }
        for(int i=0;i<output_num;i++){
            NeuronMap[hiddenlayers_num][i]=outputlayer.getActivation().getNeuron();
        }

        //init weight
        WeightMap=new double[layers_num-1][];
        if(hiddenlayers_num==1){
            weight_num+=(input_num+output_num)*hiddenneurons_num[0];
            WeightMap[0] = new double [input_num*hiddenneurons_num[0]];
            WeightMap[1] = new double [output_num*hiddenneurons_num[0]];
        }
        else{
            weight_num+=input_num*hiddenneurons_num[0];
            WeightMap[0] = new double [input_num*hiddenneurons_num[0]];
            for(int i=0;i<hiddenlayers_num;i++){
                if(i+1==hiddenlayers_num){
                    weight_num+=hiddenneurons_num[i]*output_num;
                    WeightMap[i+1] = new double [output_num*hiddenneurons_num[0]];
                }
                else {
                    weight_num += hiddenneurons_num[i] * hiddenneurons_num[i + 1];
                    WeightMap[i+1] = new double [hiddenneurons_num[i] * hiddenneurons_num[i + 1]];
                }
            }
        }

        for(int i=0;i<WeightMap.length;i++){
            for(int j=0;j<WeightMap[i].length;j++) {
                WeightMap[i][j]= weight_function.init();
             //   System.out.println(WeightMap[i][j]);
            }
        }

    }

    public void setListener(Listener L){
        Statistic=L;
    }

    public void feedforward(double [] inputset){
        //assume we only have 3 layers input,hidden,output
        if(layers_num==3){
            //cal input->hidden layer
            for(int i=0;i<hiddenneurons_num[0];i++) {
                //record weights number
                int num=i;
                double neuron_input=0;
                for(int j=0;j<input_num;j++){
                    //calculate weight*input
                    neuron_input+=WeightMap[0][num]*inputset[j];
                    num+=hiddenneurons_num[0];
                    inputlist.add(inputset[j]);
                }
                NeuronMap[0][i].execOutput(neuron_input+bias);
                //System.out.println(NeuronMap[0][i].getoutput());
            }
            //cal hidden->output layer
            double totalerror=0;
            for(int i=0;i<output_num;i++) {
                //record weights number
                int num=i;
                double neuron_input=0;
                for(int j=0;j<hiddenneurons_num[0];j++){
                    //calculate weight*input
                    neuron_input+=WeightMap[1][num]*NeuronMap[0][j].getoutput();
                    num+=output_num;
                }
                NeuronMap[1][i].execOutput(neuron_input+bias);
                //System.out.println(NeuronMap[1][i].getoutput());
                //cal error
                NeuronMap[1][i].setTarget(inputset[input_num+i]);
                NeuronMap[1][i].setError(lossfunction.exec(inputset[input_num+i],NeuronMap[1][i].getoutput(),output_num));
                totalerror+=NeuronMap[1][i].getError();
            }
            //System.out.println(totalerror);
            Statistic.addError(totalerror);
            Statistic.addSampleNum();
        }
    }

    public void backpropagation(){
        if(layers_num==3) {
            //update risiduals first
            //residual in output layer only need to be setted once because it is only related to output and target_output
            double residual = 0;
            for (int j = 0; j < NeuronMap[1].length; j++) {
                //residual only need to be setted once because it is only related to output and target_output
                residual = NeuronMap[1][j].execDerivative() * lossfunction.execDrivative(NeuronMap[1][j].getTarget(), NeuronMap[1][j].getoutput(), output_num);
                NeuronMap[1][j].setResidual(residual);
            }

            //cal hidden->input layer
            //loss dirivative is more complex than before because hidden neurons are affected by multi output neurons
            //loss dirivative=loss_dirivative_outputs_sum
            //loss_dirivative_output_sum=sum(output_residual*w)
            //w+=w-learningrate*residual*input
            int weight_num = 0;
            int outputweight_num = 0;
            for (int i = 0; i < input_num; i++) {
                for (int j = 0; j < NeuronMap[0].length; j++) {
                    double sum_loss = 0;
                    for (int k = 0; k < output_num; k++) {
                        sum_loss += NeuronMap[1][k].getResidual() * WeightMap[1][outputweight_num];
                        outputweight_num++;
                    }
                    residual = NeuronMap[0][j].execDerivative() * sum_loss;
                    WeightMap[0][weight_num] += -learningrate * residual * inputlist.get(i);
                    weight_num++;
                }
                outputweight_num = 0;
            }

            //cal out->hidden layer
            //Using Partial derivative and chain derivative rule
            //w+=w-learningrate*residual*output_hiddenlayer_neuron
            //residual=activation_derivative*loss_derivative
            weight_num = 0;
            for (int i = 0; i < NeuronMap[0].length; i++) {
                for (int j = 0; j < NeuronMap[1].length; j++) {
                    residual = NeuronMap[1][j].getResidual();
                    WeightMap[1][weight_num] += -learningrate * residual * NeuronMap[0][i].getoutput();
                    weight_num++;
                }
            }
        }
        //init
        inputlist.clear();
    }

    public void test(){
        if(layers_num==3) {
            double totalerror=0;
            for (int i = 0; i < output_num; i++) {
                LossFunction lmse=LossFunction.MSE;
                totalerror+=lmse.exec(NeuronMap[1][i].getTarget(),NeuronMap[1][i].getoutput(),output_num);
            }
            Statistic.addError(totalerror);
            Statistic.addSampleNum();
        }
    }


    public void printNetwork(){
        for(int i=0;i<WeightMap.length;i++){
            for(int j=0;j<WeightMap[i].length;j++) {
                System.out.println(WeightMap[i][j]);
            }
        }
    }
}

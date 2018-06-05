package Network.Conponent;

import Activation.Neuron;

import java.util.ArrayList;

public class LayerKernel {
    private static Neuron NeuronMap[][];
    private static double WeightMap[][];

    private int layers_num;
    private static int hiddenlayers_num;
    private static int hiddenneurons_num[];
    private static int output_num;
    private static int input_num;
   // private int weight_num;
    private static Weightinit weightinit_function;

    private static boolean ifMomentumAlgorithm=false;
    private static double momentum;
    private static double MomentumMap[][];
    private static ArrayList<Double> SoftmaxValue=new ArrayList<Double>();

    private static double BiasMap[][];

    public LayerKernel(LayerConf inputlayer, ArrayList<LayerConf> hiddenlayers, LayerConf outputlayer){
        this.hiddenlayers_num=hiddenlayers.size();
        layers_num=2+hiddenlayers_num;
        output_num=outputlayer.getOutput_num();
        input_num=inputlayer.getInput_num();
        weightinit_function=inputlayer.getWeightinit();

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
                //NeuronMap[i][j].printname();
            }
        }
        for(int i=0;i<output_num;i++){
            NeuronMap[hiddenlayers_num][i]=outputlayer.getActivation().getNeuron();
        }

        //init weight
        WeightMap=new double[layers_num-1][];
        if(hiddenlayers_num==1){
            //weight_num+=(input_num+output_num)*hiddenneurons_num[0];
            WeightMap[0] = new double [input_num*hiddenneurons_num[0]];//plus 1 bias layer
            WeightMap[1] = new double [output_num*hiddenneurons_num[0]];
        }
        else{
            //weight_num+=input_num*hiddenneurons_num[0];
            WeightMap[0] = new double [input_num*hiddenneurons_num[0]];
            for(int i=0;i<hiddenlayers_num;i++){
                if(i+1==hiddenlayers_num){
                    //weight_num+=hiddenneurons_num[i]*output_num;
                    WeightMap[i+1] = new double [output_num*hiddenneurons_num[0]];
                }
                else {
                    //weight_num += hiddenneurons_num[i] * hiddenneurons_num[i + 1];
                    WeightMap[i+1] = new double [hiddenneurons_num[i] * hiddenneurons_num[i + 1]];
                }
            }
        }

        for(int i=0;i<WeightMap.length;i++){
            for(int j=0;j<WeightMap[i].length;j++) {
                WeightMap[i][j]= weightinit_function.init();
                //   System.out.println(WeightMap[i][j]);
            }
        }

        //init BiasMap
        BiasMap=new double [layers_num-1][];
        for(int i=0;i<hiddenlayers_num+1;i++){
            BiasMap[i]=new double [NeuronMap[i].length];
            for(int j=0;j<BiasMap[i].length;j++){
                BiasMap[i][j]=0;
            }
        }

        //init MomentumMap
        if(ifMomentumAlgorithm==true) {
            MomentumMap = new double[layers_num - 1][];
            for (int i = 0; i <layers_num - 1;i++){
                MomentumMap[i]=new double [WeightMap[i].length];
                for(int j=0;j<WeightMap[i].length;j++){
                    MomentumMap[i][j]=0;
                }
            }
        }
    }

    public static double getWeight(int row,int line){
        return WeightMap[row][line];
    }

    public static void updateWeight(int row,int line,double value){
        if(ifMomentumAlgorithm==false) {
            WeightMap[row][line] += value;
        }
        else{
            MomentumMap[row][line]=momentum*MomentumMap[row][line]+value;
            WeightMap[row][line] += MomentumMap[row][line];
        }
    }

    public static int getWeightRowLength(){
        return WeightMap.length;
    }

    public static int getWeightLineLength(int row){
        return WeightMap[row].length;
    }

    public static void updateNeuronOutput(int row,int line,double input){
        if(NeuronMap[row][line].getname().equals("Softmax")){
            NeuronMap[row][line].execOutput(input);
            SoftmaxValue.add(Math.exp(input));
            //System.out.println(Math.exp(input));
        }
        else {
            NeuronMap[row][line].execOutput(input);
        }
    }

    public static void CheckUpdate(int row){
        //NeuronMap[0][0].printname();
        if(NeuronMap[row][0].getname().equals("Softmax")){
            double total=0;
            for(int i=0;i<SoftmaxValue.size();i++){
                total+=SoftmaxValue.get(i);
            }
            //System.out.println(total);
            for(int i=0;i<NeuronMap[row].length;i++){
                //System.out.println(NeuronMap[row][i].getinput()+" "+total);
                NeuronMap[row][i].execOutput(Math.exp(NeuronMap[row][i].getinput()),total);
            }
            SoftmaxValue.clear();
        }
    }

    public static double getNeuronOutput(int row,int line){
        return NeuronMap[row][line].getoutput();
    }

    public static void setNeuronTarget(int row,int line,double target){
        NeuronMap[row][line].setTarget(target);
    }

    public static double getNeuronTarget(int row,int line){
        return NeuronMap[row][line].getTarget();
    }

    public static void setNeuronError(int row,int line,double error){
        NeuronMap[row][line].setError(error);
    }

    public static double getNeuronError(int row,int line){
        return NeuronMap[row][line].getError();
    }

    public static double getNeuronRowLength(){
        return NeuronMap.length;
    }

    public static double getNeuronLineLength(int row){
        return NeuronMap[row].length;
    }

    public static double getNeuronDerivative(int row,int line){
        return NeuronMap[row][line].execDerivative();
    }

    public static void setNeuronResidual(int row,int line,double residual){
        NeuronMap[row][line].setResidual(residual);
    }

    public static double getNeuronResidual(int row,int line){
        return NeuronMap[row][line].getResidual();
    }

    public static int getHiddenNeuronsNum(int row){
        return hiddenneurons_num[row];
    }

    public static void useMomentumAlgorithm(double m){
        ifMomentumAlgorithm=true;
        momentum=m;
    }

    public static double getMomentum(int row,int line){
        return MomentumMap[row][line];
    }

    public static double getBias(int row,int line){
        return BiasMap[row][line];
    }

    public static void updateBias(int row, int line, double bias){
        BiasMap[row][line]=bias;
    }
}

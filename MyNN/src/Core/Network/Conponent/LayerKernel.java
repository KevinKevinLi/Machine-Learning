package Core.Network.Conponent;

import Core.Network.Activation.Neuron;

import java.util.ArrayList;
import java.util.Random;

public class LayerKernel {
    private static Neuron NeuronMap[][];
    private static double WeightMap[][];
    private static double WeightMap_backup[][];

    private int layers_num;
    private static int hiddenlayers_num;
    private static int hiddenneurons_num[];
    private static int output_num;
    private static int input_num;
   // private int weight_num;
    private static Weightinit weightinit_function;
    private static long seed=0;

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
        seed =inputlayer.getSeed();

        //init neurons
        NeuronMap=new Neuron[layers_num][];
        //init neurons number
        this.hiddenneurons_num=new int [layers_num-1];
        NeuronMap[0]=new Neuron[input_num];
        for(int i=0;i<hiddenlayers_num;i++) {
            hiddenneurons_num[i]=hiddenlayers.get(i).getHidden_num();
            NeuronMap[i+1]=new Neuron[hiddenneurons_num[i]];
        }
        NeuronMap[layers_num-1]=new Neuron[output_num];
        //init neuron activations
        for(int i=0;i<NeuronMap.length;i++){
            for(int j=0;j<NeuronMap[i].length;j++){
                if(i==0){
                    NeuronMap[i][j]=inputlayer.getActivation().getNeuron();
                }
                else if(i==NeuronMap.length-1){
                    NeuronMap[i][j]=outputlayer.getActivation().getNeuron();
                }
                else {
                    NeuronMap[i][j] = hiddenlayers.get(i-1).getActivation().getNeuron();
                }
            }
        }

        //init weight
        WeightMap=new double[layers_num-1][];

        //weight_num+=input_num*hiddenneurons_num[0];
        for(int i=0;i<layers_num-1;i++){
            WeightMap[i] = new double [NeuronMap[i].length*NeuronMap[i+1].length];
            //System.out.println(WeightMap[i].length+" "+NeuronMap[i].length);

        }
        //init use weight init function
        Random random = new Random(seed);
        for(int i=0;i<WeightMap.length;i++){
            for(int j=0;j<WeightMap[i].length;j++) {
                WeightMap[i][j] = weightinit_function.init(NeuronMap[i].length,NeuronMap[i+1].length,random,seed);
                //   System.out.println(WeightMap[i][j]);
            }
        }

        //init weightmap_backup
        WeightMap_backup=new double[layers_num-1][];
        for(int i=0;i<layers_num-1;i++){
            WeightMap_backup[i] = new double [NeuronMap[i].length*NeuronMap[i+1].length];
            //System.out.println(WeightMap[i].length+" "+NeuronMap[i].length);
        }
        cloneWeightMap();

        //init BiasMap
        BiasMap=new double [layers_num-1][];
        for(int i=0;i<hiddenlayers_num+1;i++){
            BiasMap[i]=new double [NeuronMap[i+1].length];
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

    public static double getWeight(int col,int line){
        return WeightMap[col][line];
    }

    public static void updateWeight(int col,int line,double value){
        if(ifMomentumAlgorithm==false) {
            WeightMap[col][line] += value;
        }
        else{
            MomentumMap[col][line]=momentum*MomentumMap[col][line]+value;
            WeightMap[col][line] += MomentumMap[col][line];
        }
    }

    public static int getWeightcolLength(){
        return WeightMap.length;
    }

    public static int getWeightLineLength(int col){
        return WeightMap[col].length;
    }

    public static void updateNeuronOutput(int col,int line,double input){
        if(NeuronMap[col][line].getname().equals("Softmax")){
            NeuronMap[col][line].execOutput(input);
            SoftmaxValue.add(Math.exp(input));
            //System.out.println(Math.exp(input));
        }
        else {
            NeuronMap[col][line].execOutput(input);
        }
    }

    public static void CheckUpdate(int col){
        //NeuronMap[0][0].printname();
        if(NeuronMap[col][0].getname().equals("Softmax")){
            double total=0;
            for(int i=0;i<SoftmaxValue.size();i++){
                total+=SoftmaxValue.get(i);
            }
            //System.out.println(total);
            for(int i=0;i<NeuronMap[col].length;i++){
                //System.out.println(NeuronMap[col][i].getinput()+" "+total);
                NeuronMap[col][i].execOutput(Math.exp(NeuronMap[col][i].getinput()),total);
            }
            SoftmaxValue.clear();
        }
    }

    public static double getNeuronOutput(int col,int line){
        return NeuronMap[col][line].getoutput();
    }

    public static void setNeuronTarget(int col,int line,double target){
        NeuronMap[col][line].setTarget(target);
    }

    public static double getNeuronTarget(int col,int line){
        return NeuronMap[col][line].getTarget();
    }

    public static void setNeuronError(int col,int line,double error){
        NeuronMap[col][line].setError(error);
    }

    public static double getNeuronError(int col,int line){
        return NeuronMap[col][line].getError();
    }

    public static double getNeuroncolLength(){
        return NeuronMap.length;
    }

    public static double getNeuronLineLength(int col){
        return NeuronMap[col].length;
    }

    public static double getNeuronDerivative(int col,int line){
        return NeuronMap[col][line].execDerivative();
    }

    public static void setNeuronResidual(int col,int line,double residual){
        NeuronMap[col][line].setResidual(residual);
    }

    public static double getNeuronResidual(int col,int line){
        return NeuronMap[col][line].getResidual();
    }

    public static int getHiddenNeuronsNum(int col){
        return hiddenneurons_num[col];
    }

    public static void useMomentumAlgorithm(double m){
        ifMomentumAlgorithm=true;
        momentum=m;
    }

    public static double getMomentum(int col,int line){
        return MomentumMap[col][line];
    }

    public static double getBias(int col,int line){
        return BiasMap[col][line];
    }

    public static int getBiascolLength(){ return BiasMap.length; }

    public static int getBiasLineLength(int col){return BiasMap[col].length;};

    public static void updateBias(int col, int line, double bias){
        BiasMap[col][line]=bias;
    }

    public static void cloneWeightMap(){
        for(int i=0;i<WeightMap.length;i++){
            for(int j=0;j<WeightMap[i].length;j++){
                WeightMap_backup[i][j] = WeightMap[i][j];
            }
        }
    }

    public static double getBackupWeight(int col,int line){
        return WeightMap_backup[col][line];
    }

    public static String getNeuronActivation(int col, int line){
        return  NeuronMap[col][line].getname();
    }

    public static double[][] getWeightMap() {return WeightMap; }

    public static void setWeightMap(double [][]weightmap,double [][]biasmap){ WeightMap=weightmap; BiasMap=biasmap;}

}

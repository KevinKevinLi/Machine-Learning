package Core.Network.Conponent;

import Core.Network.Activation.ActivationFrame;
import Core.Network.LossFunction.LossFunction;

public class LayerConf {
    private static int input_num=0;
    private static int output_num=0;
    private int hidden_num=0;
    private String layer_name;
    private static int layer_num;
    private ActivationFrame activation;
    private static LossFunction lossfunction;
    private static Weightinit weightinit;
    private static long seed;

    //InputLayer
    public LayerConf(int input_num,Weightinit weightinit,ActivationFrame activation,long seed){
        layer_name="InputLayer";
        this.weightinit=weightinit;
        this.activation=activation;
        this.input_num=input_num;
        this.seed=seed;
        layer_num=0;
    }

    public static int getInput_num() {
        return input_num;
    }

    public Weightinit getWeightinit(){
        return this.weightinit;
    }

    //Hidden LayerConf
    public LayerConf(int layer_num, int hiddenneuron_num, ActivationFrame activation){
        layer_name="HiddenLayer";
        this.hidden_num=hiddenneuron_num;
        this.activation=activation;
        this.layer_num=layer_num;
    }

    public ActivationFrame getActivation(){
        //System.out.println(layer_name+" "+activation.getNeuron().getname());
        return this.activation;
    }

    public int getHidden_num(){
        return this.hidden_num;
    }

    //OutputLayer
    public LayerConf(int layer_num, int output_num, ActivationFrame activation, LossFunction lossfunction){
        layer_name="OutputLayer";
        this.output_num=output_num;
        this.lossfunction=lossfunction;
        this.activation=activation;
        this.layer_num=layer_num;
    }

    public LossFunction getLossfunction(){
        return lossfunction;
    }

    public int getOutput_num(){
        return output_num;
    }

    public long getSeed(){return seed;}
}

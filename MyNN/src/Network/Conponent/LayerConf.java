package Network.Conponent;

import Activation.ActivationFrame;
import LossFunction.LossFunction;

import java.util.ArrayList;

public class LayerConf {
    private static int input_num=0;
    private static int output_num=0;
    private static int hidden_num=0;
    private static String layer_name;
    private static int layer_num;
    private static ActivationFrame activation;
    private static LossFunction lossfunction;
    private static Weightinit weightinit;

    //InputLayer
    public LayerConf(int input_num,Weightinit weightinit){
        layer_name="InputLayer";
        this.weightinit=weightinit;
        this.input_num=input_num;
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
}

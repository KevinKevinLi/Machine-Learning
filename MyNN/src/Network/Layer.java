package Network;

import Activation.ActivationFrame;
import LossFunction.LossFunction;

public class Layer {
    private static int input_num=0;
    private static int output_num=0;
    private static int hidden_num=0;
    private static String layer_name;
    private static int layer_num;
    private static ActivationFrame activation;
    private static LossFunction lossfunction;

    public Layer(int input_num){
        layer_name="InputLayer";
        this.input_num=input_num;
        layer_num=0;
    }

    public static int getInput_num() {
        return input_num;
    }

    public Layer(int layer_num, int hiddenneuron_num, ActivationFrame activation){
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

    public Layer(int layer_num,int output_num, ActivationFrame activation,LossFunction lossfunction){
        layer_name="HiddenLayer";
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

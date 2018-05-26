package Network;
import java.util.*;

import Activation.ActivationFrame;
import LossFunction.LossFunction;

public class NetworkConfigration {
    private static int layer_num=0;
    private static int input_num=0;
    private static int output_num=0;
    private static double learningrate=0;
    private static double momentum=0;
    private static Weight weight;
    private Layer inputlayer;
    private ArrayList<Layer> hiddenlist = new ArrayList<Layer>();
    private Layer outputlayer;

    public NetworkConfigration(){
    }

    public NetworkConfigration base(double learningrate,Weight weight){
        this.learningrate=learningrate;
        this.weight=weight;
        return this;
    }

    public NetworkConfigration inputlayer(int input_num){
        layer_num++;
        this.input_num=input_num;
        inputlayer=new Layer(input_num);
        return this;
    }

    public NetworkConfigration hiddenlayer(int hiddenneuron_num, ActivationFrame activation){
        layer_num++;
        hiddenlist.add(new Layer(layer_num,hiddenneuron_num,activation));
        return this;
    }

    public NetworkConfigration outputlayer(int output_num, ActivationFrame activation,LossFunction lossfunction){
        layer_num++;
        this.output_num=output_num;
        outputlayer=new Layer(layer_num,output_num,activation,lossfunction);
        return this;
    }

    public NetworkConfigration build(){
        return this;
    }

    public int getInput_num(){
        return this.input_num;
    }

    public int getOutput_num(){
        return this.output_num;
    }

    public int getLayer_num(){
        return this.layer_num;
    }

    public double getLearningrate(){
        return learningrate;
    }

    public Weight getWeight(){
        return this.weight;
    }

    public Layer getInputlayer(){
        return this.inputlayer;
    }

    public ArrayList<Layer> getHiddenlayer(){
        return this.hiddenlist;
    }

    public Layer getOutputlayer(){
        return this.outputlayer;
    }
}

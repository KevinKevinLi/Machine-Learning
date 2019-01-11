package Core.Network;
import java.util.*;

import Core.Network.Activation.ActivationFrame;
import Core.Network.LossFunction.LossFunction;
import Core.Network.Conponent.LayerConf;
import Core.Network.Conponent.Weightinit;

public class NetworkConfigration {
    private int layer_num=0;
    private int input_num=0;
    private int output_num=0;
    private double learningrate=0;
    private double momentum=0;
    private long seed=0;
    private  Weightinit weightinit;
    private LayerConf inputlayer;
    private ArrayList<LayerConf> hiddenlist = new ArrayList<LayerConf>();
    private LayerConf outputlayer;
    private  LossFunction lossfunction;
    private double [][]weightmap;
    private double [][]biasmap;

    public NetworkConfigration(){
    }

    public NetworkConfigration base(double learningrate){
        this.learningrate=learningrate;
        return this;
    }

    public NetworkConfigration base(double learningrate,double momentum){
        this.learningrate=learningrate;
        this.momentum=momentum;
        return this;
    }

    public NetworkConfigration base(double learningrate,double momentum,long seed){
        this.learningrate=learningrate;
        this.momentum=momentum;
        this.seed=seed;
        return this;
    }

    public NetworkConfigration inputlayer(int input_num,Weightinit weightinit,ActivationFrame act){
        layer_num++;
        this.input_num=input_num;
        inputlayer=new LayerConf(input_num,weightinit,act,seed);
        return this;
    }

    public NetworkConfigration hiddenlayer(int hiddenneuron_num, ActivationFrame activation){
        layer_num++;
        hiddenlist.add(new LayerConf(layer_num,hiddenneuron_num,activation));
        //System.out.println(hiddenlist.get(0).getActivation().getNeuron().getname());
        return this;
    }

    public NetworkConfigration outputlayer(int output_num, ActivationFrame activation,LossFunction lossfunction){
        layer_num++;
        this.output_num=output_num;
        outputlayer=new LayerConf(layer_num,output_num,activation,lossfunction);
        this.lossfunction=lossfunction;
        return this;
    }

    public NetworkConfigration setweightmap(double [][]weightmap,double [][]biasmap){
        this.weightmap=weightmap;
        this.biasmap=biasmap;
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
        return this.learningrate;
    }

    public double getMomentum(){
        return this.momentum;
    }

    public long getSeed() { return this.seed; }

    public Weightinit getWeight(){
        return this.weightinit;
    }

    public LayerConf getInputlayer(){
        return this.inputlayer;
    }

    public ArrayList<LayerConf> getHiddenlayer(){
        return this.hiddenlist;
    }

    public LayerConf getOutputlayer(){
        return this.outputlayer;
    }

    public LossFunction getLossfunction(){return this.lossfunction;}

    public double [][] getWeightmap(){return this.weightmap;}

    public double [][] getBiasMap(){return this.biasmap;}
}

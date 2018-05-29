package Network;
import Listeners.*;

import Activation.*;
import LossFunction.LossFunction;
import Network.Conponent.Weightinit;

public class FeedFowardNetwork {
    private NetworkKernel net;
    private Listeners.Listener Statistic;

    public FeedFowardNetwork(){
    }

    public void SetConfiguration(NetworkConfigration conf){
        net=new NetworkKernel();
        if(conf.getMomentum()==0.0) {
            net.setbase(conf.getLayer_num(), conf.getInput_num(), conf.getOutput_num(), conf.getLearningrate());
        }
        else{
            net.setbase(conf.getLayer_num(), conf.getInput_num(), conf.getOutput_num(), conf.getLearningrate(),conf.getMomentum());
        }
        net.setlayers(conf.getInputlayer(),conf.getHiddenlayer(),conf.getOutputlayer());
        Statistic = new Listener();
        net.setListener(Statistic);
    }

    public void train(double [][] inputset,int traintimes){
        for(int i=0;i<traintimes;i++){
            for(int j=0;j<inputset.length;j++){
                net.feedforward(inputset[j]);
                net.backpropagation();
            }
            Statistic.printMeanSquareError();
            Statistic.init();
        }
        //net.printNetwork();
    }

    public void train(double [][] inputset,double maxerror){
        double flagerror=0;
        do{
            for(int i=0;i<inputset.length;i++){
                net.feedforward(inputset[i]);
                net.backpropagation();
            }
            Statistic.printMeanSquareError();
            flagerror=Statistic.getMeanError();
            Statistic.init();
        }while(flagerror>maxerror);
    }

    public void test(double [][]inputset){
        for(int i=0;i<inputset.length;i++){
            net.feedforward(inputset[i]);
            net.test();
        }
        Statistic.printTestError();
    }

}

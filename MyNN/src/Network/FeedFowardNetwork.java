package Network;
import Listeners.*;

import Activation.*;

public class FeedFowardNetwork {
    private NetworkKernel net;
    private Listeners.Listener Statistic;

    public FeedFowardNetwork(){
    }

    public void SetConfigure(int input, int output, int hiddenneurons, double learningrate, Weight weight, ActivationFrame activationFrame, LossFunction lossfunction){
        net=new NetworkKernel(input, output, hiddenneurons, learningrate, weight, activationFrame, lossfunction);
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

    }

    public void test(double [][]inputset){

    }


}

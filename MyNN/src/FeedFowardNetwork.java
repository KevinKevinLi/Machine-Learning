import Activation.*;
import Network.LossFunction;
import Network.*;

public class FeedFowardNetwork {
    private NetworkKernel net;

    public FeedFowardNetwork(){
    }

    public void SetConfigure(int input, int output, int hiddenneurons, double learningrate, Weight weight, ActivationFrame activationFrame, LossFunction lossfunction){
        net=new NetworkKernel(input, output, hiddenneurons, learningrate, weight, activationFrame, lossfunction);
    }

    public void train(double [][] inputset,int traintimes){
        for(int i=0;i<traintimes;i++){
            for(int j=0;j<inputset.length;j++){
                net.feedforward(inputset[j]);
                net.backpropagation();
            }
        }
    }

    public void train(double [][] inputset,double maxerror){

    }

    public void test(double [][]inputset){

    }
}

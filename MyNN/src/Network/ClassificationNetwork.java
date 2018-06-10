package Network;
import Listeners.*;
import LossFunction.LossFunction;
import Exception.*;

public class ClassificationNetwork {
    private FeedForwardKernel net;
    private Listeners.Listener Statistic;
    private int output_num=0;
    private int input_num=0;
   // private RecordPolice Police=new RecordPolice();

    public ClassificationNetwork(){
    }

    public void SetConfiguration(NetworkConfigration conf){
        net=new FeedForwardKernel();
        output_num=conf.getOutput_num();
        input_num=conf.getInput_num();
        if(conf.getMomentum()==0.0) {
            net.setbase(conf.getLayer_num(), conf.getInput_num(), conf.getOutput_num(), conf.getLearningrate());
        }
        else{
            net.setbase(conf.getLayer_num(), conf.getInput_num(), conf.getOutput_num(), conf.getLearningrate(),conf.getMomentum());
        }
        //conf.getHiddenlayer().get(0).getActivation().getNeuron().printname();
        net.setlayers(conf.getInputlayer(),conf.getHiddenlayer(),conf.getOutputlayer());
        Statistic = new Listener(conf.getLossfunction());
        net.setListener(Statistic);
    }

    public void train(double [][] inputset,int traintimes){
        for(int i=0;i<traintimes;i++){
            for(int j=0;j<inputset.length;j++){
                net.feedforward(inputset[j]);
                net.backpropagation();
            }
            try {
                Statistic.printMeanSquareError();
            }
            catch (GRADIENTBOOMEXCEPTION e){
                e.printError();
                Statistic.init();
                break;
            }
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
            try {
                Statistic.printMeanSquareError();
            }
            catch (GRADIENTBOOMEXCEPTION e) {
                e.printError();
                Statistic.init();
                break;
            }
            flagerror=Statistic.getMeanError();
            Statistic.init();
        }while(flagerror>maxerror);
    }

    public void test(double [][]inputset){
        net.setLossfunction(LossFunction.Mse);
        for(int i=0;i<inputset.length;i++){
            //Statistic.printpara();
            net.feedforward(inputset[i]);
        }
        //net.printNetwork();
        Statistic.printTestError();
        Statistic.init();
    }

    public void predict(double [][]inputset,double distance){
        for(int i=0;i<inputset.length;i++) {
            //Statistic.printpara();
            //MSE LOSS FUNCTION
            net.setLossfunction(LossFunction.Mse);
            if(Math.sqrt(net.feedforward(inputset[i]))<distance){
                Statistic.predictsuccess();
            }
        }
        Statistic.printPredict();
    }

}

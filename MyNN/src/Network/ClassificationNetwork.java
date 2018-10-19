package Network;
import Listeners.*;
import LossFunction.LossFunction;
import Exception.*;
import java.text.DecimalFormat;

public class ClassificationNetwork {
    private FeedForwardKernel net;
    private Listeners.Listener Statistic;
    private Listeners.Graph Chart;
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
            net.setbase(conf.getLayer_num(), conf.getInput_num(), conf.getOutput_num(), conf.getLearningrate(),conf.getSeed());
        }
        else{
            net.setbase(conf.getLayer_num(), conf.getInput_num(), conf.getOutput_num(), conf.getLearningrate(),conf.getMomentum(),conf.getSeed());
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

    //continues
    public void predict(double [][]inputset,String path){
        Chart=new Graph();
        DecimalFormat df = new DecimalFormat("#.##");
        for(int i=0;i<inputset.length;i++) {
            net.setLossfunction(LossFunction.DoNothing);
            double predict = net.feedforward(inputset[i]);
            String ck=Integer.toString(i);
            double t=inputset[i][0]*2257.48+679.28;
            Chart.add((predict*195.6-104.01)+t,"predict",ck);
            Chart.add((inputset[i][4]*195.6-104.01)+t,"actuall",ck);
//            predict=predict*45.65+13.14;
//            double actuall=(inputset[i][5]*45.65)+13.14;
//            double close=inputset[i][4]*45.45+13.02;
//            //customize
//            //start of nextday
//            System.out.print("Predict:"+df.format(predict)+"  Actual:"+df.format(actuall));
//            //end of today
//            System.out.print("  Close:"+df.format(close));
//            if(actuall>=close&&predict>=close||actuall<=close&&predict<=close){
//                System.out.println("  success");
//                Statistic.predictsuccess();
//            }
//            else{
//                System.out.println("  failed");
//            }
//        }
//        Statistic.printPredict();
        }
        Chart.createChart(path);
    }
}

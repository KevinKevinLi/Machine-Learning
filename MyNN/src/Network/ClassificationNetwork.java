package Network;
import Listeners.*;
import Network.LossFunction.LossFunction;
import Exception.*;


import java.text.DecimalFormat;

public class ClassificationNetwork {
    private FeedForwardKernel net;
    private Listeners.Listener Statistic;
    private Listeners.Chart Chart;
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

    public void SetWeightMap(NetworkConfigration conf){
        net.setWeightmap(conf.getWeightmap(),conf.getBiasMap());
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

    public void periodtrain(double [][] inputset,int start,int end, int times){
        for(int i=0;i<times;i++){
            for(int j=start;j<end;j++){
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

    public void test(double [][]inputset){
        net.setLossfunction(LossFunction.Mse);
        for(int i=0;i<inputset.length;i++){
            //Statistic.printpara();
            net.feedforward(inputset[i]);
            Statistic.printTestError();
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

    public void predict(double [][]inputset){
        for(int i=0;i<inputset.length;i++) {
            //Statistic.printpara();
            //MSE LOSS FUNCTION
            net.setLossfunction(LossFunction.DoNothing);
            double predict = net.feedforward(inputset[i])*2254.22+676.53 ;
            System.out.println("Main.Predict.Predict: "+predict+" Actuall: "+(inputset[i][5]*2254.22+676.53));
        }
    }

    public void predict(double [][]inputset,String path){
        Chart=new Chart();
        net.setLossfunction(LossFunction.DoNothing);
        DecimalFormat df = new DecimalFormat("#.##");
        //double predict=inputset[1][0]*2257.48+679.28;
        for(int i=0;i<inputset.length;i++) {
            //predict += (net.feedforward(inputset[i]) * 195.6 - 104.01);
            double predict=0;
            predict = net.feedforward(inputset[i]) * 2254.22 + 676.53;
            String ck=Integer.toString(i);
            //double t=inputset[i+1][0]*2257.48+679.28;
            Chart.add(predict,"predict",ck);
            Chart.add((inputset[i][5]*2254.22+676.53),"actuall",ck);
//
        }
        Chart.createChart(path);
    }

    //continues
    public void predict(double [][]inputset,String path,int period,int traintimes){
        Chart=new Chart();
        DecimalFormat df = new DecimalFormat("#.##");
        //double predict=inputset[1][0]*2257.48+679.28;
        for(int i=0;i<inputset.length;i++) {
            //predict += (net.feedforward(inputset[i]) * 195.6 - 104.01);
            double predict=0;
            net.setLossfunction(LossFunction.DoNothing);
            predict = net.feedforward(inputset[i]) * 2254.22 + 676.53;
            //predict = net.feedforward(inputset[i]);
            String ck=Integer.toString(i);
            //double t=inputset[i+1][0]*2257.48+679.28;
            Chart.add(predict,"predict",ck);
            //double actuall=inputset[i][5]*195.6-104.01;
            double actuall=inputset[i][7]* 2254.22 + 676.53;
            Chart.add(actuall,"actuall",ck);
            double close=inputset[i][6]* 2254.22 + 676.53;
            if(predict-close>0&&actuall-close>0||predict-close<0&&actuall-close<0){
                Statistic.predictsuccess();
            }

            if(i!=0&&(i+1)%period==0||period==1) {
                net.setLossfunction(LossFunction.Mse);
                periodtrain(inputset, i - period+1, i + 1, traintimes);
            }
        }
        net.setLossfunction(LossFunction.Mse);
        Chart.createChart(path);
        Statistic.printPredict();
        //Statistic.printPredict();
        //Statistic.printpara();
    }

    public void saveas(String path) {
        net.savenet(path,Statistic.getTestError());
    }



}

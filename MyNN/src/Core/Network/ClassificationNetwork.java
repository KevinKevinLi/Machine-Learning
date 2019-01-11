package Core.Network;
import Core.Listeners.*;
import Core.Network.LossFunction.LossFunction;
import Core.Exception.*;
import java.text.DecimalFormat;

public class ClassificationNetwork {
    private FeedForwardKernel net;
    private Core.Listeners.Listener Statistic;
    private int output_num=0;
    private int input_num=0;
    // private RecordPolice Police=new RecordPolice();
    private ChartType Chart;
    private String chartpath;
    private String []labels;
    private boolean iflabel=false;

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
            System.out.println("SP500.PredictTest.PredictTest: "+predict+" Actuall: "+(inputset[i][5]*2254.22+676.53));
        }
    }

    public void chart(ChartType Chart, String outpath){
        this.Chart=Chart;
        this.chartpath=outpath;
    }

    public void setlabels(String []labels){
        this.labels=labels;
        iflabel=true;
    }

    //continues
    public void predict(double [][]inputset, int period, int traintimes){

        double record[][]=new double[inputset.length][3];
        int []sta=new int[inputset.length];
        boolean []correctness=new boolean[inputset.length];

        DecimalFormat df = new DecimalFormat("#.##");
        for(int i=0;i<inputset.length;i++) {
            String ck=Integer.toString(i);

            net.setLossfunction(LossFunction.DoNothing);
            double predict = net.feedforward(inputset[i]) * 2254.22 + 676.53;
            double close=inputset[i][6]* 2254.22 + 676.53;
            double actuall=inputset[i][7]* 2254.22 + 676.53;
            Chart.feed(predict, actuall, close, ck);

            record[i][0] = predict;
            record[i][1] = actuall;
            record[i][2] = close;

            if(predict - close > 0){
                sta[i]=1;
            }
            else{
                sta[i]=-1;
            }
            if(actuall>0) {
                if (Math.signum(predict-close)==Math.signum(actuall-close)) {
                    Statistic.predictsuccess();
                    correctness[i]=true;
                }
                else{
                    correctness[i]=false;
                }
            }

            if(i!=0&&(i+1)%period==0||period==1) {
                net.setLossfunction(LossFunction.Mse);
                periodtrain(inputset, i - period+1, i + 1, traintimes);
            }
        }
        net.setLossfunction(LossFunction.Mse);
        System.out.println("********** Statistic **********");
        //Statistic.printPredict();
        //Statistic.printpara();
        Statistic.init();
        for(int i=0;i<record.length;i++){
            // print predict close
            if(iflabel==true) {
                System.out.print("prediction close for "+labels[i]+": " + record[i][0]);
            }
            else {
                System.out.print(record[i][0]);
            }
            //tendency
            System.out.print(" ,tendency: ");
            if(sta[i]==1){
                System.out.print("up");
            }
            else{
                System.out.print("down");
            }
            //correctness
            if(correctness[i]==true){
                System.out.println(", correct");
            }
            else{
                if(i<record.length-1) {
                    System.out.println(", incorrect");
                }
                else{
                    System.out.println("");
                }
            }
            Statistic.addSampleNum();
        }
        Chart.draw(chartpath);
        Statistic.printPredict();

//        for(int i=0;i<sta.length;i++){
//            System.out.println(sta[i]);
//        }
    }

    public int getpredict(){
        return Statistic.getPredict();
    }

    public void saveas(String path) {
        net.savenet(path,Statistic.getTestError());
    }
}

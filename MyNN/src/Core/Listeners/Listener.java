package Core.Listeners;

import Core.Network.LossFunction.LossFunction;
import Core.Exception.*;

public class Listener {
    private double totalError=0;
    private  double totalSamples=0;
    private  int interationtimes=0;
    private  int predict_success=0;
    private  LossFunction lossfunction;
    private  double testerror=0;

    public Listener(LossFunction lossfunc){
        lossfunction=lossfunc;
    }

    public void addError(double error){
        totalError+=error;
    }

    public void addSampleNum(){
        totalSamples++;
    }

    public  void predictsuccess(){predict_success++;}

    public  void init(){
        totalError=0.0;
        totalSamples=0.0;
    }

    public  void printMeanSquareError() throws GRADIENTBOOMEXCEPTION {
        interationtimes++;
        double score=totalError / totalSamples;

        if(Double.isNaN(score)){
            throw new GRADIENTBOOMEXCEPTION();
        }

        switch (lossfunction) {
            case Subtraction:
                System.out.println("Interation:" + interationtimes + " Subtraction Score is " + score);
                break;
            case Mse:
                System.out.println("Interation:" + interationtimes + " Mean Square Error is " + score);
                break;
            case CrossEntropy:
                System.out.println("Interation:" + interationtimes + " CrossEntropy Score is " + score);
                break;
            case NegativeLogLikeliHood:
                System.out.println("Interation:" + interationtimes + " NegativeLikeliHood Score is " + score);
                break;
            default:
                System.out.println("Interation:" + interationtimes + " Score is " + score);
                break;
        }
    }
    public  void printTestError(){
        testerror=totalError/totalSamples;
        System.out.println("Test Total Mean Square Error is "+testerror);
    }

    public double getTestError(){
        return testerror;
    }

    public  void printPredict(){
        if(predict_success==0){
            System.out.println("SP500.PredictTest.PredictTest Error: " + totalError / totalSamples);
        }
        else {
            System.out.println("SP500.PredictTest.PredictTest Success Number: " + predict_success +"/"+(int)totalSamples);
        }
    }

    public  int getPredict(){
        return predict_success;
    }

    public  double getMeanError(){
        return totalError/totalSamples;
    }

    public  void printpara(){
        System.out.println("totalerror:"+totalError+" totalsample:"+totalSamples);
    }
}

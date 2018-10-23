package Listeners;

import LossFunction.LossFunction;
import Exception.*;

public class Listener {
    private static double totalError=0;
    private static double totalSamples=0;
    private static int interationtimes=0;
    private static int predict_success=0;
    private static LossFunction lossfunction;
    private static double testerror=0;

    public Listener(LossFunction lossfunc){
        lossfunction=lossfunc;
    }

    public void addError(double error){
        totalError+=error;
    }

    public void addSampleNum(){
        totalSamples++;
    }

    public static void predictsuccess(){predict_success++;}

    public static void init(){
        totalError=0.0;
        totalSamples=0.0;
    }

    public static void printMeanSquareError() throws GRADIENTBOOMEXCEPTION {
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
    public static void printTestError(){
        testerror=totalError/totalSamples;
        System.out.println("Test Total Mean Square Error is "+testerror);
    }

    public static double getTestError(){
        return testerror;
    }

    public static void printPredict(){
        if(predict_success==0){
            System.out.println("Predict Error: " + totalError / totalSamples);
        }
        else {
            System.out.println("Predict Accuracy: " + predict_success / totalSamples);
        }
    }

    public static double getMeanError(){
        return totalError/totalSamples;
    }

    public static void printpara(){
        System.out.println("totalerror:"+totalError+" totalsample:"+totalSamples);
    }
}

package Listeners;

import LossFunction.LossFunction;

public class Listener {
    private static double totalError=0;
    private static double totalSamples=0;
    private static int interationtimes=0;
    private static int predict_success=0;
    private static LossFunction lossfunction;

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

    public static void printMeanSquareError(){
        interationtimes++;
        double score=totalError / totalSamples;
        if(Double.isNaN(score)){
            System.out.println("Gradient Exploded Happened!");
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
        System.out.println("Test Total Mean Square Error is "+totalError/totalSamples);
    }

    public static void printPredict(){
        System.out.println("Predict Accuracy: "+predict_success/totalSamples);
    }

    public static double getMeanError(){
        return totalError/totalSamples;
    }

    public static void printpara(){
        System.out.println("totalerror:"+totalError+" totalsample:"+totalSamples);
    }
}

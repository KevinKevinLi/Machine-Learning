package Listeners;

public class Listener {
    private static double totalError=0;
    private static double totalSamples=0;
    private static int interationtimes=0;
    private static int predict_success=0;

    public Listener(){}

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
        System.out.println("Interation:"+interationtimes+ " Mean Square Error is "+totalError/totalSamples);
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

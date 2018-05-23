package Listeners;

public class Listener {
    private static double totalError=0;
    private static double totalSamples=0;
    private static int interationtimes=0;

    public Listener(){}

    public void addError(double error){
        totalError+=error;
    }

    public void addSampleNum(){
        totalSamples++;
    }

    public void init(){
        totalError=0;
        totalSamples=0;
    }

    public void printMeanSquareError(){
        interationtimes++;
        System.out.println("Interation:"+interationtimes+ " Average Mean Square Error is "+totalError/totalSamples);
    }
}

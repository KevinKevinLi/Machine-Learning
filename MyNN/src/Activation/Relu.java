package Activation;

public class Relu extends Neuron{
    public Relu(){}

    @Override
    public void execOutput(double input){
        current_input=input;
        current_output=Math.max(0.0,input);
    }

    @Override
    public double execDerivative(){
        if(current_output==0){
            return 0;
        }
        else {
            return 1;
        }
    }

    @Override
    public void printname(){
        System.out.println("Relu");
    }
}

package Activation;

public class Softmax extends Neuron {
    public Softmax(){}

    @Override
    public void execOutput(double input){
        current_input=input;
        current_output=Math.exp(input);
    }

    @Override
    public double execDerivative(){
        return current_output*(1.0-current_output);
    }

    @Override
    public void printname(){
        System.out.println("Softmax");
    }
}


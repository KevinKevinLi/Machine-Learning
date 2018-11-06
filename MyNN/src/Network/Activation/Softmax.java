package Network.Activation;

public class Softmax extends Neuron {
    double total_value=0;

    public Softmax(){}

    @Override
    public void execOutput(double input){
        current_input=input;
    }

    @Override
    public void execOutput(double input,double total){
        current_output=input/total;
        total_value=total;
    }

    @Override
    public double execDerivative(){
        return current_output*(total_value-Math.exp(current_input))/total_value;
        //return (Math.exp(current_input)*(total_value-current_input))/(total_value*total_value);
    }

    @Override
    public void printname(){
        System.out.println("Softmax");
    }

    @Override
    public String getname(){
        return "Softmax";
    }
}


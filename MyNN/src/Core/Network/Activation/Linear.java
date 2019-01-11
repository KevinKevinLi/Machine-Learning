package Core.Network.Activation;

public class Linear extends Neuron {
    public Linear(){}

    @Override
    public void execOutput(double input){
        current_input=input;
        current_output=input;
    }

    @Override
    public void execOutput(double input,double total){
    }

    @Override
    public double execDerivative(){
        return 1;
    }

    @Override
    public void printname(){
        System.out.println("Linear");
    }

    @Override
    public String getname(){
        return "Linear";
    }
}

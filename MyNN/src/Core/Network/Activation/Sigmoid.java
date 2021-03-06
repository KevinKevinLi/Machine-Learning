package Core.Network.Activation;

public class Sigmoid extends Neuron {
    public Sigmoid(){}

    @Override
    public void execOutput(double input){
        current_input=input;
        current_output=1.0/(1.0+Math.exp(-input));
        if(current_output==1){
            //manually add bias
            current_output=1-0.00001;
        }
    }

    @Override
    public void execOutput(double input,double total){
    }

    @Override
    public double execDerivative(){
        return current_output*(1.0-current_output);
    }

    @Override
    public void printname(){
        System.out.println("Sigmoid");
    }

    @Override
    public String getname(){
        return "Sigmoid";
    }
}

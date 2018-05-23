package Activation;

public abstract class Neuron {
    protected double current_input;
    protected double current_output;
    protected double current_error;
    protected double current_target;
    protected double current_residual=1.0;

    public Neuron(){};

    public abstract void execOutput(double input);

    public abstract double execDerivative();

    public double getoutput(){
        return current_output;
    }

    public void setError(double error){
        current_error=error;
    }

    public double getError(){
        return current_error;
    }

    public void setTarget(double target){
        current_target=target;
    }

    public double getTarget(){
        return current_target;
    }

    public void setResidual(double residual){
        current_residual=residual;
    }

    public double getResidual(){
        return current_residual;
    }

    public abstract void printname();
}

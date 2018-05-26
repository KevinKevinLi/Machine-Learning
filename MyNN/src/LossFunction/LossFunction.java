package LossFunction;

public enum LossFunction {
    SUBTRACTION,
    MSE;

    private LossFunction(){}

    public double exec(double targetvalue,double outvalue,int outputnum) {
        switch(this){
            case SUBTRACTION:
                return (targetvalue-outvalue);
            case MSE:
                return (targetvalue-outvalue)*(targetvalue-outvalue)/(double)outputnum;
            default:
                throw new UnsupportedOperationException("Unknown or not supported loss function: " + this);
        }
    }

    public double execDrivative(double targetvalue,double outvalue,int outputnum){
        switch(this){
            case SUBTRACTION:
                return 1.0;
            case MSE:
                return 2.0*(outvalue-targetvalue)/(double)outputnum;
            default:
                throw new UnsupportedOperationException("Unknown or not supported loss function: " + this);
        }
    }
}

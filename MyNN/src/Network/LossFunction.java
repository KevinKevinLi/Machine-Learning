package Network;

public enum LossFunction {
    SUBTRACTION,
    MSE;

    private LossFunction(){}

    public double exec(double targetvalue,double outvalue) {
        switch(this){
            case SUBTRACTION:
                return (targetvalue-outvalue);
            case MSE:
                return (targetvalue-outvalue)*(targetvalue-outvalue)/2;
            default:
                throw new UnsupportedOperationException("Unknown or not supported loss function: " + this);
        }
    }

    public double execDrivative(double targetvalue,double outvalue){
        switch(this){
            case SUBTRACTION:
                return 1.0;
            case MSE:
                return (outvalue-targetvalue);
            default:
                throw new UnsupportedOperationException("Unknown or not supported loss function: " + this);
        }
    }
}

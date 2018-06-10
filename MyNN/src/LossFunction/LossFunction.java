package LossFunction;

public enum LossFunction {
    Subtraction,
    CrossEntropy,
    NegativeLogLikeliHood,
    Mse;

    private LossFunction(){}

    public double exec(double targetvalue,double outvalue,int outputnum) {
        switch(this){
            case Subtraction:
                return (targetvalue-outvalue);
            case Mse:
                return (targetvalue-outvalue)*(targetvalue-outvalue)/(double)outputnum;
            case CrossEntropy:
                double a=targetvalue*Math.log(outvalue);
                double b=1.0-targetvalue;
                double b2=1.0-outvalue;
                double c=Math.log(b2);
                double c2=b*c;
                double d=-(a+c2)/(double)outputnum;
                return d;
            case NegativeLogLikeliHood:
                return -Math.log(1.0-Math.abs(targetvalue-outvalue))/(double)outputnum;
            default:
                throw new UnsupportedOperationException("Unknown or not supported loss function: " + this);
        }
    }

    public double execDrivative(double targetvalue,double outvalue,int outputnum){
        switch(this){
            case Subtraction:
                return 1.0;
            case Mse:
                return 2.0*(outvalue-targetvalue)/(double)outputnum;
            case CrossEntropy:
                return (-targetvalue+outvalue)/(outputnum*outvalue*(1.0-outvalue));
            case NegativeLogLikeliHood:
                if(targetvalue-outvalue>0) {
                    return -1.0/ (1.0-targetvalue+outvalue);
                }
                else{
                    return 1.0/ (1.0-outvalue+targetvalue);
                }
            default:
                throw new UnsupportedOperationException("Unknown or not supported loss function: " + this);
        }
    }
}

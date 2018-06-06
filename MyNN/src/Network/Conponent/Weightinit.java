package Network.Conponent;

public enum Weightinit {
    XAVIER,
    CONSTANT,
    UNIFORM;

    private Weightinit(){}

    public double init(int input_num,int output_num) {
        switch(this){
            case XAVIER:
                return (Math.sqrt(6)/Math.sqrt(input_num+output_num))*(Math.random()*2.0-1.0);
            case UNIFORM:
                return Math.random();//0->1 -1->1?
            case CONSTANT:
                return 1.0;
            default:
                throw new UnsupportedOperationException("Unknown or not supported weightinit function: " + this);
        }
    }
}

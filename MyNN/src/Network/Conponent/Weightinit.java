package Network.Conponent;

public enum Weightinit {
    XAVIER,
    TEST,
    UNIFORM;

    private Weightinit(){}

    public double init() {
        switch(this){
            case UNIFORM:
                return 2.0*Math.random()-1.0;//0->1 -1->1?
            case TEST:
                return 1.0;
            default:
                throw new UnsupportedOperationException("Unknown or not supported weightinit function: " + this);
        }
    }
}

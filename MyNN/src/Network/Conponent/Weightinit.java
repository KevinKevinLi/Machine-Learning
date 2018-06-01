package Network.Conponent;

public enum Weightinit {
    XAVIER,
    CONSTANT,
    UNIFORM;

    private Weightinit(){}

    public double init() {
        switch(this){
            case UNIFORM:
                return Math.random();//0->1 -1->1?
            case CONSTANT:
                return 1.0;
            default:
                throw new UnsupportedOperationException("Unknown or not supported weightinit function: " + this);
        }
    }
}

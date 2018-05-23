package Network;

public enum Weight {
    XAVIER,
    TEST,
    UNIFORM;

    private Weight(){}

    public double init() {
        switch(this){
            case UNIFORM:
                return Math.random();
            case TEST:
                return 1.0;
            default:
                throw new UnsupportedOperationException("Unknown or not supported weightinit function: " + this);
        }
    }
}

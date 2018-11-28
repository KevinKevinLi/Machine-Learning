package Normalization;

public abstract class Units {
    public Units(){};

    public abstract double[] exec(int col,double []record);

    public abstract double[] exec(int col,double []record,boolean useprevious);

    public abstract void print();
}

package Normalization;

public abstract class Units {
    public Units(){};

    public abstract double[] exec(int row,double []record);

    public abstract double[] exec(int row,double []record,boolean useprevious);

    public abstract void print();
}

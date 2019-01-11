package Core.Normalization;

public abstract class Units {
    public Units(){};

    public abstract double[] exec(int col,double []record);

    public abstract double[] exec(int col,double []record,boolean useprevious);

    public abstract String[] exec(int col,String []record);

    public abstract void setshift(int shift);

    public abstract void print();
}

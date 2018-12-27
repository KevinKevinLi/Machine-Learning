package Normalization;

public class CopyUnit extends Units {
    private int colnum;
    private int shift;

    CopyUnit(int colnum){
        this.colnum=colnum;
        this.shift=0;
    }

    @Override
    public double[] exec(int col,double []record) {
        return record;
    }

    @Override
    public double[] exec(int col,double []record,boolean useprevious) {

        return record;
    }

    @Override
    public String[] exec(int col,String []record) {
        return record;
    }

    @Override
    public void setshift(int shift){
        this.shift=shift;
    }

    @Override
    public void print(){

    }
}

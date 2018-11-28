package Normalization;

public class MinmaxUnit extends Units{
    private double max[];
    private double min[];
    private double dif[];
    private int colnum;

    MinmaxUnit(int colnum){
        max=new double[colnum];
        min=new double[colnum];
        dif=new double[colnum];
        for(int i=0;i<colnum;i++) {
            max[i] = Double.MIN_VALUE;
            min[i] = Double.MAX_VALUE;
            dif[i] = 0;
        }
        this.colnum=colnum;
    }

    @Override
    public double[] exec(int col,double []record){
        for(int i=0;i<record.length;i++){
            if(max[col]<record[i]){
                max[col]=record[i];
            }
            if(min[col]>record[i]){
                min[col]=record[i];
            }
        }
        dif[col]=max[col]-min[col];
        for(int i=0;i<record.length;i++){
            record[i]=(record[i]-min[col])/dif[col];
        }
        col++;
        return record;
    }

    @Override
    public double[] exec(int col,double []record,boolean useprevious) {
        if(useprevious==true) {
            for (int i = 0; i < record.length; i++) {
                record[i] = (record[i] - min[col]) / dif[col];
            }
        }
        else{
            return exec(col,record);
        }
        return record;
    }

    @Override
    public void print(){
        for(int i=0;i<colnum;i++){
            System.out.println("col"+i+": max:"+max[i]+" min:"+min[i]+" dif:"+dif[i]);
        }
    }
}

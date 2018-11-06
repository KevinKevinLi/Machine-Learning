package Normalization;

public class MinmaxUnit extends Units{
    private double max[];
    private double min[];
    private double dif[];
    private int rownum;

    MinmaxUnit(int rownum){
        max=new double[rownum];
        min=new double[rownum];
        dif=new double[rownum];
        for(int i=0;i<rownum;i++) {
            max[i] = Double.MIN_VALUE;
            min[i] = Double.MAX_VALUE;
            dif[i] = 0;
        }
        this.rownum=rownum;
    }

    @Override
    public double[] exec(int row,double []record){
        for(int i=0;i<record.length;i++){
            if(max[row]<record[i]){
                max[row]=record[i];
            }
            if(min[row]>record[i]){
                min[row]=record[i];
            }
        }
        dif[row]=max[row]-min[row];
        for(int i=0;i<record.length;i++){
            record[i]=(record[i]-min[row])/dif[row];
        }
        row++;
        return record;
    }

    @Override
    public double[] exec(int row,double []record,boolean useprevious) {
        for(int i=0;i<record.length;i++){
            record[i]=(record[i]-min[row])/dif[row];
        }
        return record;
    }

    @Override
    public void print(){
        for(int i=0;i<rownum;i++){
            System.out.println("Row"+i+": max:"+max[i]+" min:"+min[i]+" dif:"+dif[i]);
        }
    }
}

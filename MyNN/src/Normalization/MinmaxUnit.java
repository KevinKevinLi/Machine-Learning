package Normalization;

public class MinmaxUnit {
    private double max[];
    private double min[];
    private double dif[];
    private int count;
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
        count=0;
    }

    public double[] exec(double []record){
        for(int i=0;i<record.length;i++){
            if(max[count]<record[i]){
                max[count]=record[i];
            }
            if(min[count]>record[i]){
                min[count]=record[i];
            }
        }
        dif[count]=max[count]-min[count];
        for(int i=0;i<record.length;i++){
            record[i]=(record[i]-min[count])/dif[count];
        }
        count++;
        return record;
    }

    public void print(){
        for(int i=0;i<rownum;i++){
            System.out.println("Row"+i+": max-"+max+" min-"+min+" dif-"+dif);
        }
    }
}

package Normalization;

import FileManage.TextRecordReader;

import java.io.FileNotFoundException;
import java.io.IOException;

public enum
NormFrame {
    MinMax,
    Donothing,
    Subtraction,
    Addition;

    private String filepath,splits;
    private Units unit;
    private boolean useprevious=false;

    private NormFrame(){
    }

    private Units getUnit(){
        return unit;
    }

    public void setpath(String filepath,String splits,int rownum){
        this.filepath=filepath;
        this.splits=splits;
        switch(this){
            case MinMax:
                unit=new MinmaxUnit(rownum);
                break;
            case Donothing:
                break;
            default:
                throw new UnsupportedOperationException("Unknown or not supported noramlization: " + this);
        }
    }

    public void changepath(String filepath,String splits){
        this.filepath=filepath;
        this.splits=splits;
    }

    public double[] exec(int row){
        //read
        double[] record= null;
        try {
            TextRecordReader DataSet=new TextRecordReader(filepath,splits);
            record = DataSet.ReturnRecord(row);
        }catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }catch (Exception e) {
            e.printStackTrace();
        }
        switch(this){
            case MinMax:
                if(useprevious==true) {
                    //unit.print();
                    record= unit.exec(row,record,true);
                    //record = newunit[0].getUnit().exec(row,record,true);
                }
                else if(useprevious==false){
                    record= unit.exec(row,record);
                }
                else{
                    throw new UnsupportedOperationException("Too many args in exec! ");
                }
                break;
            case Donothing:
                break;
            default:
                throw new UnsupportedOperationException("Unknown or not supported noramlization: " + this);
        }
        return record;
    }

    //overload+1
    public double[] exec(int row,double []dataset){
        switch(this){
            case MinMax:
                dataset=unit.exec(row,dataset);
                break;
            default:
                throw new UnsupportedOperationException("Unknown or not supported noramlization: " + this);
        }
        return dataset;
    }

    //overload+2
    //Subtrahend=Subtrahend-minuend
    public double[] exec(int front,int later){
        double[] sub= null;
        double[] min=null;
        try {
            TextRecordReader DataSet=new TextRecordReader(filepath,splits);
            sub = DataSet.ReturnRecord(front);
            min = DataSet.ReturnRecord(later);
        }catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }catch (Exception e) {
            e.printStackTrace();
        }

        switch(this){
            case Subtraction:
                for(int i=0;i<sub.length;i++){
                    sub[i]-=min[i];
                }
                break;
            case Addition:
                for(int i=0;i<sub.length;i++){
                    sub[i]+=min[i];
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown or not supported noramlization: " + this);
        }
        return sub;
    }

    public void useprevious(){
        useprevious=true;
    }

    public void print(){
        unit.print();
    }
}

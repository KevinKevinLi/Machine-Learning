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

    public void setpath(String filepath,String splits,int colnum){
        this.filepath=filepath;
        this.splits=splits;
        switch(this){
            case MinMax:
                unit=new MinmaxUnit(colnum);
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

    public double[] exec(int col){
        //read
        double[] record= null;
        try {
            TextRecordReader DataSet=new TextRecordReader(filepath,splits);
            record = DataSet.ReturnRecord(col,false);
        }catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }catch (Exception e) {
            e.printStackTrace();
        }
        switch(this){
            case MinMax:
                    record= unit.exec(col,record,useprevious);
                break;
            case Donothing:
                break;
            default:
                throw new UnsupportedOperationException("Unknown or not supported noramlization: " + this);
        }
        return record;
    }

    //overload+1
    public double[] exec(int col,double []dataset){
        switch(this){
            case MinMax:
                dataset=unit.exec(col,dataset);
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
            sub = DataSet.ReturnRecord(front,false);
            min = DataSet.ReturnRecord(later,false);
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

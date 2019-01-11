package Core.Normalization;

import Core.FileManage.TextRecordReader;

import java.io.FileNotFoundException;
import java.io.IOException;

public enum
NormFrame {
    MinMax,
    Copy,
    Subtraction,
    Addition;

    private String filepath,splits;
    private Units unit;
    private boolean useprevious=false;
    private int shift=0;
    private boolean skiprow=false;

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
            case Copy:
                unit=new CopyUnit(colnum);
                break;
            default:
                throw new UnsupportedOperationException("Unknown or not supported noramlization: " + this);
        }
    }

    public void changepath(String filepath,String splits){
        this.filepath=filepath;
        this.splits=splits;
    }

    public void skiprow(boolean skip){
        skiprow=skip;
    }

    public double[] exec(int col){
        //read
        double[] record= null;
        try {
            TextRecordReader DataSet=new TextRecordReader(filepath,splits);
            //DataSet.skipcol(0);
            record = DataSet.ReturnRecord(col,skiprow);
        }catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }catch (Exception e) {
            e.printStackTrace();
        }
        switch(this){
            case MinMax:
                if(shift!=0){
                    unit.setshift(shift);
                }
                record= unit.exec(col,record,useprevious);
                break;
            case Copy:
                record= unit.exec(col,record);
                break;
            default:
                throw new UnsupportedOperationException("Unknown or not supported noramlization: " + this);
        }
        return record;
    }

    //overload+0
    //copy string
    public String[] exec(int col, String label_name){
        //read
        String[] record= null;
        try {
            TextRecordReader DataSet=new TextRecordReader(filepath,splits);
            record = DataSet.ReturnStr(col,false);
        }catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }catch (Exception e) {
            e.printStackTrace();
        }
        switch(this){
            case MinMax:
                throw new UnsupportedOperationException("bad entry of normalization: " + this);
            case Copy:
                record= unit.exec(col,record);
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

    public void useprevious(int shift){
        useprevious=true;
        this.shift=shift;
    }

    public void print(){
        unit.print();
    }
}

package Normalization;

import FileManage.TextRecordReader;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.*;

public enum NormApproach{
    MinMax,
    Donothing,
    Subtraction,
    Addition;

    private String filepath,splits;
    private int rownum;

    private NormApproach(){}

    public void setpath(String filepath,String splits,int rownum){
        this.filepath=filepath;
        this.splits=splits;
        this.rownum=rownum;
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
                MinmaxUnit minmax=new MinmaxUnit(rownum);
                record= minmax.exec(record);
                break;
            case Donothing:
                break;
            default:
                throw new UnsupportedOperationException("Unknown or not supported noramlization: " + this);
        }
        return record;
    }

    //overload+1
    public double[] exec(double []dataset){
        switch(this){
            case MinMax:
                MinmaxUnit minmax=new MinmaxUnit(rownum);
                dataset=minmax.exec(dataset);
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
}

package Normalization;

import FileManage.TextRecordReader;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.*;

public enum NormApproach{
    MinMax,
    Donothing;

    private String filepath,splits;

    private NormApproach(){}

    public void setpath(String filepath,String splits){
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
                double max=Double.MIN_VALUE;
                double min=Double.MAX_VALUE;
                for(int i=0;i<record.length;i++){
                    if(max<record[i]){
                        max=record[i];
                    }
                    if(min>record[i]){
                        min=record[i];
                    }
                }
                double dif=max-min;
                for(int i=0;i<record.length;i++){
                    record[i]=(record[i]-min)/dif;
                }
                break;
            case Donothing:
                break;
            default:
                throw new UnsupportedOperationException("Unknown or not supported noramlization: " + this);
        }
        return record;
    }
}

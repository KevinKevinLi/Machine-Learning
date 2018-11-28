package Factorize;

import FileManage.TextRecordReader;

import java.io.*;

public class FactorConfigration {

    private int fin_colnum;
    private String pathin;
    private String splits;
    private int[] colpara;
    private double [][]dataset;
    private int offset;
    private int start;

    public FactorConfigration(String pathin,String splits, int fin_colnum){
        this.offset=0;
        this.pathin=pathin;
        this.splits=splits;
        this.fin_colnum=fin_colnum;
        colpara=new int [fin_colnum];
    }

    public FactorConfigration skipfirstrow(){
        start++;
        return this;
    }

    public FactorConfigration setcol(int newline, int oldline) {
        colpara[newline]=oldline;
        return this;
    }

    public FactorConfigration process(int offset, String outpath){
        //write
        double[] record= null;
        TextRecordReader DataSet=new TextRecordReader(pathin,splits);
        dataset=new int [DataSet.LineNumber()][];
        for(int i=0;i<fin_colnum;i++){
            if(colpara[i]<0){

            }
        }
        record = DataSet.ReturnRecord();

        System.out.println("Process success!");
        return this;
    }
}

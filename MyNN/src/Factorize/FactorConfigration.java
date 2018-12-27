package Factorize;

import FileManage.TextRecordReader;

import java.io.*;

public class FactorConfigration {

    private int fin_colnum;
    private String pathin;
    private String splits;
    private int[][] coloffset;
    private double [][]dataset;
    private boolean skiprow=false;
    private String []labels;

    //date labels
    private boolean ifdate=false;
    private int datepos=0;
    private String []date_labels;

    public FactorConfigration(String pathin,String splits, int fin_colnum){
        this.pathin=pathin;
        this.splits=splits;
        this.fin_colnum=fin_colnum;

        int rownum=0;
        TextRecordReader DataSet=new TextRecordReader(pathin,splits);
        try {
            rownum=DataSet.LineNumber();
        }catch(Exception e){
            e.printStackTrace();
        }
        //start+end+whichcol
        coloffset=new int [3][fin_colnum];
        //initialize
        for(int i=0;i<fin_colnum;i++){
            coloffset[0][i]=0;
            coloffset[1][i]=rownum-1;
            coloffset[2][i]=0;
        }

        labels = new String[rownum];
    }

    public FactorConfigration skipfirstrow(){
        this.skiprow=true;
        return this;
    }

    public FactorConfigration setcol(int newcol, int oldcol) {
        //only support last and next row now(offset one row)
        if(oldcol<0){
            coloffset[0][newcol]+=1;
            coloffset[2][newcol]=-oldcol;
        }
        else if(oldcol>=fin_colnum){
            coloffset[1][newcol]-=1;
            coloffset[2][newcol]=oldcol-fin_colnum;
        }
        else{
            coloffset[2][newcol]=oldcol;
        }
        return this;
    }

    public FactorConfigration setdate(int col) {
        datepos=col;
        ifdate=true;
        return this;
    }

    public FactorConfigration process(String outpath){
        //find the most offset
        int start_flag=Integer.MIN_VALUE;
        int end_flag=Integer.MAX_VALUE;
        for(int i=0;i<fin_colnum;i++){
            if(coloffset[0][i]>start_flag){
                start_flag=coloffset[0][i];
            }
            if(coloffset[1][i]<end_flag){
                end_flag=coloffset[1][i];
            }
        }

        //read
        double[] record= null;
        int flag=1;
        if(skiprow==true){
            flag=0;
        }
        dataset = new double[end_flag-start_flag+flag][fin_colnum];
        try {
            TextRecordReader DataSet = new TextRecordReader(pathin, splits);
            int start=0;
            int end=0;
            for (int i = 0; i < coloffset[0].length; i++) {
                record = DataSet.ReturnRecord(coloffset[2][i],skiprow);

                // System.out.println(record.length);
                if(coloffset[0][i]==start_flag){
                    start=start_flag-1;
                    end=end_flag-1;
                }
                else if(coloffset[1][i]==end_flag){
                    start=start_flag+1;
                    end=end_flag+1;
                }
                else{
                    start=start_flag;
                    end=end_flag;
                }
                for(int j=start,k=0;j<end+flag;j++,k++){
                    dataset[k][i]=record[j];
                }
            }
            if(ifdate==true){
                int offset=0;
                date_labels=new String[end_flag-start_flag+flag];
                if(datepos>=0&&datepos<=fin_colnum-1){
                    start=start_flag;
                    end=end_flag;
                }
                else{
                    offset = datepos/(fin_colnum);
                    start=start_flag+offset;
                    end=end_flag+offset;
                }
                String temp[] = DataSet.ReturnStr(datepos-offset*(fin_colnum),skiprow);
                for(int i=start,j=0;i<end;i++,j++){
                    date_labels[j]=temp[i];
                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }

        //write
        File file=new File(outpath);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
            for (int i = 0; i < dataset.length; i++) {
                StringBuilder str=new StringBuilder();
                if(ifdate==true){
                    str.append(date_labels[i]+",");
                }
                for(int j=0;j<dataset[0].length;j++) {
                    str.append(dataset[i][j]+",");
                }
                bw.write(str.toString());
                bw.newLine();
            }
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Process success!");
        return this;
    }
}

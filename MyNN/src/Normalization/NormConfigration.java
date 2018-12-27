package Normalization;

import java.io.*;

public class NormConfigration {
    private double [][]dataset;
    private String [][]labels;
    private String curpath=null;
    private int colnum;
    private int labelnum;

    public NormConfigration(int colnum){
        dataset=new double [colnum][];
        labels=new String[0][];
        this.colnum=colnum;
        this.labelnum=0;
    }

    public NormConfigration(int colnum, int labelnum){
        dataset=new double [colnum-labelnum][];
        labels=new String [labelnum][];
        //total colnum
        this.colnum=colnum;
        this.labelnum=labelnum;
    }

    public NormConfigration setcol(int col,double [] set){
        dataset[col-labelnum]=set;
        return this;
    }

    public NormConfigration setcol(int col, String [] set){
        if(col+1>labels.length){
            System.out.println("only support labels in first rows!");
        }
        else {
            labels[col] = set;
        }
        return this;
    }

    public NormConfigration process(String outpath){
        curpath=outpath;
        //write
        File file=new File(outpath);
            try {
                FileOutputStream fos = new FileOutputStream(file);
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
                for (int i = 0; i < dataset[0].length; i++) {
                    StringBuilder str=new StringBuilder();
                    int strflag=labelnum;
                    for(int j=0;j<colnum;j++) {
                        if(strflag==0) {
                            str.append(dataset[j-labelnum][i] + ",");
                        }
                        else{
                            str.append(labels[j][i] + ",");
                            strflag--;
                        }
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

    public NormConfigration separate(int start,int end,String outpath){
        File file=new File(outpath);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));

            FileInputStream inputStream = new FileInputStream(curpath);
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
            String str;
            int i=0;
            while((str = br.readLine()) != null) {
                if(i>=start&&i<=end) {
                    bw.write(str);
                    bw.newLine();
                }
                i++;
            }
            bw.close();
            br.close();
        }catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Successful separate file to "+outpath);
        return this;
    }

    public NormConfigration saveconfig(){
        return this;
    }
}

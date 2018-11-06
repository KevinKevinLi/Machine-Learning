package Normalization;

import java.io.*;

public class NormConfigration {
    private double [][]dataset;
    private String curpath=null;
    private int rownum;

    public NormConfigration(int rownum){
        dataset=new double [rownum][];
        this.rownum=rownum;
    }

    public NormConfigration setrow(int row,double [] set){
        dataset[row]=set;
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
                    for(int j=0;j<dataset.length;j++) {
                        str.append(dataset[j][i]+",");
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

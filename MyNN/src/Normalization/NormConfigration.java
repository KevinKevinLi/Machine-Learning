package Normalization;

import java.io.*;

public class NormConfigration {
    private double [][]dataset=null;

    public NormConfigration(int rownum){
        dataset=new double [rownum][];
    }

    public NormConfigration setrow(int row,double [] set){
        dataset[row]=set;
        return this;
    }

    public NormConfigration process(String outpath){
        //write
        File file=new File(outpath);
            try {
                FileOutputStream fos = new FileOutputStream(file);
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
                for (int i = 0; i < dataset[0].length; i++) {
                    StringBuilder str=new StringBuilder();
                    for(int j=0;j<dataset.length;j++) {
                        str.append(dataset[j][i]);
                    }
                    bw.write(str.toString());
                    bw.newLine();
                }
                bw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }




        return this;
    }
}

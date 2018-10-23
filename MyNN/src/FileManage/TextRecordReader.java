package FileManage;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class TextRecordReader {
    private String Delimiter = "";
    private String filepath="";
    private int InputMaxNum=999;
    private int record_length=0;
    private ArrayList<Double> numList = new <Double>ArrayList();

    public TextRecordReader(String filename, String splitsymbol){
        filepath=filename;
        Delimiter=splitsymbol;
    }

    public int LineNumber() throws Exception{
        FileInputStream inputStream = new FileInputStream(filepath);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

        String str = null;
        int num=0;
        while((str = bufferedReader.readLine()) != null) {
            num++;
        }
        return num;
    }

    public double[][] ReturnRecord(int input_num,int output_num) throws Exception{
        FileInputStream inputStream = new FileInputStream(filepath);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

        String str = null;
        double result[][]=new double [LineNumber()][input_num+output_num];
        int i=0;

        while((str = bufferedReader.readLine()) != null) {
            String[] strarray=str.split(Delimiter);
            for(int j=0;j<strarray.length;j++){
                result[i][j]=Double.valueOf(strarray[j]);
                //System.out.println(result[i][j]);
            }
            record_length=strarray.length;
            i++;
        }

        //check if normal situation, record fits input and output
        if(record_length!=input_num+output_num){
            double flag=0;
            //the number of different values
            numList.add(result[0][input_num]);

            for(int j=0;j<result.length;j++){
                if(!numList.contains(result[j][input_num])){
                    numList.add(result[j][input_num]);
                }
            }

            for(int j=0;j<result.length;j++){
                flag=result[j][input_num];
                for(int k=0;k<output_num;k++){
                    if(flag==numList.get(k)){
                        result[j][input_num+k]=1.0;
                    }
                    else{
                        result[j][input_num+k]=0.0;
                    }
                }
            }
        }

//        for(int m=0;m<result.length;m++){
//            for(int n=0;n<input_num+output_num;n++){
//                System.out.println(result[m][n]);
//            }
//        }
        numList.clear();
        return result;
    }
}

package RecordReader;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;

public class TextRecordReader {
    private String Delimiter = "";
    private String filepath="";
    private int InputMaxNum=999;

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

    public double[][] ReturnRecord(int RowNumber) throws Exception{
        FileInputStream inputStream = new FileInputStream(filepath);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

        String str = null;
        double result[][]=new double [LineNumber()][RowNumber];
        int i=0;
        while((str = bufferedReader.readLine()) != null) {
            String[] strarray=str.split(Delimiter);
            for(int j=0;j<strarray.length;j++){
                result[i][j]=Double.valueOf(strarray[j]);
                //System.out.println(result[i][j]);
            }
            i++;
        }
        return result;
    }

}

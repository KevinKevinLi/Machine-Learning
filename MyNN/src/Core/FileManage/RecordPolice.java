package Core.FileManage;
import java.util.*;

public class RecordPolice {
    private ArrayList<Double> numList = new <Double>ArrayList();

    public RecordPolice(){}

    public double [][]HandleOutput(double [][]inputset,int input,int output){
        double newset[][]=new double [inputset.length][input+output];
        for(int i=0;i<inputset.length;i++){
            for(int j=0;j<input;j++){
                newset[i][j]=inputset[i][j];
            }
            for(int j=0;j<output;j++){
                //warning
                if(numList.get(j)==inputset[i][input]){
                    newset[i][input+j]=1;
                }
                else {
                    newset[i][input + j] = 0;
                }
            }

        }
        return newset;
    }
}

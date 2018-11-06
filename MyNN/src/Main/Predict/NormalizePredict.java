package Main.Predict;

import Normalization.NormConfigration;
import Normalization.NormFrame;

public class NormalizePredict {
    public static void main(String[] args) throws Exception {
        int rownum = 8;
        NormFrame minmax = NormFrame.MinMax;
        String prepathin = "data/Stock/1029/1102.csv";
        String pathin="data/Stock/1029/until1103.csv";
        minmax.setpath(prepathin, ",", rownum);
        //record previous minmax parameter
        for(int i=0;i<rownum;i++){
            minmax.exec(i);
        }
        minmax.changepath(pathin,",");
        //use previous nomalization parameter, set that flag=true;
        minmax.useprevious();
        NormConfigration confi = new NormConfigration(rownum)
                .setrow(0, minmax.exec(0))
                .setrow(1, minmax.exec(1))
                .setrow(2, minmax.exec(2))
                .setrow(3, minmax.exec(3))
                .setrow(4, minmax.exec(4))
                .setrow(5, minmax.exec(5))
                .setrow(6, minmax.exec(6))
                .setrow(7, minmax.exec(7))
                .process("./data/Stock/1029/until1103_origin.csv");
        minmax.print();
    }
}

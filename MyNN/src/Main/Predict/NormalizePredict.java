package Main.Predict;

import Normalization.NormConfigration;
import Normalization.NormFrame;

public class NormalizePredict {
    public static void main(String[] args) throws Exception {
        int colnum = 8;
        NormFrame minmax = NormFrame.MinMax;
        String prepathin = "data/Stock/1029/1102.csv";
        String pathin= "data/Stock/1029/1127_1217_fac.csv";
        minmax.setpath(prepathin, ",", colnum);
        //record previous minmax parameter
        for(int i=0;i<colnum;i++){
            minmax.exec(i);
        }
        minmax.changepath(pathin,",");
        //use previous nomalization parameter, set that flag=true;
        minmax.useprevious();
        NormConfigration confi = new NormConfigration(colnum)
                .setcol(0, minmax.exec(0))
                .setcol(1, minmax.exec(1))
                .setcol(2, minmax.exec(2))
                .setcol(3, minmax.exec(3))
                .setcol(4, minmax.exec(4))
                .setcol(5, minmax.exec(5))
                .setcol(6, minmax.exec(6))
                .setcol(7, minmax.exec(7))
                .process("data/Stock/1029/1127_1217_fac_nor.csv");
        minmax.print();
    }
}

package Main.Predict;

import Normalization.NormConfigration;
import Normalization.NormFrame;

public class NormalizePredict {
    public static void main(String[] args) throws Exception {
        //colnum inclde date
        int colnum = 8;
        NormFrame minmax = NormFrame.MinMax;
        NormFrame copy = NormFrame.Copy;
        String prepathin = "data/Stock/1029/1102.csv";
        String pathin= "data/Stock/Formal/1005_1219_fac.csv";
        //because previous data don't have date info so, col = col -1
        minmax.setpath(prepathin, ",", colnum);
        //record previous minmax parameter
        for(int i=0;i<colnum;i++){
            minmax.exec(i);
        }
        minmax.changepath(pathin,",");
        //use previous nomalization parameter, set that flag=true;
        //because previous format are different, so shift 1 to right
        //minmax.useprevious(1);
        copy.setpath(pathin,",", colnum);

        NormConfigration confi = new NormConfigration(colnum,0)// total colnum
                //.setcol(0,copy.exec(0,"date"))
                .setcol(0, minmax.exec(0))
                .setcol(1, minmax.exec(1))
                .setcol(2, minmax.exec(2))
                .setcol(3, minmax.exec(3))
                .setcol(4, minmax.exec(4))
                .setcol(5, minmax.exec(5))
                .setcol(6, minmax.exec(6))
                .setcol(7, minmax.exec(7))
                .process("data/Stock/Formal/1005_1219_fac_nor.csv");
        minmax.print();
    }
}

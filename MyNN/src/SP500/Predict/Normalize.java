package SP500.Predict;

import Core.Normalization.NormConfigration;
import Core.Normalization.NormFrame;

public class Normalize {
    public static void main(String[] args) throws Exception {
        //colnum include date
        int colnum = 9;
        NormFrame minmax = NormFrame.MinMax;
        NormFrame copy = NormFrame.Copy;
        String prepathin = "data/Stock/Test/1102.csv";
        String pathin= "data/Stock/SP500/1005_now_fac.csv";
        //because previous data don't have date info so, col = col -1
        minmax.setpath(prepathin, ",", colnum-1);
        //record previous minmax parameter
        boolean skiprow=false;
        for(int i=0;i<colnum-1;i++){
            minmax.exec(i);
        }
        minmax.changepath(pathin,",");
        //use previous nomalization parameter, set that flag=true;
        //because previous format are different, so shift 1 to right
        minmax.useprevious(1);
        copy.setpath(pathin,",", colnum);

        NormConfigration confi = new NormConfigration(colnum,1)// total colnum
                .setcol(0,copy.exec(0,"date"))
                .setcol(1, minmax.exec(1))
                .setcol(2, minmax.exec(2))
                .setcol(3, minmax.exec(3))
                .setcol(4, minmax.exec(4))
                .setcol(5, minmax.exec(5))
                .setcol(6, minmax.exec(6))
                .setcol(7, minmax.exec(7))
                .setcol(8, minmax.exec(8))
                .process("data/Stock/SP500/1005_now_fac_nor.csv");
        minmax.print();
    }
}

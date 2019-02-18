package Main.Predict;

import Core.Normalization.NormConfigration;
import Core.Normalization.NormFrame;

public class Normalize {
    public static void main(String[] args) throws Exception {
        String prepathin = "data/Stockv2/";
        String pathin= "data/Stockv2/";
        switch(args[0]) {
            case "1":
                //Sp500
                prepathin+="Sp500/980101_190118_fac.csv";
                pathin+="Sp500/190118_now_fac";
                break;
            case "2":
                //Russell
                prepathin+="Russell2000/980101_190118_fac.csv";
                pathin+="Russell2000/190118_now_fac";
                break;
            case "3":
                //Dow30
                prepathin+="Dow30/980101_190118_fac.csv";
                pathin+="Dow30/190118_now_fac";
                break;
            case "4":
                //Dow30
                prepathin+="Nasdaq/980101_190118_fac.csv";
                pathin+="Nasdaq/190118_now_fac";
                break;
        }
        //colnum exclude date
        int colnum = 8;
        NormFrame minmax = NormFrame.MinMax;
        NormFrame copy = NormFrame.Copy;
        //because previous data don't have date info so, col = col -1
        minmax.setpath(prepathin, ",", colnum+1);
        //record previous minmax parameter

        for(int i=1;i<colnum+1;i++){
            minmax.exec(i);
        }
        minmax.changepath(pathin+".csv",",");
        //use previous nomalization parameter, set that flag=true;
        //because previous format are different, so shift 1 to right
        minmax.useprevious(0);
        copy.setpath(pathin+".csv",",", colnum+1);

        NormConfigration confi = new NormConfigration(colnum+1,1)// total colnum
                .setcol(0,copy.exec(0,"date"))
                .setcol(1, minmax.exec(1))
                .setcol(2, minmax.exec(2))
                .setcol(3, minmax.exec(3))
                .setcol(4, minmax.exec(4))
                .setcol(5, minmax.exec(5))
                .setcol(6, minmax.exec(6))
                .setcol(7, minmax.exec(7))
                .setcol(8, minmax.exec(8))
                .process(pathin+"_nor.csv");
        minmax.print();
    }
}

package Main.Train;

import Core.Normalization.NormConfigration;
import Core.Normalization.NormFrame;

public class Normalize {
    public static void main(String[] args) {
        //colnum include date
        int colnum = 9;

        String pathin= "data/Stockv2/";
        switch(args[0]) {
            case "1":
                //Sp500
                pathin+="Sp500/980101_190118";
                break;
            case "2":
                //Russell
                pathin+="Russell2000/980101_190118";
                break;
            case "3":
                //Dow30
                pathin+="Dow30/980101_190118";
                break;
            case "4":

                pathin+="Nasdaq/980101_190118";
                break;
        }

        NormFrame minmax = NormFrame.MinMax;
        NormFrame copy = NormFrame.Copy;
        minmax.setpath(pathin+"_fac.csv", ",", colnum);
        copy.setpath(pathin+"_fac.csv",",", colnum);

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
                .process(pathin+"_fac_nor.csv")
                .separate(0,4779, pathin+"_train.csv")
                .separate(4780,5293, pathin+"_test.csv");
        minmax.print();
    }
}

package Main.Train;

import Core.Factorize.FactorConfigration;

public class Factorize {
    public static void main(String[] args) {
        //colnum except date
        int colnum=8;
        String pathin= "data/Stockv2/";
        switch(args[0]) {
            case "1":
                pathin+="Sp500/980101_190118";
                break;
            case "2":
                pathin+="Russell2000/980101_190118";
                break;
            case "3":
                pathin+="Dow30/980101_190118";
                break;
            case "4":
                pathin+="Nasdaq/980101_190118";
                break;
        }
        FactorConfigration confi=new FactorConfigration(pathin+".csv",",",colnum)
                .skipfirstrow()
                //date labels
                .setdate(8)
                //open
                .setcol(0,1)
                //high
                .setcol(1,2)
                //low
                .setcol(2,3)
                //volumn
                .setcol(3,6)
                //last close
                .setcol(4,-5)
                //next open
                .setcol(5,1+colnum)
                //cur close
                .setcol(6,5)
                //next close
                .setcol(7,5+colnum)
                .process(pathin+"_fac.csv");

    }
}

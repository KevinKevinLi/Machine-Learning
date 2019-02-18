package Main.Predict;

import Core.Factorize.FactorConfigration;

public class Factorize {
    public static void main(String[] args) {
        String pathin= "data/Stockv2/";
        switch(args[0]) {
            case "1":
                pathin+="Sp500/190118_now";
                break;
            case "2":
                pathin+="Russell2000/190118_now";
                break;
            case "3":
                pathin+="Dow30/190118_now";
                break;
            case "4":
                pathin+="Nasdaq/190118_now";
                break;
        }
        //colnum except date
        int colnum=8;

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

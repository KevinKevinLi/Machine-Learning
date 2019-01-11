package Main.Train;

import Core.Factorize.FactorConfigration;
import java.util.Scanner;

public class Factorize {
    public static void main(String[] args) {
        //colnum except date
        int colnum=8;
        String pathin="data/Stock/";
        switch(args[0]) {
            case "1":
                pathin+="SP500/981013_181008";
                break;
            case "2":
                pathin+="Russell2000/981013_181008";
                break;
            case "3":
                pathin+="Dow30/981013_181008";
                break;
            case "4":
                pathin+="Nasdaq/981013_181008";
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

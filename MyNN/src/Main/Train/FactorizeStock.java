package Main.Train;

import Factorize.FactorConfigration;

public class FactorizeStock {
    public static void main(String[] args) {
        int colnum=8;
        String pathin= "data/Stock/Formal/1005_1219.csv";
        FactorConfigration confi=new FactorConfigration(pathin,",",colnum)
            .skipfirstrow()
                //date
                //.setlabel(0,0)
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
            .process("data/Stock/Formal/1005_1219_fac.csv");
    }
}

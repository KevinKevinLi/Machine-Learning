package Main.Train;

import Factorize.FactorConfigration;

public class FactorizeStock {
    public static void main(String[] args) {
        int colnum=8;
        String pathin="data/Stock/1029/1029_1127.csv";
        FactorConfigration confi=new FactorConfigration(pathin,",",colnum)
            .skipfirstrow()
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
            //cur close
            .setcol(5,5)
            //next open
            .setcol(6,1+colnum)
            //next close
            .setcol(7,5+colnum)
            .process("data/Stock/1029/1029_1127_fac.csv");
    }
}

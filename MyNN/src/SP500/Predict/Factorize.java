package SP500.Predict;

import Core.Factorize.FactorConfigration;

public class Factorize {
    public static void main(String[] args) {
        //colnum except date
        int colnum=8;
        String pathin= "data/Stock/SP500/1005_now.csv";
        FactorConfigration confi=new FactorConfigration(pathin,",",colnum)
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
            .process("data/Stock/SP500/1005_now_fac.csv");
    }
}

package Main.Train;

import Normalization.NormFrame;
import Normalization.NormConfigration;

public class Normalize {
    public static void main(String[] args) {
        NormFrame minmax= NormFrame.MinMax;
        //NormFrame sub=NormFrame.Subtraction;
        String pathin= "data/Stock/1029/1102.csv";
        //sub.setpath(pathin,",");
        int colnum=8;
        minmax.setpath(pathin,",",colnum);
        NormConfigration confi=new NormConfigration(colnum)
                .setcol(0,minmax.exec(0))
                .setcol(1,minmax.exec(1))
                .setcol(2,minmax.exec(2))
                .setcol(3,minmax.exec(3))
                .setcol(4,minmax.exec(4))
                .setcol(5,minmax.exec(5))
                .setcol(6,minmax.exec(6))
                .setcol(7,minmax.exec(7))
                .process("./data/Stock/1029/1102_origin.csv")
                .separate(0,3997,"./data/Stock/1029/4997.csv")
                .separate(4998,5027,"./data/Stock/1029/4995027.csv");
        minmax.print();
    }
}

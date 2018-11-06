package Main.Train;

import Normalization.NormFrame;
import Normalization.NormConfigration;

public class Normalize {
    public static void main(String[] args) {
        NormFrame minmax= NormFrame.MinMax;
        //NormFrame sub=NormFrame.Subtraction;
        String pathin= "data/Stock/1029/1102.csv";
        //sub.setpath(pathin,",");
        int rownum=8;
        minmax.setpath(pathin,",",rownum);
        NormConfigration confi=new NormConfigration(rownum)
                .setrow(0,minmax.exec(0))
                .setrow(1,minmax.exec(1))
                .setrow(2,minmax.exec(2))
                .setrow(3,minmax.exec(3))
                .setrow(4,minmax.exec(4))
                .setrow(5,minmax.exec(5))
                .setrow(6,minmax.exec(6))
                .setrow(7,minmax.exec(7))
                .process("./data/Stock/1029/1102_origin.csv")
                .separate(0,3997,"./data/Stock/1029/1105tra.csv")
                .separate(3998,5027,"./data/Stock/1029/1105test.csv");
        minmax.print();
    }
}

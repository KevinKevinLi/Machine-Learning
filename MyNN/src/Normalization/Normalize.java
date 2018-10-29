package Normalization;

public class Normalize {
    public static void main(String[] args) {
        NormApproach minmax=NormApproach.MinMax;
        minmax.setpath("./data/Stock/1029/origintest.csv",",");
        NormConfigration confi=new NormConfigration(5)
                .setrow(0,minmax.exec(0))
                .setrow(1,minmax.exec(1))
                .setrow(2,minmax.exec(2))
                .setrow(3,minmax.exec(3))
                .setrow(4,minmax.exec(4))
                .process("./data/Stock/1029/test.csv");
    }
}

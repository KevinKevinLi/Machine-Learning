package Main.Train;

import Network.ClassificationNetwork;
import FileManage.*;

public class RecursiveTrain {
    public static void main(String[] args) throws Exception {
        String filepath="./data/Stock/1029/1102_train.csv";
        TextRecordReader TrainSet=new TextRecordReader(filepath,",");
        filepath="./data/Stock/1029/1102_test.csv";
        TextRecordReader TestSet=new TextRecordReader(filepath,",");

        int input_num=7;
        int output_num=1;
        ClassificationNetwork OldNetwork=new ClassificationNetwork();
        ModelRecord model=new ModelRecord();
        model.buildfrom("data/Network/stock_4_650.nn",OldNetwork);

        OldNetwork.train(TrainSet.ReturnRecord(input_num,output_num),20000);
        OldNetwork.test(TestSet.ReturnRecord(input_num,output_num));
        OldNetwork.predict(TestSet.ReturnRecord(input_num,output_num),"./data/ChartOutput/11061.png",21,50);
        OldNetwork.saveas("data/Network/stock11061.nn");
    }
}

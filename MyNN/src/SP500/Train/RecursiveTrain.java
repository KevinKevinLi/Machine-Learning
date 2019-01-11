package SP500.Train;

import Core.Network.ClassificationNetwork;
import Core.FileManage.*;

public class RecursiveTrain {
    public static void main(String[] args) throws Exception {
        String filepath= "data/Stock/Test/1102_train.csv";
        TextRecordReader TrainSet=new TextRecordReader(filepath,",");
        filepath= "data/Stock/Test/1102_test.csv";
        TextRecordReader TestSet=new TextRecordReader(filepath,",");

        int input_num=7;
        int output_num=1;
        ClassificationNetwork OldNetwork=new ClassificationNetwork();
        ModelRecord model=new ModelRecord();
        model.buildfrom("data/Network/stock1102_train_200k.nn",OldNetwork);

        OldNetwork.train(TrainSet.ReturnRecord(input_num,output_num),200000);
        //OldNetwork.test(TestSet.ReturnRecord(input_num,output_num));
        //OldNetwork.chart(ChartType.Zero_base,"./data/ChartOutput/temp.png");
        //OldNetwork.predict(TestSet.ReturnRecord(input_num,output_num),21,50);
        OldNetwork.saveas("data/Network/stock1102_train_400k.nn");
    }
}

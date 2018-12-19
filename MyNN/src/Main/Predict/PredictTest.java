package Main.Predict;

import FileManage.ModelRecord;
import FileManage.TextRecordReader;
import Network.ClassificationNetwork;

public class PredictTest {
    public static void main(String[] args) throws Exception {
        int input_num=7;
        int output_num=1;
        String filepath= "data/Stock/Formal/1005_1219_fac_nor.csv";
        //String filepath= "data/Stock/1029/1102_test.csv";
        TextRecordReader TestSet=new TextRecordReader(filepath,",");

        ClassificationNetwork OldNetwork=new ClassificationNetwork();
        ModelRecord model=new ModelRecord();
        //model.buildfrom("./data/Network/stock1102_train_100k.nn",OldNetwork);
        //model.buildfrom("./data/Network/stock150k_3_654.nn",OldNetwork);
        model.buildfrom("./data/Network/stock_4_650.nn",OldNetwork);
        //OldNetwork.test(TestSet.ReturnRecord(5,1));

        OldNetwork.predict(TestSet.ReturnRecord(input_num,output_num),"./data/ChartOutput/temp.png",4,50);
        //OldNetwork.saveas("data/Network/stock150k_3_654.nn");
    }
}

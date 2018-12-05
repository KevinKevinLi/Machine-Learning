package Main.Predict;

import FileManage.ModelRecord;
import FileManage.TextRecordReader;
import Network.ClassificationNetwork;

public class Predict {
    public static void main(String[] args) throws Exception {
        int input_num=7;
        int output_num=1;
        String filepath= "data/Stock/1029/0806_1128_fac_nor.csv";
        TextRecordReader TestSet=new TextRecordReader(filepath,",");

        ClassificationNetwork OldNetwork=new ClassificationNetwork();
        ModelRecord model=new ModelRecord();
        model.buildfrom("./data/Network/stock1104_650.nn",OldNetwork);
        //OldNetwork.test(TestSet.ReturnRecord(5,1));

        OldNetwork.predict(TestSet.ReturnRecord(input_num,output_num),"./data/ChartOutput/11301.png",4,50);
        // OldNetwork.saveas("data/Network/stock11061_temp.nn");
    }
}

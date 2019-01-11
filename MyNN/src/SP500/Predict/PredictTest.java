package SP500.Predict;

import Core.FileManage.ModelRecord;
import Core.FileManage.TextRecordReader;
import Core.Listeners.ChartType;
import Core.Network.ClassificationNetwork;

public class PredictTest {
    public static void main(String[] args) throws Exception {

        Factorize.main(args);
        Normalize.main(args);

        int input_num=7;
        int output_num=1;
        String filepath= "data/Stock/SP500/1005_now_fac_nor.csv";
        //String filepath= "data/Stock/Test/1102_test.csv";
        TextRecordReader TestSet=new TextRecordReader(filepath,",");
        //only support skip first several rows
        TestSet.skipcol(0);

        ClassificationNetwork OldNetwork=new ClassificationNetwork();
        ModelRecord model=new ModelRecord();
        //model.buildfrom("./data/Core.Network/stock1102_train_150k.nn",OldNetwork);
        //model.buildfrom("./data/Core.Network/stock150k_3_654.nn",OldNetwork);
        //model.buildfrom("./data/Core.Network/stock200k_1_1_696.nn",OldNetwork);
        model.buildfrom("./data/Network/stock_4_650.nn",OldNetwork);
        //model.buildfrom("./data/Core.Network/stock200k_4_50_647.nn",OldNetwork);
        //OldNetwork.test(TestSet.ReturnRecord(5,1));

        OldNetwork.chart(ChartType.Zero_base,"./data/ChartOutput/temp.png");
        OldNetwork.setlabels(TestSet.ReturnStr(0,false));
        OldNetwork.predict(TestSet.ReturnRecord(input_num,output_num),4,50);
        //OldNetwork.saveas("data/Core.Network/stock150k_4_1_689.nn");
    }
}

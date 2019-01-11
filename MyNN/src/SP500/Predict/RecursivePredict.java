package SP500.Predict;

import Core.FileManage.ModelRecord;
import Core.FileManage.TextRecordReader;
import Core.Listeners.ChartType;
import Core.Network.ClassificationNetwork;

public class RecursivePredict {
    public static void main(String[] args) throws Exception {
        int recursize = 60;
        int [] statistic = new int [recursize];
        for(int i=1;i<recursize+1;i++) {
            statistic[i-1] = predict(i);
        }

        for(int i=0;i<statistic.length;i++) {
            System.out.println(statistic[i]);
        }
    }

    public static int predict(int i) throws Exception{
        String filepath= "data/Stock/Test/1102_test.csv";
        TextRecordReader TestSet=new TextRecordReader(filepath,",");

        int input_num=7;
        int output_num=1;

        ClassificationNetwork OldNetwork = new ClassificationNetwork();
        ModelRecord model = new ModelRecord();
        model.buildfrom("./data/Network/stock1102_train_100k.nn", OldNetwork);
        //OldNetwork.test(TestSet.ReturnRecord(5,1));

        OldNetwork.chart(ChartType.Zero_base,"./data/ChartOutput/temp.png");
        OldNetwork.predict(TestSet.ReturnRecord(input_num,output_num), i, 50);
        //OldNetwork.saveas("data/Core.Network/stock_before1204.nn");
        return OldNetwork.getpredict();
    }
}

package Main.Train;

import Core.FileManage.ModelRecord;
import Core.FileManage.TextRecordReader;
import Core.Listeners.ChartType;
import Core.Network.ClassificationNetwork;
import Main.Predict.Factorize;
import Main.Predict.Normalize;

import java.util.Scanner;

public class Test {
    public static void main(String[] args) throws Exception {
        Scanner scan = new Scanner(System.in);
        System.out.println("Choose a Stock Number: 1.SP500 2.Ressull2000 3.Dow30 4.Nasdaq : ");

        if(scan.hasNext()) {
            String[] strs = new String[3];
            strs[0] = scan.next();

            Factorize.main(strs);
            Normalize.main(strs);
            String filepath = "data/Stock/";
            String old_model = "./data/Network/";
            String chartname = "./data/ChartOutput/";
            String new_model = "./data/Network/";

            switch (strs[0]) {
                case "1":
                    filepath += "SP500/981013_181008_test.csv";
                    old_model += "stock1102_train_400k.nn";
                    chartname += "SP500.png";
                    new_model += "Sp_200k_4_50.nn";
                    break;
                case "2":
                    filepath += "Russell2000/981013_181008_test.csv";
                    old_model += "Russell_train_200k.nn";
                    chartname += "Russell2000.png";
                    new_model += "Russell_200k_4_50.nn";
                    break;
                case "3":
                    filepath += "Dow30/981013_181008_test.csv";
                    old_model += "Dow_train_200k.nn";
                    chartname += "Dow30.png";
                    new_model += "Dow_200k_4_50.nn";
                    break;
                case "4":
                    filepath += "Nasdaq/981013_181008_test.csv";
                    old_model += "Nasdaq_train_200k.nn";
                    chartname += "Nasdaq.png";
                    new_model += "Nasdaq_200k_4_50.nn";
                    break;
            }

            int input_num = 7;
            int output_num = 1;
            //String filepath= "data/Stock/Test/1102_test.csv";
            TextRecordReader TestSet = new TextRecordReader(filepath, ",");
            //only support skip first several rows
            TestSet.skipcol(0);

            ClassificationNetwork OldNetwork = new ClassificationNetwork();
            ModelRecord model = new ModelRecord();
            model.buildfrom(old_model, OldNetwork);

            OldNetwork.chart(ChartType.Zero_base, chartname);
            OldNetwork.setlabels(TestSet.ReturnStr(0, false));
            OldNetwork.predict(TestSet.ReturnRecord(input_num, output_num), 4, 50);
            OldNetwork.saveas(new_model);
        }
    }
}

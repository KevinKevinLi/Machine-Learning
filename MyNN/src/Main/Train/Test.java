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
        ClassificationNetwork OldNetwork = new ClassificationNetwork();
        ModelRecord model = new ModelRecord();

        Scanner scan = new Scanner(System.in);
        System.out.println("Choose a Stock Number: 1.Sp500 2.Ressull2000 3.Dow30 4.Nasdaq : ");

        String filepath = "data/Stockv2/";
        String old_model = "./data/Network/Stockv2/";
        String chartname = "./data/ChartOutput/";
        String new_model = "./data/Network/Stockv2/";


        String[] strs = new String[3];
        if(scan.hasNext()) {
            strs[0] = scan.next();
            switch (strs[0]) {
                case "1":
                    filepath += "Sp500/smallup05.csv";
                    old_model += "Sp500_train_200k.nn";
                    chartname += "Sp500.png";
                    new_model += "Sp500_200k_";
                    OldNetwork.reductdata(2254.22, 676.53);
                    break;
                case "2":
                    filepath += "Russell2000/980101_190118_test.csv";
                    old_model += "Russell_train_200k.nn";
                    chartname += "Russell2000.png";
                    new_model += "Russell_200k_";
                    OldNetwork.reductdata(1413.71, 327.04);
                    break;
                case "3":
                    filepath += "Dow30/980101_190118_test.csv";
                    old_model += "Dow30_train_200k.nn";
                    chartname += "Dow30.png";
                    new_model += "Dow30_200k_";
                    OldNetwork.reductdata(20281.34, 6547.05);
                    break;
                case "4":
                    filepath += "Nasdaq/980101_190118_test.csv";
                    old_model += "Nasdaq_train_200k.nn";
                    chartname += "Nasdaq.png";
                    new_model += "Nasdaq_200k_";
                    OldNetwork.reductdata(6995.58, 1114.11);
                    break;
                default:
                    throw new Exception("Invalid stock number!");
            }
        }

        System.out.println("Specify the period and traintimes you want to perform(use space to split): ");
        int period=1;
        int traintimes=1;

        if(scan.hasNext()) {
            period=Integer.parseInt(scan.next());
            traintimes=Integer.parseInt(scan.next());
        }
        new_model += period+"_"+traintimes+".nn";

        //deal with data
        Factorize.main(strs);
        Normalize.main(strs);

        int input_num = 7;
        int output_num = 1;
        TextRecordReader TestSet = new TextRecordReader(filepath, ",");
        //only support skip first several rows
        TestSet.skipcol(0);

        model.buildfrom(old_model, OldNetwork);

        OldNetwork.chart(ChartType.Zero_base, chartname);
        OldNetwork.setlabels(TestSet.ReturnStr(0, false));
        OldNetwork.predict(TestSet.ReturnRecord(input_num, output_num), period, traintimes);
        OldNetwork.saveas(new_model);

    }
}

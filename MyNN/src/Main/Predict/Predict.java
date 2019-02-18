package Main.Predict;

import Core.FileManage.ModelRecord;
import Core.FileManage.TextRecordReader;
import Core.Listeners.ChartType;
import Core.Network.ClassificationNetwork;
import java.util.Scanner;

public class Predict {
    public static void main(String[] args) throws Exception {
        ClassificationNetwork OldNetwork = new ClassificationNetwork();
        ModelRecord model = new ModelRecord();

        Scanner scan = new Scanner(System.in);
        System.out.println("Choose a Stock Number: 1.Sp500 2.Ressull2000 3.Dow30 4.Nasdaq : ");

        String filepath = "data/Stockv2/";
        String old_model = "./data/Network/Stockv2/";
        String chartname = "./data/ChartOutput/";
        String new_model = "";

        String []strs =new String[3];
        if(scan.hasNext()) {
            strs[0] = scan.next();

            switch (strs[0]) {
                case "1":
                    filepath += "Sp500/190118_now_fac_nor.csv";
                    old_model += "Sp500_200k_";
                    chartname += "Sp500.png";
                    OldNetwork.reductdata(2254.22, 676.53);
                    break;
                case "2":
                    filepath += "Russell2000/190118_now_fac_nor.csv";
                    old_model += "Russell_200k_";
                    chartname += "Russell2000.png";
                    OldNetwork.reductdata(1413.71, 327.04);
                    break;
                case "3":
                    //dow 1_1 precise
                    filepath += "Dow30/190118_now_fac_nor.csv";
                    old_model += "Dow30_200k_";
                    chartname += "Dow30.png";
                    OldNetwork.reductdata(20281.34, 6547.05);
                    break;
                case "4":
                    //nasdaq 4_50 precise
                    filepath += "Nasdaq/190118_now_fac_nor.csv";
                    old_model += "Nasdaq_200k_";
                    chartname += "Nasdaq.png";
                    OldNetwork.reductdata(6995.58, 1114.11);
                    break;

                default:
                    throw new Exception("Invalid stock number!");
            }
        }

        int period=1;
        int traintimes=1;
        System.out.println("Specify the training period you want to perform: ");
        if(scan.hasNext()) {
            period=Integer.parseInt(scan.next());
        }
        System.out.println("Specify the training times you want to perform: ");
        if(scan.hasNext()) {
            traintimes = Integer.parseInt(scan.next());
        }
        old_model += period+"_"+traintimes+".nn";

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
        //OldNetwork.saveas(New_model);

    }
}

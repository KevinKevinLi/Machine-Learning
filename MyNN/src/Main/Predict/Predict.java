package Main.Predict;

import Core.FileManage.ModelRecord;
import Core.FileManage.TextRecordReader;
import Core.Listeners.ChartType;
import Core.Network.ClassificationNetwork;
import java.util.Scanner;

public class Predict {
    public static void main(String[] args) throws Exception {
        Scanner scan = new Scanner(System.in);
        System.out.println("Choose a Stock Number: 1.SP500 2.Ressull2000 3.Dow30 4.Nasdaq : ");

        if(scan.hasNext()) {
            String []strs =new String[3];
            strs[0]=scan.next();

            Factorize.main(strs);
            Normalize.main(strs);
            String filepath="data/Stock/";
            String old_model="./data/Network/";
            String chartname="./data/ChartOutput/";
            String new_model="";

            switch(strs[0]){
                case "1":
                    filepath+="SP500/1005_now_fac_nor.csv";
                    old_model+="stock_4_650.nn";
                    chartname+="SP500.png";
                    break;
                case "2":
                    filepath+="Russell2000/1005_now_fac_nor.csv";
                    old_model+="Russell_200k_4_50.nn";
                    chartname+="Russell2000.png";
                    break;
                case "3":
                    filepath+="Dow30/1005_now_fac_nor.csv";
                    old_model+="Dow_200k_1_1.nn";
                    chartname+="Dow30.png";
                    break;
                case "4":
                    filepath+="Nasdaq/1005_now_fac_nor.csv";
                    old_model+="Nasdaq_200k_4_50.nn";
                    chartname+="Nasdaq.png";
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
            //OldNetwork.saveas("data/Core.Network/Russell_200k_4_50.nn");
        }
    }
}

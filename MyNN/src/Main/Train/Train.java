package Main.Train;

import Core.FileManage.TextRecordReader;
import Core.Network.Activation.ActivationFrame;
import Core.Network.ClassificationNetwork;
import Core.Network.Conponent.Weightinit;
import Core.Network.LossFunction.LossFunction;
import Core.Network.NetworkConfigration;

import java.util.Scanner;

public class Train {
    public static void main(String[] args) throws Exception {
        Scanner scan = new Scanner(System.in);
        System.out.println("Choose a Stock Number: 1.Sp500 2.Ressull2000 3.Dow30 4.Nasdaq :");


        if(scan.hasNext()) {

            String []strs =new String[3];
            strs[0]=scan.next();

            Factorize.main(strs);
            Normalize.main(strs);
            //stock v1
//            String filepath= "data/Stockv1/";
//            String new_model="data/Network/Stockv1";
            //stock v2
            String filepath= "data/Stockv2/";
            String new_model="data/Network/Stockv2/";


            switch(strs[0]){
                case "1":
                    filepath+="Sp500/980101_190118_train.csv";
                    new_model+="Sp500_train_200k.nn";
                    break;
                case "2":
                    filepath+="Russell2000/980101_190118_train.csv";
                    new_model+="Russell_train_200k.nn";
                    break;
                case "3":
                    filepath+="Dow30/980101_190118_train.csv";
                    new_model+="Dow30_train_200k.nn";
                    break;
                case "4":
                    filepath+="Nasdaq/980101_190118_train.csv";
                    new_model+="Nasdaq_train_200k.nn";
                    break;
            }

            //by default, last cols are outputs
            TextRecordReader TrainSet = new TextRecordReader(filepath, ",");
            TrainSet.skipcol(0);

            ClassificationNetwork NewNetwork = new ClassificationNetwork();

            int input_num = 7;
            int output_num = 1;
            long seed = 5432;
            NetworkConfigration configuration = new NetworkConfigration()
                    .base(0.1, 0.2, seed)//learningrate,(momentum),(time seed)
                    .inputlayer(input_num, Weightinit.XAVIER, ActivationFrame.Linear)
                    .hiddenlayer(10, ActivationFrame.Sigmoid)
                    .hiddenlayer(6, ActivationFrame.Sigmoid)
                    .outputlayer(output_num, ActivationFrame.Linear, LossFunction.Mse)
                    .build();
            NewNetwork.SetConfiguration(configuration);

            NewNetwork.train(TrainSet.ReturnRecord(input_num, output_num), 200000);
            NewNetwork.saveas(new_model);
        }
    }
}

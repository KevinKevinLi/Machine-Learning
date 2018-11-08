package Main.Train;

import Network.Activation.ActivationFrame;
import Network.Conponent.Weightinit;
import Network.ClassificationNetwork;
import Network.LossFunction.LossFunction;
import Network.NetworkConfigration;
import FileManage.*;

public class TrainTest {
    public static void main(String[] args) throws Exception {
        //by default, last rows are outputs
        String filepath="./data/Stock/1029/1102_train.csv";
        TextRecordReader TrainSet=new TextRecordReader(filepath,",");
        filepath="./data/Stock/1029/1102_test.csv";
        TextRecordReader TestSet=new TextRecordReader(filepath,",");

        ClassificationNetwork NewNetwork=new ClassificationNetwork();

        int input_num=7;
        int output_num=1;
        long seed=5432;
        NetworkConfigration configuration=new NetworkConfigration()
                .base(0.1,0.2,seed)//learningrate,(momentum),(time seed)
                .inputlayer(input_num,Weightinit.XAVIER,ActivationFrame.Linear)
                .hiddenlayer(10,ActivationFrame.Sigmoid)
                .hiddenlayer(6,ActivationFrame.Sigmoid)
                //.hiddenlayer(4,ActivationFrame.Sigmoid)
                //.outputlayer(output_num,ActivationFrame.Softmax,Network.LossFunction.NegativeLogLikeliHood)
                //.outputlayer(output_num,ActivationFrame.Sigmoid,Network.LossFunction.CrossEntropy)
                .outputlayer(output_num,ActivationFrame.Linear,LossFunction.Mse)
                .build();
        NewNetwork.SetConfiguration(configuration);
        //NewNetwork.SetConfigure(2,1,20,0.01, Weightinit.UNIFORM, ActivationFrame.Sigmoid, Network.LossFunction.MSE);

        //NewNetwork.train(TrainSet.ReturnRecord(3),700);
        NewNetwork.train(TrainSet.ReturnRecord(input_num,output_num),2000);
        NewNetwork.test(TestSet.ReturnRecord(input_num,output_num));
        //NewNetwork.predict(TestSet.ReturnRecord(input_num,output_num),0.001);
        //customized predict
        NewNetwork.predict(TestSet.ReturnRecord(input_num,output_num),"./data/ChartOutput/11071.png",19,50);
        NewNetwork.saveas("data/Network/stock11071.nn");
    }
}

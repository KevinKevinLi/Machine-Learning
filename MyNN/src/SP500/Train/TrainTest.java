package SP500.Train;

import Core.Network.Activation.ActivationFrame;
import Core.Network.Conponent.Weightinit;
import Core.Network.ClassificationNetwork;
import Core.Network.LossFunction.LossFunction;
import Core.Network.NetworkConfigration;
import Core.FileManage.*;

public class TrainTest {
    public static void main(String[] args) throws Exception {
        //by default, last cols are outputs
        String filepath= "data/Stock/Test/1102_train.csv";
        TextRecordReader TrainSet=new TextRecordReader(filepath,",");
        filepath= "data/Stock/Test/1102_test.csv";
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
                //.outputlayer(output_num,ActivationFrame.Softmax,Core.Network.LossFunction.NegativeLogLikeliHood)
                //.outputlayer(output_num,ActivationFrame.Sigmoid,Core.Network.LossFunction.CrossEntropy)
                .outputlayer(output_num,ActivationFrame.Linear,LossFunction.Mse)
                .build();
        NewNetwork.SetConfiguration(configuration);
        //NewNetwork.SetConfigure(2,1,20,0.01, Weightinit.UNIFORM, ActivationFrame.Sigmoid, Core.Network.LossFunction.MSE);

        //NewNetwork.train(TrainSet.ReturnRecord(3),700);
        NewNetwork.train(TrainSet.ReturnRecord(input_num,output_num),00000);
        //NewNetwork.test(TestSet.ReturnRecord(input_num,output_num));
        //NewNetwork.predict(TestSet.ReturnRecord(input_num,output_num),0.001);
        //customized predict
        //NewNetwork.predict(TestSet.ReturnRecord(input_num,output_num),"./data/ChartOutput/11071.png",60,50);
        NewNetwork.saveas("data/Network/stock1102_train_300k.nn");
    }
}

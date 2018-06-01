import Activation.ActivationFrame;
import Network.Conponent.Weightinit;
import Network.FeedFowardNetwork;
import LossFunction.LossFunction;
import Network.NetworkConfigration;
import RecordReader.TextRecordReader;

public class MLPexample {
    public static void main(String[] args) throws Exception {
        //by default, last rows are outputs
        //String filepath="./data/saturn_data_train.csv";
        //String filepath="./data/iris.txt";
        String filepath="./data/Train_csv.csv";
        TextRecordReader TrainSet=new TextRecordReader(filepath,",");
        //filepath="./data/saturn_data_eval.csv";
        filepath="./data/MLP_test.csv";
        TextRecordReader TestSet=new TextRecordReader(filepath,",");

        FeedFowardNetwork NewNetwork=new FeedFowardNetwork();

        NetworkConfigration configuration=new NetworkConfigration()
                .base(0.01,0.9)
                .inputlayer(2,Weightinit.UNIFORM)
                .hiddenlayer(20,ActivationFrame.Relu)
                .outputlayer(1,ActivationFrame.Sigmoid,LossFunction.MSE)
                .build();
        NewNetwork.SetConfiguration(configuration);

        //NewNetwork.SetConfigure(2,1,20,0.01, Weightinit.UNIFORM, ActivationFrame.Sigmoid, LossFunction.MSE);

        //NewNetwork.train(TrainSet.ReturnRecord(3),700);
        NewNetwork.train(TrainSet.ReturnRecord(3),0.001);
        NewNetwork.test(TestSet.ReturnRecord(3));
        NewNetwork.predict(TestSet.ReturnRecord(3),0.5);
    }
}

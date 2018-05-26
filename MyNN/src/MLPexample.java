import Activation.ActivationFrame;
import Network.FeedFowardNetwork;
import LossFunction.LossFunction;
import Network.NetworkConfigration;
import Network.Weight;
import RecordReader.TextRecordReader;

public class MLPexample {
    public static void main(String[] args) throws Exception {
        //by default, last rows are outputs
        String filepath="./data/Train_csv.csv";
        TextRecordReader TrainSet=new TextRecordReader(filepath,",");
        filepath="./data/MLP_test.csv";
        TextRecordReader TestSet=new TextRecordReader(filepath,",");

        FeedFowardNetwork NewNetwork=new FeedFowardNetwork();

        NetworkConfigration configuration=new NetworkConfigration()
                .base(0.01,Weight.UNIFORM)
                .inputlayer(2)
                .hiddenlayer(20,ActivationFrame.Sigmoid)
                .outputlayer(1,ActivationFrame.Sigmoid,LossFunction.MSE)
                .build();
        NewNetwork.SetConfiguration(configuration);

        //NewNetwork.SetConfigure(2,1,20,0.01, Weight.UNIFORM, ActivationFrame.Sigmoid, LossFunction.MSE);


        NewNetwork.train(TrainSet.ReturnRecord(3),700);
        NewNetwork.test(TestSet.ReturnRecord(3));
    }
}

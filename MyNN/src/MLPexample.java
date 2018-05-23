import Activation.ActivationFrame;
import Network.FeedFowardNetwork;
import Network.LossFunction;
import Network.Weight;
import RecordReader.TextRecordReader;

public class MLPexample {
    public static void main(String[] args) throws Exception {
        //by default, last rows are outputs
        String filepath="./data/Train_csv.csv";
        TextRecordReader TrainSet=new TextRecordReader(filepath,",");

        FeedFowardNetwork NewNetwork=new FeedFowardNetwork();
        NewNetwork.SetConfigure(2,1,20,0.01, Weight.UNIFORM, ActivationFrame.Sigmoid, LossFunction.MSE);

        NewNetwork.train(TrainSet.ReturnRecord(3),100);
    }
}

import Activation.ActivationFrame;
import FileManage.ModelRecord;
import FileManage.TextRecordReader;
import LossFunction.LossFunction;
import Network.ClassificationNetwork;
import Network.Conponent.Weightinit;
import Network.NetworkConfigration;

public class Predict {
    public static void main(String[] args) throws Exception {
        String filepath="./data/Stock/1018/OHLVCC-19Test.csv";
        TextRecordReader TestSet=new TextRecordReader(filepath,",");

        ClassificationNetwork OldNetwork=new ClassificationNetwork();
        ModelRecord model=new ModelRecord();
        model.buildfrom("./data/Network/stock1.nn",OldNetwork);
        //OldNetwork.test(TestSet.ReturnRecord(5,1));
        //OldNetwork.saveas("./data/Network/stock2.nn");
    }
}

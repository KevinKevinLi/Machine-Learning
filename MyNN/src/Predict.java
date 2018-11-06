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
        double []temp={0.973758479,0.126565322,-0.147917195,0.275071035,0.979465151,0.977650845
        };
        double [][]octten={temp};
        OldNetwork.predict(octten);
        OldNetwork.saveas("./data/Network/stock2.nn");
    }
}

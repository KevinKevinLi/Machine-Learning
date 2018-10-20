import Activation.ActivationFrame;
import Network.Conponent.Weightinit;
import Network.ClassificationNetwork;
import LossFunction.LossFunction;
import Network.NetworkConfigration;
import RecordManage.TextRecordReader;

public class MLPexample {
    public static void main(String[] args) throws Exception {
        //by default, last rows are outputs
        //String filepath="./data/saturn_data_train.csv";
        //String filepath="./data/iris.txt";
        //String filepath="./data/Car/TrainSet80%csv.csv";
        String filepath="./data/Stock/1018/OHLVCC-19Train.csv";
        //String filepath="./data/wine/wine-80.csv";
        TextRecordReader TrainSet=new TextRecordReader(filepath,",");
        //filepath="./data/saturn_data_eval.csv";
        //filepath="./data/wine/wine-20.csv";
        filepath="./data/Stock/1018/OHLVCC-19Test.csv";
        TextRecordReader TestSet=new TextRecordReader(filepath,",");

        ClassificationNetwork NewNetwork=new ClassificationNetwork();

        int input_num=5;
        int output_num=1;
        long seed=54321;
        NetworkConfigration configuration=new NetworkConfigration()
                .base(0.1,0.2)//learningrate,(momentum),(time seed)
                .inputlayer(input_num,Weightinit.XAVIER,ActivationFrame.Linear)
                .hiddenlayer(10,ActivationFrame.Sigmoid)
                .hiddenlayer(5,ActivationFrame.Sigmoid)
                //.hiddenlayer(4,ActivationFrame.Sigmoid)
                //.hiddenlayer(6,ActivationFrame.Sigmoid)
                //.hiddenlayer(4,ActivationFrame.Sigmoid)
                //.outputlayer(output_num,ActivationFrame.Softmax,LossFunction.NegativeLogLikeliHood)
                //.outputlayer(output_num,ActivationFrame.Sigmoid,LossFunction.CrossEntropy)
                .outputlayer(output_num,ActivationFrame.Linear,LossFunction.Mse)
                .build();
        NewNetwork.SetConfiguration(configuration);

        //NewNetwork.SetConfigure(2,1,20,0.01, Weightinit.UNIFORM, ActivationFrame.Sigmoid, LossFunction.MSE);

        //NewNetwork.train(TrainSet.ReturnRecord(3),700);
        NewNetwork.train(TrainSet.ReturnRecord(input_num,output_num),2000);
        NewNetwork.test(TestSet.ReturnRecord(input_num,output_num));
        //NewNetwork.predict(TestSet.ReturnRecord(input_num,output_num),0.001);
        //customized predict
        NewNetwork.predict(TestSet.ReturnRecord(input_num,output_num),"./data/ChartOutput/5line(2).png",5,100);
    }
}

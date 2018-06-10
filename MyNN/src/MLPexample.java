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
        String filepath="./data/Car/TrainSet80%csv.csv";
        //String filepath="./data/Train_csv.csv";
        TextRecordReader TrainSet=new TextRecordReader(filepath,";");
        //filepath="./data/saturn_data_eval.csv";
        //filepath="./data/MLP_test.csv";
        filepath="./data/Car/TestSet20%csv.csv";
        TextRecordReader TestSet=new TextRecordReader(filepath,";");

        ClassificationNetwork NewNetwork=new ClassificationNetwork();

        int input_num=21;
        int output_num=4;
        NetworkConfigration configuration=new NetworkConfigration()
                .base(0.3,0.6)//learningrate,(momentum)
                .inputlayer(input_num,Weightinit.XAVIER,ActivationFrame.Linear)
                .hiddenlayer(14,ActivationFrame.Sigmoid)
                .hiddenlayer(14,ActivationFrame.Sigmoid)
                //.hiddenlayer(14,ActivationFrame.Sigmoid)
                //.outputlayer(output_num,ActivationFrame.Softmax,LossFunction.NegativeLogLikeliHood)
                //.outputlayer(output_num,ActivationFrame.Sigmoid,LossFunction.CrossEntropy)
                .outputlayer(output_num,ActivationFrame.Sigmoid,LossFunction.Mse)
                .build();
        NewNetwork.SetConfiguration(configuration);

        //NewNetwork.SetConfigure(2,1,20,0.01, Weightinit.UNIFORM, ActivationFrame.Sigmoid, LossFunction.MSE);

        //NewNetwork.train(TrainSet.ReturnRecord(3),700);
        NewNetwork.train(TrainSet.ReturnRecord(input_num,output_num),0.01);
        NewNetwork.test(TestSet.ReturnRecord(input_num,output_num));
       // NewNetwork.predict(TestSet.ReturnRecord(input_num,output_num),0.5);
    }
}

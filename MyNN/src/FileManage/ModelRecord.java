package FileManage;


import LossFunction.LossFunction;
import Network.Conponent.Weightinit;
import Network.NetworkConfigration;
import Activation.ActivationFrame;
import Network.ClassificationNetwork;

import java.io.*;

public class ModelRecord {
    public ModelRecord(){};

    public void buildfrom(String path, ClassificationNetwork oldnet) {
        NetworkConfigration configuration = new NetworkConfigration();

        int layersnum=0;
        double [][] weightmap=null;
        double [][] biasmap=null;
        int curlayernum=0;
        int lastneuronum=0;
        int curmapnum=0;
        int curbiasnum=0;

        File file = new File(path);
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            while ((tempString = reader.readLine()) != null) {
                String temp[]=tempString.split(" ");
                switch(temp[0]){
                    case "base":
                        configuration.base(Double.valueOf(temp[1]), Double.valueOf(temp[2]),Long.valueOf(temp[3]));
                        break;
                    case "totallayer":
                        layersnum=Integer.valueOf(temp[1]);
                        weightmap=new double[layersnum-1][];
                        biasmap=new double [layersnum-1][];
                        break;
                    case "inputlayer":
                        configuration.inputlayer(Integer.valueOf(temp[1]),Weightinit.XAVIER,getactivation(temp[2]));
                        lastneuronum=Integer.valueOf(temp[1]);
                        break;
                    case "hiddenlayer":
                        configuration.hiddenlayer(Integer.valueOf(temp[1]),getactivation(temp[2]));
                        weightmap[curlayernum]=new double [lastneuronum*Integer.valueOf(temp[1])];
                        biasmap[curlayernum]=new double [Integer.valueOf(temp[1])];
                        curlayernum++;
                        lastneuronum=Integer.valueOf(temp[1]);
                        break;
                    case "outputlayer":
                        configuration.outputlayer(Integer.valueOf(temp[2]),getactivation(temp[3]),getlossfunction(temp[1]));
                        weightmap[curlayernum]=new double [lastneuronum*Integer.valueOf(temp[2])];
                        biasmap[curlayernum]=new double [Integer.valueOf(temp[2])];
                        break;
                    case "weightmap":
                        for(int i=1;i<temp.length;i++){
                            weightmap[curmapnum][i-1]=Double.valueOf(temp[i]);
                        }
                        curmapnum++;
                        break;
                    case "biasmap":
                        for(int i=1;i<temp.length;i++){
                            biasmap[curbiasnum][i-1]=Double.valueOf(temp[i]);
                        }
                        curbiasnum++;
                        break;
                }
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
        oldnet.SetConfiguration(configuration);
        configuration.setweightmap(weightmap,biasmap);
        oldnet.SetWeightMap(configuration);
    }

    public ActivationFrame getactivation(String acts){
        switch(acts){
            case "Linear":
                return ActivationFrame.Linear;
            case "Sigmoid":
                return ActivationFrame.Sigmoid;
            case "Relu":
                return ActivationFrame.Relu;
            case "Softmax":
                return ActivationFrame.Softmax;
            default:
                throw new UnsupportedOperationException("Unknown or not supported activation function: " + this);
        }
    }

    public LossFunction getlossfunction(String losss){
        switch(losss) {
            case "Subtraction":
                return LossFunction.Subtraction;
            case "CrossEntropy":
                return LossFunction.CrossEntropy;
            case "NegativeLogLikeliHood":
                return LossFunction.NegativeLogLikeliHood;
            case "Mse":
                return LossFunction.Mse;
            case "DoNothing":
                return LossFunction.DoNothing;
            default:
                throw new UnsupportedOperationException("Unknown or not supported loss function: " + this);
        }
    }
}

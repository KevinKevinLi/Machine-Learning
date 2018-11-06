package Network;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;
import Listeners.*;
import Network.LossFunction.LossFunction;
import Network.Conponent.LayerConf;
import Network.Conponent.LayerKernel;
import java.util.Date;
import java.text.SimpleDateFormat;

public class FeedForwardKernel {
    private static int input_num;
    private ArrayList<Double> inputlist = new ArrayList<Double>();
    private static int output_num;
    private static int layers_num;

    private static double learningrate;
    private static double momentum=0.0;
    private static LossFunction lossfunction;
    private static double bias=0.0;
    private static long seed=0;

    private Listener Statistic;
    private LayerKernel NetworkLayer;

    public FeedForwardKernel(){
    }

    public void setbase(int layer_num,int input_num,int output_num,double learningrate,long seed){
        this.layers_num=layer_num;
        this.input_num=input_num;
        this.output_num=output_num;
        this.learningrate=learningrate;
        this.seed=seed;
    }

    //overload
    public void setbase(int layer_num,int input_num,int output_num,double learningrate,double momentum,long seed){
        this.layers_num=layer_num;
        this.input_num=input_num;
        this.output_num=output_num;
        this.learningrate=learningrate;
        this.momentum=momentum;
        this.seed=seed;
    }

    public void setlayers(LayerConf inputlayer, ArrayList<LayerConf> hiddenlayers, LayerConf outputlayer){
        if(momentum!=0.0){
            NetworkLayer.useMomentumAlgorithm(momentum);
        }
        this.lossfunction=outputlayer.getLossfunction();
        NetworkLayer=new LayerKernel(inputlayer,hiddenlayers,outputlayer);
    }

    public void setListener(Listener L){
        Statistic=L;
    }

    public double feedforward(double [] inputset){
        double totalerror=0;
        for(int i=0;i<input_num;i++){
            inputlist.add(inputset[i]);
        }
        int layer_number=0;
        //init input layers
        for(int i=0;i<input_num;i++)
        {
            NetworkLayer.updateNeuronOutput(0,i,inputset[i]);
        }
        layer_number++;
        //cal input->hidden->output layer
        while(layer_number<layers_num) {
            //cal hidden->output layer
            for (int i = 0; i < NetworkLayer.getNeuronLineLength(layer_number); i++) {
                //record weights number
                int num = i;
                double neuron_input = 0;
                for (int j = 0; j < NetworkLayer.getNeuronLineLength(layer_number - 1); j++) {
                    //calculate weight*input
                    neuron_input += NetworkLayer.getWeight(layer_number-1, num) * NetworkLayer.getNeuronOutput(layer_number - 1, j);
                    num += NetworkLayer.getNeuronLineLength(layer_number);
                }
                //System.out.println(neuron_input+NetworkLayer.getBias(1,i));
                NetworkLayer.updateNeuronOutput(layer_number, i, neuron_input + NetworkLayer.getBias(layer_number-1, i));
            }
            NetworkLayer.CheckUpdate(layer_number);
            layer_number++;
        }
        //cal error
        for(int i=0;i<output_num;i++) {
            //cal error
            NetworkLayer.setNeuronTarget(layers_num-1, i, inputset[input_num + i]);
            //System.out.println(i+" "+NetworkLayer.getNeuronOutput(1,i));
            NetworkLayer.setNeuronError(layers_num-1, i, lossfunction.exec(inputset[input_num + i], NetworkLayer.getNeuronOutput(layers_num-1, i), output_num));
            totalerror += NetworkLayer.getNeuronError(layers_num-1, i);
        }
        //System.out.println(totalerror);
        Statistic.addError(totalerror);
        Statistic.addSampleNum();
        return totalerror;
    }

    public void backpropagation(){
        NetworkLayer.cloneWeightMap();
        //update risiduals first
        //residual in output layer only need to be setted once because it is only related to output and target_output
        double residual = 0;
        for (int j = 0; j < NetworkLayer.getNeuronLineLength(layers_num-1); j++) {
            //residual only need to be setted once because it is only related to output and target_output
            residual = NetworkLayer.getNeuronDerivative(layers_num-1,j) * lossfunction.execDrivative(NetworkLayer.getNeuronTarget(layers_num-1,j), NetworkLayer.getNeuronOutput(layers_num-1,j), output_num);
            //NeuronMap[1][j].setResidual(residual);
            NetworkLayer.setNeuronResidual(layers_num-1,j,residual);
        }

        int weight_num = 0;
        //cal out->hidden layer
        //Using Partial derivative and chain derivative rule
        //w+=w-learningrate*residual*output_hiddenlayer_neuron
        //residual=activation_derivative*loss_derivative
        weight_num = 0;
        for (int i = 0; i < NetworkLayer.getNeuronLineLength(layers_num-2); i++) {
            for (int j = 0; j < NetworkLayer.getNeuronLineLength(layers_num-1); j++) {
                residual = NetworkLayer.getNeuronResidual(layers_num-1,j);
                NetworkLayer.updateWeight(layers_num-2,weight_num,-learningrate * residual * NetworkLayer.getNeuronOutput(layers_num-2,i));
                //update bias
                if(i==0){
                    NetworkLayer.updateBias(layers_num-2,weight_num,-learningrate * residual);
                }
                weight_num++;
            }
        }

        //cal hiddens->input layer
        //loss dirivative is more complex than before because hidden neurons are affected by multi output neurons
        //loss dirivative=loss_dirivative_outputs_sum
        //loss_dirivative_output_sum=sum(output_residual*w)
        //w+=w-learningrate*residual*input
        int outputweight_num = 0;
        for(int m=layers_num-1-1;m>0;m--) {
            weight_num = 0;
            for (int i = 0; i < NetworkLayer.getNeuronLineLength(m-1); i++) {
                for (int j = 0; j < NetworkLayer.getNeuronLineLength(m); j++) {
                    if (i == 0) {
                        double sum_loss = 0;
                        for (int k = 0; k < NetworkLayer.getNeuronLineLength(m+1); k++) {
                            sum_loss += NetworkLayer.getNeuronResidual(m+1, k) * NetworkLayer.getBackupWeight(m, outputweight_num);
                            outputweight_num++;
                        }
                        residual = NetworkLayer.getNeuronDerivative(m, j) * sum_loss;
                        NetworkLayer.setNeuronResidual(m, j, residual);
                        NetworkLayer.updateWeight(m-1, weight_num, -learningrate * residual * NetworkLayer.getNeuronOutput(m-1,i));
                        //update bias
                        NetworkLayer.updateBias(m-1, weight_num, -learningrate * residual);
                    } else {
                        residual = NetworkLayer.getNeuronResidual(m, j);
                        NetworkLayer.updateWeight(m-1, weight_num, -learningrate * residual * NetworkLayer.getNeuronOutput(m-1,i));
                    }
                    weight_num++;
                }
                outputweight_num = 0;
            }
        }
        //init
        inputlist.clear();
    }

    public void printNetwork(){
        for(int i=0;i<NetworkLayer.getWeightRowLength();i++){
            for(int j=0;j<NetworkLayer.getWeightLineLength(i);j++) {
                System.out.println("("+i+","+j+") W:"+NetworkLayer.getWeight(i,j)+" M:"+NetworkLayer.getMomentum(i,j));
            }
        }
        for(int i=0;i<NetworkLayer.getWeightRowLength();i++){
            for(int j=0;j<NetworkLayer.getNeuronLineLength(i+1);j++){
                System.out.println("("+i+","+j+") B:"+NetworkLayer.getBias(i,j));
            }
        }
        for(int i=0;i<NetworkLayer.getNeuronRowLength();i++){
            for(int j=0;j<NetworkLayer.getNeuronLineLength(i);j++){
                System.out.println("Neuron("+i+","+j+"): "+NetworkLayer.getNeuronActivation(i,j));
            }
        }
    }

    public void setLossfunction(LossFunction los){
        this.lossfunction=los;
    }

    public void setWeightmap(double [][]weightmap,double [][]biasmap){
        NetworkLayer.setWeightMap(weightmap,biasmap);
    }

    public void savenet(String path,double error){
        //output to file
        FileOutputStream out = null;
        try {
            File outFile = new File(path);
            if (!outFile.getParentFile().exists()) {
                outFile.getParentFile().mkdirs();
            }
            out = new FileOutputStream(path);
            //feed forward
            StringBuilder stringb= new StringBuilder("name"+" feedfoward\n");
            //date
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss\n");//设置日期格式
            stringb.append("date "+df.format(new Date()));
            //test_error predicterror
            stringb.append("testerror "+error+"\n");
            //total layer
            stringb.append("totallayer "+layers_num+"\n");
            //hidden_layer_num learning_rate momentum seed
            stringb.append("base "+learningrate+" "+momentum+" "+seed+"\n");
            //LAYER CONFI
            for(int i=0;i<NetworkLayer.getNeuronRowLength();i++){
                if(i==0){
                    stringb.append("inputlayer"+" ");
                }
                else if(i==NetworkLayer.getNeuronRowLength()-1){
                    stringb.append("outputlayer"+" "+lossfunction+" ");
                }
                else{
                    stringb.append("hiddenlayer"+" ");
                }
                stringb.append((int)NetworkLayer.getNeuronLineLength(i)+" "+NetworkLayer.getNeuronActivation(i,0)+"\n");
            }
            //weight map
            for(int i=0; i<NetworkLayer.getWeightRowLength();i++){
                stringb.append("weightmap ");
                for(int j=0;j<NetworkLayer.getWeightLineLength(i);j++){
                    stringb.append(NetworkLayer.getWeight(i,j)+" ");
                }
                stringb.append("\n");
            }
            //biasmap
            for(int i=0;i<NetworkLayer.getBiasRowLength();i++){
                stringb.append("biasmap ");
                for(int j=0;j<NetworkLayer.getBiasLineLength(i);j++){
                    stringb.append(NetworkLayer.getBias(i,j)+" ");
                }
                stringb.append("\n");
            }

            String string=stringb.toString();
            byte[]bytes=string.getBytes();
            out.write(bytes);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    // do nothing
                }
            }
        }
    }
}

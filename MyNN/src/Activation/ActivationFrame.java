package Activation;

import Activation.*;

public enum ActivationFrame {
    Sigmoid,
    Softmax,
    Relu;

    ActivationFrame(){}

    public Neuron getNeuron(){
        switch(this){
            case Sigmoid:
                Sigmoid activationSig =new Sigmoid();
                return activationSig;
            case Relu:
                Relu activationRelu=new Relu();
                return activationRelu;
            case Softmax:
                Softmax activationSoftmax =new Softmax();
                return activationSoftmax;
            default:
                throw new UnsupportedOperationException("Unknown or not supported activation function: " + this);
        }
    }
}

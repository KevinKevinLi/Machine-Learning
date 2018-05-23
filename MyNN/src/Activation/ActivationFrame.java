package Activation;

import Activation.*;

public enum ActivationFrame {
    Sigmoid;

    ActivationFrame(){}

    public Neuron getNeuron(){
        switch(this){
            case Sigmoid:
                Sigmoid sig =new Sigmoid();
                return sig;
            default:
                throw new UnsupportedOperationException("Unknown or not supported activation function: " + this);
        }
    }
}

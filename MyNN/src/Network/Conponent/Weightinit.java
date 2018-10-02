package Network.Conponent;
import java.util.Random;

public enum Weightinit {
    XAVIER,
    CONSTANT,
    UNIFORM;

    private Weightinit(){}

    public double init(int input_num,int output_num,Random random,long seed) {
        double randomNum=0.0;
        //System.out.println(seed);
        if(seed!=0){
            randomNum=2*random.nextDouble()-1.0;
            //System.out.println(randomNum);
        }
        else{
            randomNum=2*Math.random()-1.0;
        }

        switch(this){
            case XAVIER:
                return (Math.sqrt(6)/Math.sqrt(input_num+output_num))*randomNum;
            case UNIFORM:
                return randomNum;//0->1 -1->1?
            case CONSTANT:
                return 1.0;
            default:
                throw new UnsupportedOperationException("Unknown or not supported weightinit function: " + this);
        }
    }
}

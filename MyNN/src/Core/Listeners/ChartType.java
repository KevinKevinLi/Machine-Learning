package Core.Listeners;

public enum ChartType {
    Zero_one,
    Zero_base,
    Origin;

    private Core.Listeners.Chart Chart;

    ChartType(){
        Chart = new Chart();
    }

    public void feed(double predict, double actuall, double flag, String label){
        switch(this){
            case Zero_one:
                if(actuall>0) {
                    if (Math.signum(predict - flag) == Math.signum(actuall - flag)) {
                        Chart.add(1, "correctness", label);
                    } else {
                        Chart.add(0, "correctness", label);
                    }
                }
                break;
            case Zero_base:
                Chart.add(predict-flag, "predict", label);
                if(actuall>0) {
                    Chart.add(actuall - flag, "actuall", label);
                }
                Chart.add(0,"zero",label);
                break;
            case Origin:
                Chart.add(predict, "predict", label);
                if(actuall>0) {
                    Chart.add(actuall, "actuall", label);
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown or not supported activation function: " + this);
        }
    }

    public void draw(String outpath){
        switch(this) {
            case Zero_one:
                Chart.createChart(outpath, 1000, 500);
                break;
            case Zero_base:
                Chart.createChart(outpath, 2000, 1000);
                break;
            case Origin:
                Chart.createChart(outpath,4000,2000);
                break;
            default:
                throw new UnsupportedOperationException("Unknown or not supported activation function: " + this);
        }
    }
}

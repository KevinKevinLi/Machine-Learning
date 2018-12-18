package Listeners;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.general.DatasetUtilities;
import org.jfree.data.category.DefaultCategoryDataset;

public class Chart {
    private static DefaultCategoryDataset categoryDataset;

    public Chart(){
        categoryDataset = new DefaultCategoryDataset();
    }

    public void add(double value,String rowKey,String colKey){
        categoryDataset.addValue(value, rowKey, colKey);
    }

    public static void createChart(String outputPath) {
        JFreeChart jfreechart = ChartFactory.createLineChart("Main.PredictTest.PredictTest-Actuall",
                "X",
                "Y",
                categoryDataset, // dataset
                PlotOrientation.VERTICAL, true, // legend
                false, // tooltips
                false); // URLs

        CategoryPlot plot = (CategoryPlot)jfreechart.getPlot();
        // background transparency
        plot.setBackgroundAlpha(0.5f);
        // frontground transparency
        plot.setForegroundAlpha(0.5f);
        // others
        LineAndShapeRenderer renderer = (LineAndShapeRenderer)plot.getRenderer();
        renderer.setBaseShapesVisible(false); // points visible
        renderer.setBaseLinesVisible(true); // line visible
        renderer.setUseSeriesOffset(false); // offset
        renderer.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator());
        renderer.setBaseItemLabelsVisible(false);
        renderer.setBaseShapesFilled(false);

        //output to file
        FileOutputStream out = null;
        try {
            File outFile = new File(outputPath);
            if (!outFile.getParentFile().exists()) {
                outFile.getParentFile().mkdirs();
            }
            out = new FileOutputStream(outputPath);

            // ChartUtilities.writeChartAsPNG(out, chart, 600, 400);

            ChartUtilities.writeChartAsPNG(out, jfreechart, 4000, 2000);
            out.flush();
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
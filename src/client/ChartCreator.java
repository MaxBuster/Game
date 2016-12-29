/**
 * This is where we generate JFreeChart objects to display on the client UI
 */

package client;

import java.awt.Color;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.IntervalXYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class ChartCreator {

	/**
	 * Creates a complete blank JChart that is ready to add to the UI
	 * @return A ChartPanel object representing the blank chart
	 */
	public static ChartPanel create_blank_chart() {
		final JFreeChart chart = ChartFactory.createXYLineChart(
				"Expectations", "Position", "Likelihood", null,
				PlotOrientation.VERTICAL, true, false, false);
		XYPlot plot = (XYPlot) chart.getPlot();
		plot.getRangeAxis().setTickMarksVisible(false);
		plot.getRangeAxis().setTickLabelsVisible(false);
		plot.setBackgroundPaint(Color.white);
		plot.getDomainAxis().setRange(0.00, 100);
		final ChartPanel chartPanel = new ChartPanel(chart);
		chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
		return chartPanel;
	}

	/**
	 * Creates a JFreeChart dataset from an array of doubles and a string title
	 * @param data - array of data where position=x value and value at position=y value
	 * @param name - the title of the dataset
	 * @return JFreeChart compatible dataset
	 */
	public static IntervalXYDataset create_dataset(double[] data, String name) {
		final XYSeries series = new XYSeries(name);
		for (int i = 0; i < data.length; i++) {
			series.add(i, data[i]);
		}
		final XYSeriesCollection dataset = new XYSeriesCollection(series);
		return dataset;
	}
}

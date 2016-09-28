package client;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.IntervalXYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class ChartCreator {

	public static ChartPanel create_chart(double[] data) {
		IntervalXYDataset dataset = createDataset(data, "Voters");
		final JFreeChart chart = ChartFactory.createXYLineChart(
				"Expectations", "Ideal Point", "Distribution", dataset,
				PlotOrientation.VERTICAL, true, false, false);
		XYPlot plot = (XYPlot) chart.getPlot();
		plot.getDomainAxis().setRange(0.00, 100);
		final ChartPanel chartPanel = new ChartPanel(chart);
		chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
		return chartPanel;
	}

	public static IntervalXYDataset createDataset(double[] beta, String dataName) {
		final XYSeries series = new XYSeries(dataName);
		for (int i = 0; i < beta.length; i++) {
			series.add(i, beta[i]);
		}
		final XYSeriesCollection dataset = new XYSeriesCollection(series);
		return dataset;
	}
}

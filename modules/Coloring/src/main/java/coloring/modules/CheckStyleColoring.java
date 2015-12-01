package coloring.modules;

import java.io.IOException;
import java.util.HashMap;

import coloring.AbstractColoring;
import coloring.metric.CountColoringMetric;
import coloring.metric.IColoringMetric;
import coloring.metric.NullColoringMetric;
import metrics.Metric;
import utility.DataColour;

public class CheckStyleColoring extends AbstractColoring {
	HashMap<String, DataColour> datamap;

	@Override
	public void getDataColoring() throws IOException {
		String path = params[0];
		String metricString = params[1];
		int max = Integer.parseInt(params[2]);
		
		Metric metric = Metric.createMetric(metricString, max, null);
		metric.execute(path);
		try {
			datamap = metric.getData();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public IColoringMetric calculateMetric(String path) {
		
		DataColour item = datamap.get(path);
		double nProblems;
		if (item == null || (nProblems = item.getMetricValue()) < 0 ) {
			return new NullColoringMetric();
		}
		// at most 5 
		return new CountColoringMetric(Math.ceil(nProblems), 5);
	}
}
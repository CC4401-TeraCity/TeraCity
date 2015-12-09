package coloring.modules;

import java.util.Hashtable;
import java.util.Random;

import coloring.AbstractColoring;
import coloring.metric.IColoringMetric;
import coloring.metric.NullColoringMetric;
import coloring.metric.RateColoringMetric;

public class MockColoring extends AbstractColoring {
	Hashtable<String, Integer> data;
	String metricType;

	public IColoringMetric getMetric(String path) {
		if (metricType == "random") {
			return new RateColoringMetric(
					new Random().nextDouble());

		} else if (metricType == "good") {
			return new RateColoringMetric(RateColoringMetric.BEST_RATE);
		}
		
		// invalid option!
		return new NullColoringMetric();
	}
	
	
	@Override
	public void getDataColoring(){
		
		metricType = params[0];	
		data = new Hashtable<String, Integer>();
	}
	
}
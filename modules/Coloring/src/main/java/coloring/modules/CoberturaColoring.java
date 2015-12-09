package coloring.modules;

import java.io.IOException;

import coloring.AbstractColoring;
import coloring.metric.IColoringMetric;
import coloring.metric.NullColoringMetric;
import coloring.metric.RateColoringMetric;
import commands.CoberturaCommand;

public class CoberturaColoring extends AbstractColoring {
	
	CoberturaCommand cobby;
	
	public CoberturaColoring(){
		cobby = new CoberturaCommand();
	}
	
	@Override
	public IColoringMetric getMetric(String path) {
		
		double rate =  CoberturaCommand.getMetricValue(path);
		if (rate < 0) {
			return new NullColoringMetric();
		}
		return new RateColoringMetric(rate);
	}
	
	@Override
	public void getDataColoring() throws IOException {
		cobby.analyze(params[0], params[1], params[2]);
		cobby.waitForAnalysis();
	}
}

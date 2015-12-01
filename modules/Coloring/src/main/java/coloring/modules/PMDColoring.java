package coloring.modules;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import coloring.AbstractColoring;
import coloring.metric.CountColoringMetric;
import coloring.metric.IColoringMetric;
import coloring.metric.NullColoringMetric;
import processor.PMDProcessor;

public class PMDColoring extends AbstractColoring {
	
	Map<String, Integer> warningCounterMap = new HashMap<String, Integer>();
	static Map<String, PMDProcessor> rulesApplied = new HashMap<String, PMDProcessor>();
	
	String rootPath = getRootPath();
	String metricType;


	@Override
	public IColoringMetric calculateMetric(String path) {
		
		Integer warningCount = warningCounterMap.get(path);
		
		// warning free classes will not have a counter on the map. 
		if (warningCount == null) { warningCount = 0; }
		
		if (warningCount < 0) { return new NullColoringMetric(); }

		// at most 7 warnings
		return new CountColoringMetric(warningCount.intValue(), 7);
	}
	
	@Override
	public void getDataColoring() throws IOException {
		PMDProcessor pmd = getProcessor();
		warningCounterMap = pmd.getCounterMap();
	}

	private PMDProcessor getProcessor() {
		
		metricType = params[0];
		if (!rulesApplied.keySet().contains(metricType) || !rootPath.equals(getRootPath()))
		{
			rulesApplied.put(metricType, new PMDProcessor(rootPath, metricType));
			rootPath = getRootPath();
		}
		return rulesApplied.get(metricType);
	}

}
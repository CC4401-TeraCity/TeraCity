package coloring.modules;

import java.util.Hashtable;

import org.terasology.codecity.world.structure.CodeRepresentation;
import org.terasology.registry.CoreRegistry;

import coloring.AbstractColoring;
import coloring.metric.CountColoringMetric;
import coloring.metric.IColoringMetric;
import coloring.metric.NullColoringMetric;
import coloring.metric.RateColoringMetric;
import gitMetrics.GitMetric;

public class GitColoring extends AbstractColoring {
	Hashtable<String, Integer> data;
	GitMetric metricType;

	@Override
	public IColoringMetric calculateMetric(String path) {
		
		Integer problems = data.get(path);
		if (problems == null) { return new NullColoringMetric(); }
		
		if (metricType.toString() == "bug") {
			if (problems > 0) {
				return new RateColoringMetric(RateColoringMetric.WORST_RATE);
			}
			return new RateColoringMetric(RateColoringMetric.BEST_RATE);
			
		} else if (metricType.toString() == "version") {
			// we don't mind getting 50 or more errors
			return new CountColoringMetric(problems.intValue(), 50);
		}
		
		// invalid metric by default
		return new NullColoringMetric();
	}

	@Override
	public void getDataColoring(){
		String url = CoreRegistry.get(CodeRepresentation.class).getGithubDir();
		String metricString = params[0];
		String projectName = params[2];
		String output = "modules/GitHub/tempRepo/";
		
		metricType = new GitMetric(metricString, url, projectName, output, null);
		metricType.execute();
		metricType.setData();
		data = metricType.getData();
	}

}
package coloring;

import java.io.IOException;

import coloring.metric.IColoringMetric;

public interface IColoring{
	
	public void executeColoring();
	
	public void getDataColoring() throws IOException ;
	
	public IColoringMetric getMetric(String path);
	
	public void execute(String[] params);
	
	public void setFaceToPaint(String face);
	
	String[] getMetricData(String path);
	
}

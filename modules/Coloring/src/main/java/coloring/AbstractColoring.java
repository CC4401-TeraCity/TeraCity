package coloring;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.io.BufferedReader;
import java.io.FileReader;

import org.terasology.codecity.world.map.CodeMap;
import org.terasology.codecity.world.map.CodeMapFactory;
import org.terasology.codecity.world.map.MapObject;
import org.terasology.codecity.world.structure.CodeRepresentation;
import org.terasology.codecity.world.structure.scale.CodeScale;
import org.terasology.codecity.world.structure.scale.SquareRootCodeScale;
import org.terasology.registry.CoreRegistry;
import org.terasology.rendering.nui.layers.ingame.coloring.FaceToPaint;
import org.terasology.world.WorldProvider;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;

import coloring.metric.IColoringMetric;
import coloring.commands.ColoringCommands;
import coloring.commands.PlaceBlockCommand;

public abstract class AbstractColoring implements IColoring, Runnable {
	
	protected String[] params;
	private String faceToPaint = FaceToPaint.ALL.toString();
	
	public void setFaceToPaint(String face) {
		this.faceToPaint = face;
	}
	
	public ArrayList<String> getClassPaths(){
		WorldProvider world = CoreRegistry.get(WorldProvider.class);
        if (world != null) {
        	CodeMap map = CoreRegistry.get(CodeMap.class);
        	CodeScale scale = new SquareRootCodeScale();
        	return getPathInfo(map,scale);
        }
        throw new IllegalArgumentException("Sorry, something went wrong!");
	}
	public ArrayList <String> getPathInfo(CodeMap map, CodeScale scale) {
		CodeMapFactory factory = new CodeMapFactory(scale);
		ArrayList<String> result = new ArrayList<String>();
		for (MapObject obj: map.getMapObjects()){
			if (obj.isOrigin()){
				result.add(obj.getObject().getBase().getPath());
				CodeMap next = obj.getObject().getSubmap(scale, factory);
				result.addAll(getPathInfo(next, scale));
			}
		}
		return result;
	
	}
	public String getRootPath(){
		CodeRepresentation code =  CoreRegistry.get(CodeRepresentation.class);
		return code.getPath();
	}
	
	@Override
	public void executeColoring() {
		System.out.println("Painting face: " + faceToPaint);
		ArrayList<String> paths = getClassPaths();
		PlaceBlockCommand pbc = new PlaceBlockCommand();
		for (String path : paths) {
			String[] classMetricData = getMetricData(path); 
			int maxHealth = 10;
			int damage = (int)((maxHealth - 1)*(1.0 - Double.valueOf(classMetricData[0])));
			damage = Math.min((maxHealth-1), damage);
			
			pbc.ColorBuildCommon(path, classMetricData[1], faceToPaint, damage, maxHealth);
		}
		ColoringCommands.STATE = "Awaiting analisys";
	}

	@Override
	public String[] getMetricData(String path) {

		try {				
			BufferedReader buffer = null;
			String currentLine = "";
			String csvSplitBy = ",";
			String className = this.getClass().getSimpleName();
			buffer = new BufferedReader(new FileReader(className + ".csv"));
			
			String[] data = new String[2];
			while ((currentLine = buffer.readLine()) != null) {
				String[] pathMetric = currentLine.split(csvSplitBy);
				if (pathMetric[0] == path) {
					data[0] = pathMetric[1];
					data[1] = pathMetric[2];
					break;
				}
			}
			buffer.close();
			return data;
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public abstract IColoringMetric getMetric(String path);
	
	@Override
	public abstract void getDataColoring() throws IOException;
	
	@Override
	public void run() {
		try {
			getDataColoring();
		} catch (IOException e) {
			System.err.println("Falla el coloreo");
			e.printStackTrace();
		}
	}
	
	@Override
	public void execute(String[] params) {
		this.params = params;
		ColoringCommands.STATE = "Analyzing...";
		ListeningExecutorService executor = MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(1));
		ListenableFuture<AbstractColoring> listenableFuture = executor.submit(this,this);
	    CoreRegistry.put(ListenableFuture.class, listenableFuture);
	    Futures.addCallback(listenableFuture, new FutureCallback<AbstractColoring>() {
	        public void onSuccess(AbstractColoring result) {
	        	ColoringCommands.STATE = "Coloring...";
	        	System.out.println("Listo para pintar");
	        	
	        }

	        public void onFailure(Throwable thrown) {
	        	ColoringCommands.STATE = "Analyzing...";
	            System.err.println("Falla de Analisis");
	        }
	    });
	    executor.shutdown();
	}

}

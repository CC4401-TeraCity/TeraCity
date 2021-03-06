package coloring;

import java.util.HashMap;
import java.util.Map;

import org.terasology.registry.CoreRegistry;

public class ColoringRegistry {

    private Map<String, BuildRegister> buildMap;
    private ColoringState state;
    
    public ColoringRegistry() {
    	buildMap = new HashMap<>();
    	state = new ColoringState();
    }
 
    public void updateRegistry(BuildRegister build) {
    	buildMap.put(build.name, build);
    }
    
    // returns the BuildRegister associated to the build name
    // if the register does not exits yet, then it is created	
    public BuildRegister getBuild(String name) {
    	
    	if (!buildMap.containsKey(name)) {
    		buildMap.put(name, new BuildRegister(name));
        }
    	return buildMap.get(name);
    }
    
    // gets ColoringRegistty from the CoreRegister object.
    // if the register does not exits yet, then it is created
    public static ColoringRegistry getRegister() {
        
    	ColoringRegistry coloringRegistry = CoreRegistry.get(ColoringRegistry.class);
        if (coloringRegistry == null) {
        	coloringRegistry = new ColoringRegistry();
        	CoreRegistry.put(ColoringRegistry.class, coloringRegistry);
        }
        return coloringRegistry;
    }
    
    public ColoringState getState() {
    	return this.state;
    }
    
    public void updateState(boolean renderQuakes) {
    	this.state.setRenderQuakes(renderQuakes);
    }
}
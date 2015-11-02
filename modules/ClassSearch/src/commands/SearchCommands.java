package commands;

import java.util.HashSet;
import java.util.Set;

import org.terasology.codecity.world.map.CodeMap;
import org.terasology.codecity.world.map.MapObject;
import org.terasology.entitySystem.systems.BaseComponentSystem;
import org.terasology.entitySystem.systems.RegisterSystem;
import org.terasology.logic.console.commandSystem.annotations.Command;
import org.terasology.logic.console.commandSystem.annotations.CommandParam;
import org.terasology.registry.CoreRegistry;

/**
 * @author mrgcl
 */
@RegisterSystem
public class SearchCommands extends BaseComponentSystem{
	@Command(shortDescription = "Searches for the className building and moves the camera " +
			"towards it if it exists.")
    public String search(@CommandParam("className") String className) {
		CodeMap codeMap = CoreRegistry.get(CodeMap.class);
		Set<MapObject> possibleResults = searchForClassName(codeMap.getMapObjects(), className);
		if(possibleResults.size() == 1){
			//TODO: move map.
			return "Class found!";
		}
		else if(possibleResults.size() > 1){
			return "Too many results, try refining your search.";
		}
        return "Class not found.";
    }
	/**
	 * Searches for all MapObjects which start with the given className.
	 * 
	 * @param allObjects the set with all the MapObjects.
	 * @param className the search query.
	 * @return a subset of allObjects which may meet the query.
	 */
	private Set<MapObject> searchForClassName(Set<MapObject> allObjects, String className){
		Set<MapObject> possibleResults = new HashSet<MapObject>();
		for(MapObject object : allObjects){
			String objectClass = object.getObject().getBase().getName().toLowerCase();
			if(objectClass.startsWith(className.toLowerCase())){
				possibleResults.add(object);
			}
		}
		return possibleResults;		
	}
}
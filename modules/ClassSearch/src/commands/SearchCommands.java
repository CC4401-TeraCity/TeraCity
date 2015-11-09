package commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.terasology.codecity.world.map.CodeMap;
import org.terasology.codecity.world.map.CodeMapFactory;
import org.terasology.codecity.world.map.MapObject;
import org.terasology.codecity.world.structure.scale.CodeScale;
import org.terasology.entitySystem.entity.EntityRef;
import org.terasology.entitySystem.systems.BaseComponentSystem;
import org.terasology.entitySystem.systems.RegisterSystem;
import org.terasology.logic.console.commandSystem.annotations.Command;
import org.terasology.logic.console.commandSystem.annotations.CommandParam;
import org.terasology.logic.players.LocalPlayer;
import org.terasology.logic.players.PlayerSystem;
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
		List<MapObject> possibleResults = searchForClassName(codeMap.getMapObjects(), className);
		if(possibleResults.size() == 1){
			MapObject result = possibleResults.get(0);
			LocalPlayer localPlayer = CoreRegistry.get(LocalPlayer.class);
			EntityRef client = localPlayer.getClientEntity();
			PlayerSystem playerSystem = CoreRegistry.get(PlayerSystem.class);
			CodeScale codeScale = CoreRegistry.get(CodeScale.class);
			CodeMapFactory codeMapFactory = CoreRegistry.get(CodeMapFactory.class);
			playerSystem.teleportCommand(client, result.getPositionX(), result.getHeight(codeScale, codeMapFactory), result.getPositionZ());
			return "Class found, teleporting!";
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
	private List<MapObject> searchForClassName(Set<MapObject> allObjects, String className){
		List<MapObject> possibleResults = new ArrayList<MapObject>();
		for(MapObject object : allObjects){
			String objectClass = object.getObject().getBase().getName().toLowerCase();
			if(objectClass.startsWith(className.toLowerCase())){
				possibleResults.add(object);
			}
		}
		return possibleResults;		
	}
}
package org.terasology.codecity.world.generator;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.terasology.codecity.world.loader.CodeCityDefaultLoader;
import org.terasology.codecity.world.loader.CodeCityLoader;
import org.terasology.codecity.world.loader.CodeCityLoaderManager;
import org.terasology.codecity.world.loader.DirectorySingleton;
import org.terasology.codecity.world.map.CodeMap;
import org.terasology.codecity.world.map.CodeMapFactory;
import org.terasology.codecity.world.map.DrawableCode;
import org.terasology.codecity.world.map.DrawableCodeFactory;
import org.terasology.codecity.world.structure.CodeRepresentation;
import org.terasology.codecity.world.structure.scale.CodeScale;
import org.terasology.codecity.world.structure.scale.SquareRootCodeScale;
import org.terasology.engine.SimpleUri;
import org.terasology.registry.CoreRegistry;
import org.terasology.world.generation.BaseFacetedWorldGenerator;
import org.terasology.world.generation.WorldBuilder;
import org.terasology.world.generator.RegisterWorldGenerator;

/**
 * Generate a new world using information provided by JEdit
 */
@RegisterWorldGenerator(id = "codecity", displayName = "CodeCity", description = "Generates the world using a CodeCity structure")
public class CodeCityWorldGenerator extends BaseFacetedWorldGenerator {
    private final CodeScale cScale = new SquareRootCodeScale();

    public CodeCityWorldGenerator(SimpleUri uri) {
        super(uri);
    }

    @Override
    public void initialize() {
        
        //Retorna en loader por fichero  en caso de que este exista, si no, el por socket.
    	//String path = "C:"+File.separator+"DCC"+File.separator+"Primavera15"+File.separator+"Ingenieria"+File.separator+"TeraCity"+File.separator+"modules"+File.separator+"GitHub";
        String path = DirectorySingleton.getInstance("").getPath();
        CodeCityLoader loader;
        if(path != ""){
        	loader = new CodeCityProjectLoader(path);
        }
        else{
        	loader = new CodeCityDefaultLoader();
        }
    	//CodeCityLoader loader = CodeCityLoaderManager.getLoader(path);
        
    	

        //

        //CodeCityLoader loader = new CodeCitySocketLoader(25778);
        
        CodeRepresentation code = loader.loadCodeRepresentation();

        CodeMap codeMap = generateCodeMap(code);
        CoreRegistry.put(CodeMap.class, codeMap);
        CoreRegistry.put(CodeRepresentation.class, code);

        super.initialize();
        
       // storeCodeRepresentation(code);
    }

    private void storeCodeRepresentation(CodeRepresentation code) {
        JEditExporter.export(code);
        
    }

    @Override
    protected WorldBuilder createWorld(long seed) {
        return new WorldBuilder(seed)
                .addProvider(new CodeCityGroundProvider())
                .addProvider(new CodeCityBuildingProvider())
                .addRasterizer(new CodeCityGroundRasterizer())
                .addRasterizer(new CodeCityBuildingRasterizer())
                .setSeaLevel(0);
    }

    /**
     * Insert into the CodeRegistry the DrawableCode, gen
     * @param code
     */
    private CodeMap generateCodeMap(CodeRepresentation code) {
        DrawableCodeFactory drawableFactory = new DrawableCodeFactory();
        List<DrawableCode> list = new ArrayList<DrawableCode>();
        list.add(drawableFactory.generateDrawableCode(code));

        CodeMapFactory factory = new CodeMapFactory(cScale);
        return factory.generateMap(list);
    }

}

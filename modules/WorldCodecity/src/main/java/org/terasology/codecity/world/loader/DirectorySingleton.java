package org.terasology.codecity.world.loader;

public class DirectorySingleton {

	private static DirectorySingleton instance = null;
	String path = "";

	protected DirectorySingleton(String _path) {
		this.path = _path;
	}

	public static DirectorySingleton getInstance(String path) {
		if (instance == null) {
			instance = new DirectorySingleton(path);
		}
		return instance;
	}
	
	public String getPath(){
		return this.path;
	}
}

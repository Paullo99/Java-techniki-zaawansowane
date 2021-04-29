package loader;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class MyClassLoader extends ClassLoader{
	
	private Path directoryPath;
	
	public MyClassLoader(Path path) {
		if(!Files.isDirectory(path))
			System.out.println("Œcie¿ka musi byæ œcie¿k¹ do katalogu");
		directoryPath = path;
	}
	
	@Override
	public Class<?> findClass(String fileName) throws ClassNotFoundException{
		Path absolutePath = Paths.get(directoryPath + FileSystems.getDefault().getSeparator()
				+ fileName);
		byte[] classToLoad = null;
		try {
			classToLoad = Files.readAllBytes(absolutePath);

		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return defineClass(null, classToLoad, 0, classToLoad.length);
	}
}

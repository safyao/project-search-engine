import java.nio.file.DirectoryStream;
import java.util.List;
import java.nio.file.Files;
import java.nio.file.Path;
import java.io.IOException;
import java.util.ArrayList;

/**
 * @author Safya Osman
 *
 */
public class DirectoryTraverser
{
	public List<Path> traverseDirectory (Path directory) throws IOException
	{
		List<Path> allFiles = new ArrayList<>();

	       try (DirectoryStream<Path> stream = Files.newDirectoryStream(directory)) {
	           for (Path file: stream) {
	               if (isTextFile(file))
	               {
	            	   allFiles.add(file);
	               }
	           }
	       }
	       return allFiles;
	}
	
	public static boolean isTextFile (Path file)
	{
		String name = file.getFileName().toString().toLowerCase();
		if (name.endsWith(".txt") || name.endsWith(".text"))
		{
			return true;
		}
		return false;
	}
}

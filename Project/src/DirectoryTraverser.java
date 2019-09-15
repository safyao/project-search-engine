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
	public static List<Path> allFiles;
	
	public DirectoryTraverser()
	{
		allFiles = new ArrayList<>();
	}
	
	public static List<Path> traverseDirectory (Path directory) throws IOException
	{
	       try (DirectoryStream<Path> stream = Files.newDirectoryStream(directory)) {
	           for (Path entry: stream) {
	        	   if (Files.isDirectory(entry))
	        	   {
	        		   traverseDirectory (entry);
	        	   }
	        	   else
	        	   {
	        		   if (isTextFile(entry))
		               {
		            	   allFiles.add(entry);
		               }
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
	
//	public static void main(String[] args) throws IOException
//	{
//		System.out.println(traverseDirectory(Path.of("/Users/Safya/Desktop/CS 212/Repositories").normalize()).toString());
//	}
}

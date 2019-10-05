import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;


public class IndexBuilder {
	
	public static void buildIndex (Path path, InvertedIndex index) throws IOException
	{
		List<Path> files = DirectoryTraverser.traverseDirectory(path);
		for (Path item : files) {
			addPath (item, index);
		}
	}
	
	public static void addPath (Path path, InvertedIndex index) throws IOException
	{
		int wordCount = 0;
		int positionCount = 0;
		
		try (
				BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8);
		) {
			String line = null;
			while ((line = reader.readLine()) != null) {
				String[] parsedLine = TextParser.parse(line);
				wordCount = wordCount + parsedLine.length;
				List<String> stemmedLine = TextStemmer.uniqueStems(line);
				for (String item : stemmedLine) {
					index.add(item, path.toString(), ++positionCount);
				}
			}
			
			index.addCount(path.toString(), Integer.valueOf(wordCount));
		}
		
	}
}

package migration;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class FeatureFileConverter {
	


	    
	    public static void main(String[] args) {
	        String inputFilePath = "/Users/ghazalashahin/Documents/acc/new/src/test/resources/Features/Login.feature";
	        String outputDirectoryPath = "/Users/ghazalashahin/Documents/acc/migration/Include/features/";
	        
	        
	        try {
	            // Step 1: Read the Selenium feature file
	            File inputFile = new File(inputFilePath);
	            if (!inputFile.exists()) {
	                System.err.println("Input file does not exist: " + inputFilePath);
	                return;
	            }
	            
	            List<String> lines = Files.readAllLines(inputFile.toPath());

	            // Step 2: Extract the feature file name and prepare the output path
	            String featureFileName = inputFile.getName(); // Get the feature file name (e.g., 'example.feature')
	            Path outputFilePath = Paths.get(outputDirectoryPath, featureFileName);

	            // Step 3: Write the content to the output folder
	            Files.createDirectories(Paths.get(outputDirectoryPath)); // Ensure the output directory exists
	            Files.write(outputFilePath, lines, StandardOpenOption.CREATE); // Write the content to a new file

	            System.out.println("Feature file saved successfully to: " + outputFilePath.toString());
	        } catch (IOException e) {
	            e.printStackTrace();
	            System.err.println("Error processing the feature file.");
	        }
	    }
	}

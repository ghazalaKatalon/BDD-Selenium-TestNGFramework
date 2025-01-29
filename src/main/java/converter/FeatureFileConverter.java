package converter;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class FeatureFileConverter {
    public static void main(String[] args) {
    	 String inputDirectoryPath = System.getProperty("inputDirectoryPath");
    	 inputDirectoryPath = Paths.get(inputDirectoryPath, "src", "test","resources", "features").toString();
         String katalonDir = System.getProperty("katalonDir");

         if (inputDirectoryPath == null || katalonDir == null) {
             System.out.println("Directories not provided. Exiting program.");
             System.exit(0);
         }

       

        String outputDirectoryPath = Paths.get(katalonDir, "Include", "features").toString();

        try {
            File inputDirectory = new File(inputDirectoryPath);
            if (!inputDirectory.exists() || !inputDirectory.isDirectory()) {
                System.err.println("Input directory does not exist or is not a directory: " + inputDirectoryPath);
                return;
            }

            // Filter for .feature files
            File[] featureFiles = inputDirectory.listFiles((dir, name) -> name.endsWith(".feature"));

            if (featureFiles == null || featureFiles.length == 0) {
                System.out.println("No .feature files found in the input directory.");
                return;
            }

            // Step 2: Ensure the ProjectFolder and output directory exist
            String projectFolderPath = outputDirectoryPath + "/aib/";
            Files.createDirectories(Paths.get(projectFolderPath));

            // Step 3: Process each .feature file
            for (File featureFile : featureFiles) {
                try {
                    // Read the content of the current feature file
                    List<String> lines = Files.readAllLines(featureFile.toPath());

                    // Extract the feature file name
                    String featureFileName = featureFile.getName();

                    // Prepare the output file path
                    Path outputFilePath = Paths.get(projectFolderPath, featureFileName);

                    // Write the content to the output directory
                    Files.write(outputFilePath, lines, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);

                    System.out.println("Feature file saved successfully to: " + outputFilePath.toString());
                } catch (IOException e) {
                    System.err.println("Error processing file: " + featureFile.getName());
                    e.printStackTrace();
                }
            }

            System.out.println("All feature files have been processed successfully.");
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error processing the feature files.");
        }
    }
}

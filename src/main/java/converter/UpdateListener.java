package converter;
import java.io.*;
import java.nio.file.Paths;
import java.util.regex.*;

public class UpdateListener {
    
    public static void main(String[] args) throws IOException {
        // File paths
        String javaFilePath = "src/main/java/converter/FeatureFileConverter.java";
        String katalonDir = "/Users/ghazalashahin/Documents/AIBLatest/Demo";
        String groovyFilePath = Paths.get(katalonDir, "Test Listeners", "Listener.groovy").toString();
        
        // Read the project name from the FeatureFileConverter.java
        String projectName = extractProjectName(javaFilePath);
        
        if (projectName != null) {
            // Remove trailing slash if it exists
            projectName = projectName.replaceAll("/$", "");
            
            // Update the Listener.groovy file with the project name
            updateListenerGroovy(groovyFilePath, projectName);
            System.out.println("Listener.groovy updated with project name: " + projectName);
        } else {
            System.out.println("Project name not found.");
        }
    }
    
    // Method to extract the project name from the Java file
    public static String extractProjectName(String filePath) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        String line;
        String projectName = null;
        
        // Regex pattern to find the project folder path line
        Pattern pattern = Pattern.compile("String projectFolderPath = .*\"(.*?)\";");
        
        while ((line = reader.readLine()) != null) {
            Matcher matcher = pattern.matcher(line);
            if (matcher.find()) {
                // Extract the project name from the line
                projectName = matcher.group(1);  // The captured group for the project name
                break;
            }
        }
        
        reader.close();
        return projectName != null ? projectName : null;
    }
    
 // Method to update the Listener.groovy file with the project name
    public static void updateListenerGroovy(String groovyFilePath, String projectName) throws IOException {
        File groovyFile = new File(groovyFilePath);
        BufferedReader reader = new BufferedReader(new FileReader(groovyFile));
        StringBuilder content = new StringBuilder();
        String line;
        
        // Remove any leading or trailing slashes from projectName
        projectName = projectName.replaceAll("^/+", "").replaceAll("/+$", "");

        // Read each line and modify the CucumberKW.GLUE line with the project name
        while ((line = reader.readLine()) != null) {
            if (line.contains("CucumberKW.GLUE")) {
                // Update the line with the sanitized project name dynamically
                line = "CucumberKW.GLUE = ['common', 'operations', '" + projectName + "']";
            }
            content.append(line).append("\n");
        }
        
        reader.close();
        
        // Write the updated content back to the file
        BufferedWriter writer = new BufferedWriter(new FileWriter(groovyFile));
        writer.write(content.toString());
        writer.close();
    }

}

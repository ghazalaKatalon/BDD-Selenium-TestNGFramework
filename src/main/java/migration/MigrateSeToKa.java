package migration;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import converter.FeatureFileConverter;
import converter.stepC;
import converter.KatalonSetup;
import converter.ObjectsConverter;
import converter.TestConvertor;
import converter.UpdateListener;

public class MigrateSeToKa {

    private String inputDirectoryPath;
    private String katalonDir;

    public static void main(String[] args) {
        // Instantiate the MigrationExecutor object
        MigrateSeToKa executor = new MigrateSeToKa();
        
        // Call the method to show the UI for input and output directories
        executor.showDirectoryInputDialog();

        // After input, execute the migration logic
        executor.executeMigration();
    }

    // Method to show pop-up UI and get user input for input and output directories
    private void showDirectoryInputDialog() {
        // Prompt the user for the input directory path
        inputDirectoryPath = JOptionPane.showInputDialog(null, 
                "Please enter the Input Directory Path:", 
                "Enter Input Directory", 
                JOptionPane.PLAIN_MESSAGE);

        // If input is empty, exit
        if (inputDirectoryPath == null || inputDirectoryPath.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No Input Directory provided. Exiting program.");
            System.exit(0);
        }

        // Validate the input directory
        File inputDir = new File(inputDirectoryPath);
        if (!inputDir.exists() || !inputDir.isDirectory()) {
            JOptionPane.showMessageDialog(null, "Invalid Input Directory. Please enter a valid directory.");
            System.exit(0);
        }

        // Prompt the user for the output directory path
        katalonDir = JOptionPane.showInputDialog(null, 
                "Please enter the Output Directory Path:", 
                "Enter Output Directory", 
                JOptionPane.PLAIN_MESSAGE);

        // If output directory is empty, exit
        if (katalonDir == null || katalonDir.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No Output Directory provided. Exiting program.");
            System.exit(0);
        }

        // Validate the output directory
        File outputDir = new File(katalonDir);
        if (!outputDir.exists() || !outputDir.isDirectory()) {
            JOptionPane.showMessageDialog(null, "Invalid Output Directory. Please enter a valid directory.");
            System.exit(0);
        }

        // Display success message with the selected directories
        JOptionPane.showMessageDialog(null, 
                "Input Directory: " + inputDirectoryPath + "\nOutput Directory: " + katalonDir);
    }

    // Method to execute the migration logic
    private void executeMigration() {
        System.out.println("Executing migration with the following directories:");
        System.out.println("Input Directory: " + inputDirectoryPath);
        System.out.println("Output Directory: " + katalonDir);

        // Update directory paths for each class
        updateDirectoryPaths();

        // Execute each migration class
        executeMigrationClasses();
    }

    // Method to update directory paths in each class dynamically
    private void updateDirectoryPaths() {
        // Set system properties for both directories
        System.setProperty("inputDirectoryPath", inputDirectoryPath);
        System.setProperty("katalonDir", katalonDir);
    }

    // Method to execute the migration classes
    private void executeMigrationClasses() {
        //KatalonSetup.main(new String[]{});
		   ObjectsConverter.main(new String[]{});
		   FeatureFileConverter.main(new String[]{});
		  TestConvertor.main(new String[]{});
		   //stepC.main(new String[]{});
		  // UpdateListener.main(new String[]{});
       }
   }

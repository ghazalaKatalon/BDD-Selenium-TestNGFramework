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

public class MigrateSEToKA {

    private String inputDirectoryPath;
    private String katalonDir;
    private String projectName;

    public static void main(String[] args) {
        MigrateSEToKA executor = new MigrateSEToKA();
        executor.showDirectoryInputDialog();
        executor.executeMigration();
    }

    private void showDirectoryInputDialog() {
        inputDirectoryPath = JOptionPane.showInputDialog(null, 
                "Please enter the Input Directory Path:", 
                "Enter Input Directory", 
                JOptionPane.PLAIN_MESSAGE);

        if (inputDirectoryPath == null || inputDirectoryPath.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No Input Directory provided. Exiting program.");
            System.exit(0);
        }

        File inputDir = new File(inputDirectoryPath);
        if (!inputDir.exists() || !inputDir.isDirectory()) {
            JOptionPane.showMessageDialog(null, "Invalid Input Directory. Please enter a valid directory.");
            System.exit(0);
        }

        katalonDir = JOptionPane.showInputDialog(null, 
                "Please enter the Output Directory Path:", 
                "Enter Output Directory", 
                JOptionPane.PLAIN_MESSAGE);

        if (katalonDir == null || katalonDir.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No Output Directory provided. Exiting program.");
            System.exit(0);
        }

        File outputDir = new File(katalonDir);
        if (!outputDir.exists() || !outputDir.isDirectory()) {
            JOptionPane.showMessageDialog(null, "Invalid Output Directory. Please enter a valid directory.");
            System.exit(0);
        }

        projectName = JOptionPane.showInputDialog(null, 
                "Please enter the Project Name:", 
                "Enter Project Name", 
                JOptionPane.PLAIN_MESSAGE);

        if (projectName == null || projectName.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No Project Name provided. Exiting program.");
            System.exit(0);
        }

        JOptionPane.showMessageDialog(null, 
                "Input Directory: " + inputDirectoryPath + "\nOutput Directory: " + katalonDir + "\nProject Name: " + projectName);
    }

    private void executeMigration() {
      //  System.out.println("Executing migration with the following details:");
        System.out.println("Input Directory: " + inputDirectoryPath);
        System.out.println("Output Directory: " + katalonDir);
        System.out.println("Project Name: " + projectName);

        updateDirectoryPaths();
        executeMigrationClasses();
    }

    private void updateDirectoryPaths() {
        System.setProperty("inputDirectoryPath", inputDirectoryPath);
        System.setProperty("katalonDir", katalonDir);
        System.setProperty("projectName", projectName);
    }

    private void executeMigrationClasses() {

        KatalonSetup.main(new String[]{});
        ObjectsConverter.main(new String[]{});
       FeatureFileConverter.main(new String[]{});
       TestConvertor.main(new String[]{});
       stepC.main(new String[]{});
        try {
            UpdateListener.main(new String[]{});
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

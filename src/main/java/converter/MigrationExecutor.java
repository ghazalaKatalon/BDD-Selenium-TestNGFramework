package converter;

// Import utility classes from the migration package
import migration.KatalonSetup;
import migration.ObjectsConverter;
import migration.FeatureFileConverter;
import migration.TestConvertor;
import migration.FeatureToStepDef;

public class MigrationExecutor {

    public static void main(String[] args) {
    	
    	KatalonSetup.main(args);
    	ObjectsConverter.main(args);
    	FeatureFileConverter.main(args);
    	TestConvertor.main(args);
    	FeatureToStepDef.main(args);
     
}
}

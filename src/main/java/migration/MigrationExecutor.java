package migration;

import java.io.IOException;

import converter.FeatureFileConverter;
import converter.FeatureToStepDef;
import converter.KatalonSetup;
import converter.ObjectsConverter;
import converter.TestConvertor;
import converter.UpdateListener;


public class MigrationExecutor {

    public static void main(String[] args) {
    	
    	KatalonSetup.main(args);
    	ObjectsConverter.main(args);
    	FeatureFileConverter.main(args);
    	TestConvertor.main(args);
    	FeatureToStepDef.main(args);
    	try {
			UpdateListener.main(args);
		} catch (IOException e) {
			e.printStackTrace();
		}
    	
     
}
}

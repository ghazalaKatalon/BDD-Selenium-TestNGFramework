package migration;

import java.io.IOException;

import converter.FeatureFileConverter;
//import converter.FeatureToStepDef;
import converter.stepC;
import converter.KatalonSetup;
import converter.ObjectsConverterB;
import converter.TestConvertor;
import converter.UpdateListener;


public class MigrationExecutor {

    public static void main(String[] args) {
    	
    	KatalonSetup.main(args);
    	ObjectsConverterB.main(args);
    	FeatureFileConverter.main(args);
    	TestConvertor.main(args);
    	stepC.main(args);
    	try {
			UpdateListener.main(args);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
     
}
}

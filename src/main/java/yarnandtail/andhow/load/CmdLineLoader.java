package yarnandtail.andhow.load;

import yarnandtail.andhow.LoaderException;
import java.util.ArrayList;
import java.util.List;
import yarnandtail.andhow.AppConfig;
import yarnandtail.andhow.AppConfigStructuredValues;
import yarnandtail.andhow.LoaderValues;
import yarnandtail.andhow.PointValue;
import yarnandtail.andhow.appconfig.AppConfigDefinition;
//import yarnandtail.andhow.*;

/**
 *
 * @author eeverman
 */
public class CmdLineLoader extends BaseLoader {
	
	public CmdLineLoader() {
	}
	
	@Override
	public LoaderValues load(AppConfigDefinition appConfigDef, List<String> cmdLineArgs,
			AppConfigStructuredValues existingValues, List<LoaderException> loaderExceptions) {
		
		ArrayList<PointValue> values = new ArrayList();
			
		if (cmdLineArgs != null) {
			for (String s : cmdLineArgs) {
				try {
					KVP kvp = KVP.splitKVP(s, AppConfig.KVP_DELIMITER);

					attemptToAdd(appConfigDef, values, kvp.getName(), kvp.getValue());

				} catch (ParsingException e) {
					loaderExceptions.add(
							new LoaderException(e, this, null, "Command line parameters")
					);
				}
			}

			values.trimToSize();
		}
		
		return new LoaderValues(this, values);
	}
	
	
	
}

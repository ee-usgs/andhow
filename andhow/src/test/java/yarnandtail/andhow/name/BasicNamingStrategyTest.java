package yarnandtail.andhow.name;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import yarnandtail.andhow.PropertyNaming;
import yarnandtail.andhow.property.StrProp;
import yarnandtail.andhow.SimpleParams;

/**
 *
 * @author eeverman
 */
public class BasicNamingStrategyTest {

	//Using SimpleParams as an arbitrary group to use for naming
	final String groupFullPath = SimpleParams.class.getCanonicalName();
		
	//Stateless, so ok to have a single instance
	final BasicNamingStrategy bns = new BasicNamingStrategy();
	
	@Test
	public void testDefaultNaming() {

		StrProp point = StrProp.builder().build();
		PropertyNaming naming = bns.buildNames(point, SimpleParams.class, "BOB");
		
		assertEquals(groupFullPath + ".BOB", naming.getCanonicalName());
		assertEquals(0, naming.getInAliases().size());
	}

}

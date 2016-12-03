package yarnandtail.andhow.name;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.junit.Test;
import yarnandtail.andhow.ConfigPointType;
import yarnandtail.andhow.NamingStrategy.Naming;
import yarnandtail.andhow.point.StringConfigPoint;
import yarnandtail.andhow.valuetype.StringType;
import yarnandtail.andhow.SimpleParamsWAlias;
import yarnandtail.andhow.point.StringPointBuilder;

/**
 *
 * @author eeverman
 */
public class BasicNamingStrategyTest {

	//Using SimpleParamsWAlias as an arbitrary group to use for naming
	final String groupFullPath = SimpleParamsWAlias.class.getCanonicalName();
		
	//Stateless, so ok to have a single instance
	final BasicNamingStrategy bns = new BasicNamingStrategy();
	
	@Test
	public void testDefaultNaming() {

		StringConfigPoint point = new StringConfigPoint();
		Naming naming = bns.buildNames(point, SimpleParamsWAlias.class, "BOB");
		
		assertEquals(groupFullPath + ".BOB", naming.getCanonicalName());
		assertEquals(0, naming.getAliases().size());
	}
	
	@Test
	public void testExplicitNaming() {

		StringConfigPoint point = StringPointBuilder.init().addAlias("myName").build();
		Naming naming = bns.buildNames(point, SimpleParamsWAlias.class, "BOB");
		
		assertEquals(groupFullPath + ".BOB", naming.getCanonicalName());
		assertEquals(1, naming.getAliases().size());
		assertEquals(groupFullPath + ".myName", naming.getAliases().get(0));
	}
	
	@Test
	public void testExplicitNamingWithAliases() {

		StringConfigPoint point = new StringConfigPoint(
				null, false, null, null, ConfigPointType.SINGLE_NAME_VALUE, StringType.instance(), 
				null,  new String[] {"name1", "name2", "name3"});
		
		Naming naming = bns.buildNames(point, SimpleParamsWAlias.class, "BOB");
		
		assertEquals(groupFullPath + ".BOB", naming.getCanonicalName());
		assertEquals(3, naming.getAliases().size());
		assertEquals(groupFullPath + ".name1", naming.getAliases().get(0));
		assertEquals(groupFullPath + ".name2", naming.getAliases().get(1));
		assertEquals(groupFullPath + ".name3", naming.getAliases().get(2));
	}
}

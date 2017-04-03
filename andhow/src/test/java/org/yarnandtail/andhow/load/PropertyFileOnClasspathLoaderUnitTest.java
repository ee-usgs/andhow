package org.yarnandtail.andhow.load;

import java.util.*;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.yarnandtail.andhow.SimpleParams;
import org.yarnandtail.andhow.api.*;
import org.yarnandtail.andhow.internal.ConstructionDefinitionMutable;
import org.yarnandtail.andhow.internal.LoaderProblem;
import org.yarnandtail.andhow.internal.ValueMapWithContextMutable;
import org.yarnandtail.andhow.name.CaseInsensitiveNaming;
import org.yarnandtail.andhow.property.StrProp;
import org.yarnandtail.andhow.PropertyGroup;

/**
 *
 * @author eeverman
 */
public class PropertyFileOnClasspathLoaderUnitTest {
	
	ConstructionDefinitionMutable appDef;
	ValueMapWithContextMutable appValuesBuilder;
	
	public static interface TestProps extends PropertyGroup {
		StrProp CLAZZ_PATH = StrProp.builder().required().build();
	}
	
	@Before
	public void init() throws Exception {
		
		appValuesBuilder = new ValueMapWithContextMutable();
		CaseInsensitiveNaming bns = new CaseInsensitiveNaming();
		
		appDef = new ConstructionDefinitionMutable(bns);
		
		appDef.addProperty(TestProps.class, TestProps.CLAZZ_PATH);

		
		appDef.addProperty(SimpleParams.class, SimpleParams.STR_BOB);
		appDef.addProperty(SimpleParams.class, SimpleParams.STR_NULL);
		appDef.addProperty(SimpleParams.class, SimpleParams.FLAG_FALSE);
		appDef.addProperty(SimpleParams.class, SimpleParams.FLAG_TRUE);
		appDef.addProperty(SimpleParams.class, SimpleParams.FLAG_NULL);

	}
	
	@Test
	public void testHappyPath() {
		
		ArrayList<PropertyValue> evl = new ArrayList();
		evl.add(new PropertyValue(TestProps.CLAZZ_PATH, "/org/yarnandtail/andhow/load/SimpleParams1.properties"));
		LoaderValues existing = new LoaderValues(new StringArgumentLoader(new String[]{}), evl, new ProblemList<Problem>());
		appValuesBuilder.addValues(existing);
		
		PropertyFileOnClasspathLoader pfl = new PropertyFileOnClasspathLoader(TestProps.CLAZZ_PATH);
		
		LoaderValues result = pfl.load(appDef, appValuesBuilder);
		
		assertEquals(0, result.getProblems().size());
		assertEquals(0L, result.getValues().stream().filter(p -> p.hasProblems()).count());
		
		assertEquals("kvpBobValue", result.getExplicitValue(SimpleParams.STR_BOB));
		assertEquals("kvpNullValue", result.getExplicitValue(SimpleParams.STR_NULL));
		assertEquals(Boolean.FALSE, result.getExplicitValue(SimpleParams.FLAG_TRUE));
		assertEquals(Boolean.TRUE, result.getExplicitValue(SimpleParams.FLAG_FALSE));
		assertEquals(Boolean.TRUE, result.getExplicitValue(SimpleParams.FLAG_NULL));
	}

	
	@Test
	public void testDuplicateEntries() {
		
		ArrayList<PropertyValue> evl = new ArrayList();
		evl.add(new PropertyValue(TestProps.CLAZZ_PATH, "/org/yarnandtail/andhow/load/SimpleParams2.properties"));
		LoaderValues existing = new LoaderValues(new StringArgumentLoader(new String[]{}), evl, new ProblemList<Problem>());
		appValuesBuilder.addValues(existing);
		
		PropertyFileOnClasspathLoader pfl = new PropertyFileOnClasspathLoader(TestProps.CLAZZ_PATH);
		
		LoaderValues result = pfl.load(appDef, appValuesBuilder);
		
		assertEquals(0, result.getProblems().size());
		assertEquals(0L, result.getValues().stream().filter(p -> p.hasProblems()).count());
		
		assertEquals("kvpBobValue", result.getExplicitValue(SimpleParams.STR_BOB));
		assertEquals("3", result.getExplicitValue(SimpleParams.STR_NULL));
		assertEquals(Boolean.FALSE, result.getExplicitValue(SimpleParams.FLAG_TRUE));
		assertEquals(Boolean.TRUE, result.getExplicitValue(SimpleParams.FLAG_FALSE));
		assertEquals(Boolean.TRUE, result.getExplicitValue(SimpleParams.FLAG_NULL));
	}
	
	@Test
	public void testEmptyValues() {
		
		ArrayList<PropertyValue> evl = new ArrayList();
		evl.add(new PropertyValue(TestProps.CLAZZ_PATH, "/org/yarnandtail/andhow/load/SimpleParams3.properties"));
		LoaderValues existing = new LoaderValues(new StringArgumentLoader(new String[]{}), evl, new ProblemList<Problem>());
		appValuesBuilder.addValues(existing);
		
		PropertyFileOnClasspathLoader pfl = new PropertyFileOnClasspathLoader(TestProps.CLAZZ_PATH);
		
		LoaderValues result = pfl.load(appDef, appValuesBuilder);
		
		assertEquals(0, result.getProblems().size());
		assertEquals(0L, result.getValues().stream().filter(p -> p.hasProblems()).count());
		
		assertNull(result.getExplicitValue(SimpleParams.STR_BOB));
		assertEquals("bob", result.getEffectiveValue(SimpleParams.STR_BOB));
		assertNull(result.getExplicitValue(SimpleParams.STR_NULL));
		assertEquals(Boolean.TRUE, result.getExplicitValue(SimpleParams.FLAG_TRUE));
		assertEquals(Boolean.TRUE, result.getExplicitValue(SimpleParams.FLAG_FALSE));
		assertEquals(Boolean.TRUE, result.getExplicitValue(SimpleParams.FLAG_NULL));
	}
	
	@Test
	public void testAllWhitespaceValues() {
		
		ArrayList<PropertyValue> evl = new ArrayList();
		evl.add(new PropertyValue(TestProps.CLAZZ_PATH, "/org/yarnandtail/andhow/load/SimpleParams4.properties"));
		LoaderValues existing = new LoaderValues(new StringArgumentLoader(new String[]{}), evl, new ProblemList<Problem>());
		appValuesBuilder.addValues(existing);
		
		PropertyFileOnClasspathLoader pfl = new PropertyFileOnClasspathLoader(TestProps.CLAZZ_PATH);
		
		LoaderValues result = pfl.load(appDef, appValuesBuilder);
		
		assertEquals(0, result.getProblems().size());
		assertEquals(0L, result.getValues().stream().filter(p -> p.hasProblems()).count());
		
		assertNull(result.getExplicitValue(SimpleParams.STR_BOB));
		assertEquals("bob", result.getEffectiveValue(SimpleParams.STR_BOB));
		assertNull(result.getExplicitValue(SimpleParams.STR_NULL));
		assertEquals(Boolean.TRUE, result.getExplicitValue(SimpleParams.FLAG_TRUE));
		assertEquals(Boolean.TRUE, result.getExplicitValue(SimpleParams.FLAG_FALSE));
		assertEquals(Boolean.TRUE, result.getExplicitValue(SimpleParams.FLAG_NULL));
	}
	
	@Test
	public void testQuotedStringValues() {
		
		ArrayList<PropertyValue> evl = new ArrayList();
		evl.add(new PropertyValue(TestProps.CLAZZ_PATH, "/org/yarnandtail/andhow/load/SimpleParams5.properties"));
		LoaderValues existing = new LoaderValues(new StringArgumentLoader(new String[]{}), evl, new ProblemList<Problem>());
		appValuesBuilder.addValues(existing);
		
		PropertyFileOnClasspathLoader pfl = new PropertyFileOnClasspathLoader(TestProps.CLAZZ_PATH);
		
		LoaderValues result = pfl.load(appDef, appValuesBuilder);
		
		assertEquals(0, result.getProblems().size());
		assertEquals(0L, result.getValues().stream().filter(p -> p.hasProblems()).count());
		
		assertEquals("  two_spaces_&_two_tabs\t\t", result.getExplicitValue(SimpleParams.STR_BOB));
		assertEquals("  two_spaces_&_two_tabs\t\t", result.getEffectiveValue(SimpleParams.STR_BOB));
		assertEquals("", result.getExplicitValue(SimpleParams.STR_NULL));
		assertEquals("", result.getEffectiveValue(SimpleParams.STR_NULL));
	}
	
	@Test
	public void testPropFileLoaderWithUnrecognizedPropNames() {
		
		ArrayList<PropertyValue> evl = new ArrayList();
		evl.add(new PropertyValue(TestProps.CLAZZ_PATH, "/org/yarnandtail/andhow/load/SimpleParams6.properties"));
		LoaderValues existing = new LoaderValues(new StringArgumentLoader(new String[]{}), evl, new ProblemList<Problem>());
		appValuesBuilder.addValues(existing);
		
		PropertyFileOnClasspathLoader pfl = new PropertyFileOnClasspathLoader(TestProps.CLAZZ_PATH);
		
		LoaderValues result = pfl.load(appDef, appValuesBuilder);
		
		//These are the two bad property names in the file
		List<String> badPropNames = Arrays.asList(new String[] {
			"org.yarnandtail.andhow.SimpleParams.XXX",
			"org.yarnandtail.andhow.SimpleXXXXXX.STR_BOB"});
		
		assertEquals(2, result.getProblems().size());
		for (Problem lp : result.getProblems()) {
			assertTrue(lp instanceof LoaderProblem.UnknownPropertyLoaderProblem);
			
			LoaderProblem.UnknownPropertyLoaderProblem uplp = (LoaderProblem.UnknownPropertyLoaderProblem)lp;
			
			//The problem description should contain one of the bad names
			assertTrue(
				badPropNames.stream().anyMatch(n -> uplp.getProblemDescription().contains(n)));
		}
		
		assertEquals(0L, result.getValues().stream().filter(p -> p.hasProblems()).count());
		
	}
	
	@Test
	public void testPropFileLoaderWithMissingFile() {
		
		ArrayList<PropertyValue> evl = new ArrayList();
		evl.add(new PropertyValue(TestProps.CLAZZ_PATH, "/org/yarnandtail/andhow/load/XXXXXXX.properties"));
		LoaderValues existing = new LoaderValues(new StringArgumentLoader(new String[]{}), evl, new ProblemList<Problem>());
		appValuesBuilder.addValues(existing);
		
		PropertyFileOnClasspathLoader pfl = new PropertyFileOnClasspathLoader(TestProps.CLAZZ_PATH);
		
		LoaderValues result = pfl.load(appDef, appValuesBuilder);
		
		assertEquals(1, result.getProblems().size());
		for (Problem lp : result.getProblems()) {
			assertTrue(lp instanceof LoaderProblem.SourceNotFoundLoaderProblem);
		}
		
		assertEquals(0L, result.getValues().stream().filter(p -> p.hasProblems()).count());
		
	}
	
	/**
	 * The loader itself is OK w/ not having its parameter specified - it just
	 * ignores.
	 */
	@Test
	public void testPropFileLoaderWithNoClasspathConfigured() {
		
		ArrayList<PropertyValue> evl = new ArrayList();
		//evl.add(new PropertyValue(TestProps.CLAZZ_PATH, "/org/yarnandtail/andhow/load/XXXXXXX.properties"));
		LoaderValues existing = new LoaderValues(new StringArgumentLoader(new String[]{}), evl, new ProblemList<Problem>());
		appValuesBuilder.addValues(existing);
		
		PropertyFileOnClasspathLoader pfl = new PropertyFileOnClasspathLoader(TestProps.CLAZZ_PATH);
		
		LoaderValues result = pfl.load(appDef, appValuesBuilder);
		
		assertEquals(0, result.getProblems().size());
	}

}

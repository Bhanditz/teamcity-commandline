package com.jetbrains.teamcity;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.text.MessageFormat;
import java.util.Collection;

import jetbrains.buildServer.util.FileUtil;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class UtilTest {
	
	private static File ourTestFs;
	
	@BeforeClass
	public static void setup() throws Exception {
		ourTestFs = TestingUtil.createFS();
	}
	
	@AfterClass
	public static void clean() throws Exception {
		TestingUtil.releaseFS(ourTestFs);
	}
	
	@Test
	public void showVersion() {
		System.out.println(String.format("Testing %s build...", Build.build));
	}
	
	@Test
	public void hasArgument() throws Exception {
		assertTrue(Util.hasArgument(new String[] {"-one", "-two"}, "-one"));
		assertTrue(Util.hasArgument(new String[] {" -one ", "-two"}, " -one "));
		assertTrue(Util.hasArgument(new String[] {" -One "}, "-one "));
		assertTrue(Util.hasArgument(new String[] {"-one", "-two"}, "-one", "-two"));
		assertTrue(Util.hasArgument(new String[] {"-one", "-two"}, "-one", "-two", "-three"));
		assertFalse(Util.hasArgument(new String[] {"-one", "-two"}, "-tree"));
		assertFalse(Util.hasArgument(new String[] {"-one", "-two"}, (String)null));
		assertTrue(Util.hasArgument(new String[] {"-one", "-two"}, null, "-one"));
	}
	
	@Test
	public void getArgument() throws Exception {
		assertEquals("one", Util.getArgumentValue(new String[] {"-one", "one", "-two", "two"}, "-one"));
		assertEquals("one", Util.getArgumentValue(new String[] {" -one", " one ", "-two", "two"}, "-one "));
		assertEquals("one", Util.getArgumentValue(new String[] {" -one", " one ", "-two", "two"}, null, "-One"));
		assertEquals(null, Util.getArgumentValue(new String[] {" -one", " one ", "-two", "two"}, (String)null));
	}
	
	@Test
	public void getRelativePath() throws Exception {
		try{
			assertEquals(null, Util.getRelativePath(null, null));
			fail("IllegalArgumentException expected");
		} catch (IllegalArgumentException e){
			//true way
		}
		
		final File[] roots = File.listRoots();
		File rootFS = null;
		for(File fs : roots){
			if(fs.canRead()){
				rootFS = fs;
			}
		}
		
		File root =  new File(rootFS, "1");
		File child = new File(rootFS, "1/1/1.java");
		assertEquals("1/1.java", Util.getRelativePath(root, child));
		
		//extra slash
		root =  new File(rootFS, "1/");
		assertEquals("1/1.java", Util.getRelativePath(root, child));
		
		//path is not absolute
		root =  new File(rootFS, "1");
		child = new File("1/1/1.java");
		assertEquals("1/1/1.java", Util.getRelativePath(root, child));
		
		//path is not absolute
		root =  new File(rootFS, "1");
		child = new File("../1/1.java");
		assertEquals("../1/1.java", Util.getRelativePath(root, child));
		
	}

	@Test
	public void getFiles_simple() throws Exception {
		assertNotNull(Util.getFiles(MessageFormat.format("{0}{1}{2}{3}", "rootTestFolder", File.separator, "java", File.separator, "1.java")));
	}
	
	@Test
	public void getFiles_all() throws Exception {
		final String pattern = MessageFormat.format("{0}{1}", "rootTestFolder", File.separator);
		final Collection<File> files = Util.getFiles(pattern);
		assertEquals(pattern, 5, files.size());
	}

//	@Test
//	public void getFiles_pattern() throws Exception {
//		final String pattern = MessageFormat.format("{0}{1}{2}", "rootTestFolder", File.separator, "**.java");
//		final Collection<File> files = Util.getFiles(pattern);
//		assertEquals(pattern, 2, files.size());
//	}
	
	@Test(expected=IllegalArgumentException.class)
	public void getFiles_file_empty() throws Exception {
		final File contentFile = new File("@files");
		contentFile.createNewFile();
		try{
			Util.getFiles(contentFile);
		} finally {
			FileUtil.delete(contentFile);
		}
	}
	
	@Test
	public void getFiles_file_files() throws Exception {
		final File contentFile = new File("@files");
		contentFile.createNewFile();
		try{
			final String content = MessageFormat.format("{0}{1}{2}{3}{4}", "rootTestFolder", File.separator, "java", File.separator, "1.java") + "\n" +
				MessageFormat.format("{0}{1}{2}{3}{4}{5}{6}", "rootTestFolder", File.separator, "java", File.separator, "resources", File.separator, "1.resources");

          FileUtil.writeFileAndReportErrors(contentFile, content);

          final Collection<File> files = Util.getFiles(contentFile);
			assertEquals(2, files.size());
			
		} finally {
			FileUtil.delete(contentFile);
		}
	}
//	p4 -p rusps-app01:1666 -u kdonskov -P cisak21 -c konstantin.donskov fstat -T "path" -P -Oc -e 12 -W //konstantin.donskov/...
	
	@Test
	public void getFiles_file_folder() throws Exception {
		final File contentFile = new File("@files");
		contentFile.createNewFile();
		try{
			final String content = MessageFormat.format("{0}{1}{2}", "rootTestFolder", File.separator, "java");

          FileUtil.writeFileAndReportErrors(contentFile, content);

          final Collection<File> files = Util.getFiles(contentFile);
			assertEquals(3, files.size());
			
		} finally {
			FileUtil.delete(contentFile);
		}
	}
	
	@Test
	public void trim() throws Exception {
		{
			final String out = Util.trim(null);
			assertNull(out);
		}
		{
			final String out = Util.trim(null, (String)null);
			assertNull(out);
		}
		{
			final String out = Util.trim("string");
			assertEquals("string", out);
		}
		{
			final String out = Util.trim(" string ");
			assertEquals("string", out);
		}
		{
			final String out = Util.trim("", "long token");
			assertEquals("", out);
		}
		{
			final String out = Util.trim("string", "not a token");
			assertEquals("string", out);
		}
		{
			final String out = Util.trim("string\\", "\\");
			assertEquals("string", out);
		}
		{
			final String out = Util.trim("string\\/", "/", "\\");
			assertEquals("string", out);
		}
		{
			final String out = Util.trim("string\\/\"", "\"", "/", "\\");
			assertEquals("string", out);
		}
		{
			final String out = Util.trim("string   \\'/\"", "'", "\"", "/", "\\");
			assertEquals("string", out);
		}
		
		

	}


//	@Test
//	public void getFiles_file_pattern() throws Exception {
//		final File contentFile = new File("@files");
//		contentFile.createNewFile();
//		try{
//			final String content = MessageFormat.format("{0}{1}{2}", "rootTestFolder", File.separator, "**.java");
//			
//			FileUtil.writeFile(contentFile, content);
//			
//			final Collection<File> files = Util.getFiles(contentFile);
//			assertEquals(2, files.size());
//			
//		} finally {
//			FileUtil.delete(contentFile);
//		}
//	}
	

}

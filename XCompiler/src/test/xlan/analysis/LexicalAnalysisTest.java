package test.xlan.analysis;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.LinkedList;

import org.junit.Before;
import org.junit.Test;




import com.xlan.analysis.*;
import com.xlan.expections.LexicalAnalysisException;

public class LexicalAnalysisTest {
	String samplePath="D:/OneDrive/GeekProjects/XCompiler/sample codes/";
	String[] fileNames= new String[] {"helloworld","if_else","forLoop"};


	//LinkedList<Token> helloWordTokens;
	LinkedList<LexicalAnalysis> analysisList;
	@Before
	public void setUp() throws Exception {
		analysisList =new LinkedList<LexicalAnalysis>();
		for(String fileName:fileNames){
			FileReader fileReader= new FileReader(samplePath+fileName+".v");
			LexicalAnalysis lexicalAnalysis=new LexicalAnalysis(fileReader);
			analysisList.add(lexicalAnalysis);
		}
	}



	@Test
	public void test_if(){
		testCodeFile(0);
	}

	@Test
	public void test_for_loop(){
		testCodeFile(1);
	}

	@Test
	public void testHello(){
		testCodeFile(2);
	}


	private void testCodeFile(int index){
		System.out.println("----------------");
		try{
			LexicalAnalysis lexicalAnalysis=analysisList.get(index);
			LinkedList<Token> tokenList=lexicalAnalysis.read();
			System.out.println(tokenList);

		}catch(LexicalAnalysisException e){
			e.printStackTrace();
			fail("LexicalAnalysisException");
		}catch(Exception e){
			fail("Exception");
		}
	}


	// @Test
	// public void test_all() {
	// 	try{
	// 		for(LexicalAnalysis lexicalAnalysis:analysisList){
	// 			LinkedList<Token> tokenList=lexicalAnalysis.read();		
	// 			System.out.println(tokenList);
	// 		}

	// 	}catch(LexicalAnalysisException e){
	// 		e.printStackTrace();
	// 		fail("LexicalAnalysisException");
	// 	}catch(Exception e){
	// 		fail("Exception");
	// 	}

	// }

}

package test.xlan.analysis;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.xlan.analysis.SignParser;
import com.xlan.expections.LexicalAnalysisException;


public class SignParserTest {
	String[] signArray;
	@Before
	public void setUp() throws Exception {
		signArray = new String[] {
				"+", "-", "*", "/", "%", 
				">", "<", ">=", "<=", "=", "!=", "==", "=~","++","--",
				"+=", "-=", "*=", "/=", "%=",
				"&&", "||", "!", "^",
				"&&=", "||=", "^=",
				"<<", ">>", "->", "<-",
				"?", ":",
				".", ",", ";", "..",
				"(", ")", "[", "]", "{", "}",
				"@", "@@", "$",
			};
	}

	@Test
	public void test_parse() {
		
		for(String str:signArray){
			List<String> rst;
			try {
				rst=SignParser.parse(str);
				System.out.println("str="+str+"  rst="+rst);
			} catch (LexicalAnalysisException e) {
				e.printStackTrace();
			}
			
		}
	}

}

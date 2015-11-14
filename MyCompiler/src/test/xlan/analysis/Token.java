package analysis;

import java.util.*;	

public class Token {

private static final HashSet<String> keywordsSet = new HashSet<String> ();
	static {
		keywordsSet.add("if");
		keywordsSet.add("when");
		keywordsSet.add("elsif");
		keywordsSet.add("else");
		keywordsSet.add("while");
		keywordsSet.add("begin");
		keywordsSet.add("until");
		keywordsSet.add("for");
		keywordsSet.add("do");
		keywordsSet.add("try");
		keywordsSet.add("catch");
		keywordsSet.add("finally");
		keywordsSet.add("end");
		keywordsSet.add("def");
		keywordsSet.add("var");
		keywordsSet.add("this");
		keywordsSet.add("null");
		keywordsSet.add("throw");
		keywordsSet.add("break");
		keywordsSet.add("continue");
		keywordsSet.add("return");
		keywordsSet.add("operator");
	}

	public static enum Type {
		Keyword, Number, Identifier, Sign, Annotation,
		String, RegEx, Space, NewLine, EndSymbol;
	}
	final Type type;
	final String value;

	Token(Type type, String value) {
		if(type == Type.Identifier) {
			char firstChar = value.charAt(0);
			if(firstChar >= '0' & firstChar < '9') {
				type = Type.Number;
			} else if(keywordsSet.contains(value)){
				type = Type.Keyword;
			}
		}else if(type==Type.Annotation){
			value=value.substring(1);       // #this is Annotation
		}else if(type==Type.String){
			value=value.substring(1,value.length()-1); // "this is string"
		}else if(type==Type.RegEx){
			value=value.substring(1,value.length()-1); // '!notARegEx'
		}else if(type==Type.EndSymbol){
			value=null;
		}

		this.type = type;
		this.value = value;
	}
}
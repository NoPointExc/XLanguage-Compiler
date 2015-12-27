/*
TODO:
 Unit Test:F
 */
package com.xlan.analysis;

import com.xlan.expections.LexicalAnalysisException;
import java.util.*;



public class SignParser {

	private final static List<HashSet<String>> signSetList; //list of hashset, contains hashset for sign in certain length
	private final static HashSet<Character> signCharSet; //all char, like '+' ,'&','<','='
	private final static int MaxLength, MinLength;
	
	static {
			String[] signArray = new String[] {
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

		int maxLength = Integer.MIN_VALUE;
		int minLength = Integer.MAX_VALUE;
		signCharSet = new HashSet<>();
		for(String sign:signArray){
			int length=sign.length();
			//get min and max length of signs
			if(length>maxLength){
				maxLength=length;
			}
			if(length<minLength){
				minLength=length;
			}

			for(int i=0;i<length;i++){
				signCharSet.add(sign.charAt(i));
			}
		}

		signSetList =new ArrayList<>(maxLength-minLength+1);
		
		for(int i=0;i<maxLength-minLength+1;i++){
			signSetList.add(new HashSet<>());
		}
		
		for(String sign: signArray){
			int length=sign.length();
			HashSet<String> signSet=signSetList.get(length-minLength);
			signSet.add(sign);
		}
		MaxLength=maxLength;
		MinLength=minLength;
	}

	/*
	Wether char c is a part of a Sign
	such as '&' in "&&"
	 */
	static boolean inCharSet(char c){
		//String str=Character.toString(c);
		return signCharSet.contains(c);	
	}

	/*
	return list<String> of token value
	 */
	public static List<String> parse(String str) throws LexicalAnalysisException{
		int strLen=str.length();
		List<String> rst=new ArrayList<String>(strLen);
		StringBuilder stringBuffer=null;
		
		int i=0;
		boolean moveCursor=true;
		boolean hasMatched=false;
		int buffLen=0;
		while(i<strLen){
				
			char c=str.charAt(i);
			if(stringBuffer==null) stringBuffer=new StringBuilder();
			stringBuffer.append(c);
			buffLen=stringBuffer.length();
			moveCursor=true;

			if(buffLen>MaxLength){
				if(hasMatched){
				String matchedStr=stringBuffer.substring(0,buffLen-1);
				rst.add(matchedStr);
				stringBuffer=null;
				moveCursor=false;
				hasMatched=false;						
				}
				else {
					throw new LexicalAnalysisException();
				}
			}

			if(match(stringBuffer.toString())){
				hasMatched=true;				
			}else if(hasMatched){
				String matchedStr=stringBuffer.substring(0,buffLen-1);
				rst.add(matchedStr);
				stringBuffer=null;
				moveCursor=false;
				hasMatched=false;							
			}

			if(moveCursor) i++;
		}

		if(hasMatched) rst.add(stringBuffer.toString());
		
		return rst;
	}
	/*
	str matches a sign in map 
	 */
	private static boolean match(String str){
		int strLen=str.length();
		if(strLen<MinLength) return false;
		//get the signSet for length=strLen 
		HashSet<String> signSet=signSetList.get(strLen-MinLength); 
		return signSet.contains(str);
	}
}
/*
TODO:
 Unit Test:F
 */

package analysis;
class SignParser {

	private final static List<HashSet<String>> signSetList; //list of hashset, contains hashset for sign in certain length
	private final static HashSet<Character> signCharSet; //all char, like '+' ,'&','<','='
	private final static int MaxLength, MinLength;
	
	static {
			String[] signArray = new String[] {
				"+", "-", "*", "/", "%", 
				">", "<", ">=", "<=", "=", "!=", "==", "=~",
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

		signSetList =new ArrayList<>(maxLength-minLenght+1);
		for(int i=0;i<maxLength-minLenght;i++){
			signSetList.add(new HashSet<>());
		}

		for(String sign: signArray){
			int length=sign.length();
			HashSet<String> signSet=signSetList.get(length);
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
		//TODO
		return signCharSet.contains(c);	
	}

	/*
	return list<String> of token value
	 */
	public static List<String> parse(String str) throws LexicalAnalysisException{
		int strLen=str.length();
		List<String> rst=new ArrayList<String>(strLen);
		StringBuilder stringBuffer=null;
		for(int i=0;i<strLen;i++){
			char c=str.charAt(i);
			if(stringBuffer==null) stringBuffer=new StringBuilder();
			stringBuffer.append(c);
			if(stringBuffer.length()>MaxLength) throws LexicalAnalysisException;						
			if(match(stringBuffer.toString())){
				//found sign, add to rst list
				rst.add(StringBuilder.toString());
				stringBuffer=null;
			}
		}
		return rst;
	}
	private static boolean match(String str){
		int strLen=str.length();
		if(str<MinLength) return false;
		//get the signSet for length=strLen 
		HashSet<String> signSet=signSetList.get(strLen-MinLength); 
		return signSet.contains(str);
	}
}
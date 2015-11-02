package com.sun.taolan.analysis;


import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.LinkedList;
import com.sun.taolan.analysis.Token.Type;

public class LexicalAnalysis{

	private State state;
	private boolean transferredMeaningSign=false;
	private final LinkedList <Token> tokenBuff=new LinkedList <Token>();
	private StringBuilder readBuff=null;

	private static final char
	private static final char[] IdentifierRearSign =new char[] {'?','/'};


	private static final HashMap<Character, Character> StringTMMap = new HashMap<>();

	static {
		StringTMMap.put('\"', '\"');
		StringTMMap.put('\'', '\'');
		StringTMMap.put('\\', '\\');
		StringTMMap.put('b', '\b');
		StringTMMap.put('f', '\f');
		StringTMMap.put('t', '\t');
		StringTMMap.put('r', '\r');
		StringTMMap.put('n', '\n');
		StringTMMap.put('1','\1'); // :)
	}
	private static enum State{
		Normal,Identifier, Sign, Annotation,String, RegEx, Space;
	}

	public LexicalAnalysis(Reader reader){

	}

	public Token read() throws IOExpection, LexicalAnalysisException{
		//Token token=new Token();
		return null;
	}


	private void refreshBuff(char c){
		readBuff=new StrinbBuilder(c);
	}



	private void createToken(Type type) {
		Token token = new Token(type, readBuffer.toString());
		tokenBuffer.addFirst(token);
		readBuffer = null;
	}
	

	//Finiate State Machine
	private boolean readChar(char c) throws LexicalAnalysisException {
		boolean moveCursor=true;
		Type createType=null;
		switch(state){
			case State.Normal: 
				if(inIdentifierSetButNotRear(c)) {
					state = State.Identifier;
				}
				else if(SignParser.inCharSet(c)) {
					state = State.Sign;
				}
				else if(c == '#') {
					state = State.Annotation;
				}
				else if(c == '\"') {
					state = State.String;
				}
				else if(c == '\'') {
					state = State.RegEx;
				}
				else if(include(Space, c)) {
					state = State.Space;
				}
				else if(c == '\n') {
					createType = Type.NewLine;
				}
				else if(c == '\0') {
					createType = Type.EndSymbol;
				}
				else {
					throw new LexicalAnalysisException(c);
				}
				refreshBuffer(c);
				break;
			
			case State.Identifier: 
				if(inIdentifierSetButNotRear(c)){
					readBuffer.append(c);
				}else if(include(IdentifierRearSign,c)){
					createType=Type.Identifier;
					readBuff.append(c);
					state=State.Normal;
				}else{
					//not identifier char
					createType=Type.Identifier;
					state=State.Normal;
					moveCursor=false;
				}

				break;
			case State.Sign: 
				createType=Type.Sign;
				state=State.Normal;		
				readBuff.append(c);
				break;
			case State.Annotation: 
				if(c=='\n' || c=='\0'){
					createType=Type.Annotation;
					state=State.Normal;	
					moveCursor=false;				
				}else{
					readBuff.append(c);
				}
				break;
			case State.String: 
				if(c=='\"'){
					createType=Type.String;
					state=State.Normal;
					readBuff.append(c);
				}else if(c=='\\'){
					transferredMeaningSign=true;
				}else if(transferredMeaningSign){
					char tmpSign=StringTMMap.get(c);
					if(tmpSign==null){
						throw new LexicalAnalysisException(c);
					}
					readBuff.append(tmpSign);
					transferredMeaningSign=false;
				}else if(c=='\n' || c=='\0'){
					throw new LexicalAnalysisException(c);
				}else{
					readBuff.append(c);
				}

				break;
			case State.RegEx: 
				if(c=='\\'){
					transferredMeaningSign=true;
				}else if(c=='\0'){
					throw new LexicalAnalysisException(c);
				}else if(transferredMeaningSign){
					if(c!='\'') throw new LexicalAnalysisException(c);
					readBuff.append(c);
					transferredMeaningSign=false;
				}else if('\''){
					readBuff.append(c);
					createType=Type.RegEx;
					state=Normal;
				}else{
					readBuff.append(c);
				}

				break;
			case State.Space: 
				if(include(Space,c)){
					readBuff.append(c);
				}else{
					createType=Type.Space;
					state=State.Normal;
					moveCursor=false;
				}
				break;
		}

		if(createType!=null) {
			createToken(createType);
		}

		return moveCursor;
	}


	

	private boolean inIdentifierSetButNotRear(char c) {
		return (c >= 'a' & c <= 'z' ) | (c >='A' & c <= 'Z') | (c >= '0' & c <= '9')|| (c == '_');
	}

	private boolean include(char[] range, char c) {
		boolean include = false;
		for(int i=0; i<range.length; ++i) {
			if(range[i] == c) {
				include = true;
				break;
			}
		}
		return include;
	}

}
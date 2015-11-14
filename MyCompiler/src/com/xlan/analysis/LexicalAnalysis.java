package com.xlan.analysis;

import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.LinkedList;
import com.xlan.analysis.Token.Type;

public class LexicalAnalysis{

	private State state;
	private boolean transferredMeaningSign=false;
	private final LinkedList <Token> tokenBuffer=new LinkedList <Token>();
	private StringBuilder readBuffer=null;

	
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

	public Token read() throws IOException, LexicalAnalysisException{
		//Token token=new Token();
		return null;
	}


	private void refreshBuffer(char c){
		readBuffer=new StringBuilder(c);
	}



	private void createToken(Type type) {
		Token token = new Token(type, readBufferoString());
		tokenBuffer.addFirst(token);
		readBuffer null;
	}
	
	private void createToken(Type type,String value) {
		Token token = new Token(type,value);
		tokenBuffer.addFirst(token);
		readBuffer null;
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
					readBuffer.append(c);
					state=State.Normal;
				}else{
					//not identifier char
					createType=Type.Identifier;
					state=State.Normal;
					moveCursor=false;
				}

				break;
			case State.Sign: 
				if(SignParser.inCharSet()){
					readBuffer.append(c);
				}else{
					List<String> list=SignParser.parse(readBufferoString());
					for(String signStr:list){
						createToken(Type.Sign,signStr);
					}
					createType =null;
					state=State.Normal;
					moveCursor=false;
				}
				
				break;
			case State.Annotation: 
				if(c=='\n' || c=='\0'){
					createType=Type.Annotation;
					state=State.Normal;	
					moveCursor=false;				
				}else{
					readBuffer.append(c);
				}
				break;
			case State.String: 
				if(c=='\"'){
					createType=Type.String;
					state=State.Normal;
					readBuffer.append(c);
				}else if(c=='\\'){
					transferredMeaningSign=true;
				}else if(transferredMeaningSign){
					char tmpSign=StringTMMap.get(c);
					if(tmpSign==null){
						throw new LexicalAnalysisException(c);
					}
					readBuffer.append(tmpSign);
					transferredMeaningSign=false;
				}else if(c=='\n' || c=='\0'){
					throw new LexicalAnalysisException(c);
				}else{
					readBuffer.append(c);
				}

				break;
			case State.RegEx: 
				if(c=='\\'){
					transferredMeaningSign=true;
				}else if(c=='\0'){
					throw new LexicalAnalysisException(c);
				}else if(transferredMeaningSign){
					if(c!='\'') throw new LexicalAnalysisException(c);
					readBuffer.append(c);
					transferredMeaningSign=false;
				}else if('\''){
					readBuffer.append(c);
					createType=Type.RegEx;
					state=Normal;
				}else{
					readBuffer.append(c);
				}

				break;
			case State.Space: 
				if(include(Space,c)){
					readBuffer.append(c);
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
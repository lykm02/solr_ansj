package com.tzls.search.enums;
/**
 * Token marker
 * */
public enum TokenMarkerEnum {
	STARTS("^"),//
	WORD_SUFFIX("$"),//
	EWORD_PREFIX("_"),//
	ABBR_PREFIX("#"),//
	BLANK_DELIMITER(" "),
	PARTIAL_WORD_MARKER("-"),
	TERM_LENGTH_DELIMITER(":")	;
	
	private String token;
	private char c;
	
	private TokenMarkerEnum(String token, char c){
		this.token = token;
		this.c = c;
	}
	
	private TokenMarkerEnum(String token){
		this.token = token;
		if(token!=null && token.length()>0){
			this.c = token.charAt(0);
		}
	}
	
	public String getToken(){
		return token;
	}
	
	public char getChar(){
		return c;
	}
}

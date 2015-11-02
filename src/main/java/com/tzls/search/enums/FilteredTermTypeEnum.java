package com.tzls.search.enums;
/**
 * list all type enumeration.
 * 
 * */
public enum FilteredTermTypeEnum {
	MARKER("marker"),//
	MERGER("merger"),//
	NGRAM("ngram"),//
	SINGLE("single"),
	NUMBRIC("numberic"),
	;
	private String name;
	private FilteredTermTypeEnum(String name){
		this.name = name;
	}
	
	public String getTypeName(){
		return name;
	}
}

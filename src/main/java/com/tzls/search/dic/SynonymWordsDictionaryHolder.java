package com.tzls.search.dic;

import java.util.Map;
import java.util.TreeMap;

/**
 * To manage synonym words for each core.
 * 
 * */
public class SynonymWordsDictionaryHolder {
	public static final String DEFAULT =  "default";
	
	private static SynonymWordsDictionaryHolder holder ;
	
	public SynonymWordsMap synonymWordsMap = new SynonymWordsMap();
	
	private SynonymWordsDictionaryHolder(){
		
	}
	
	public static SynonymWordsDictionaryHolder getInstance(){
		if(holder == null){
			synchronized (SynonymWordsDictionaryHolder.class) {
				if(holder == null){
					holder = new SynonymWordsDictionaryHolder();
				}
			}
		}
		return holder;
	}
	
	
	public static class SynonymWordsMap{
		private Map<String,String> words ;
		
		public SynonymWordsMap(){
			words = new TreeMap<String,String>();
		}
		
		public void put(String key, String value){
			words.put(key, value);
		}
		
		public void remove(String key){
			words.remove(key);
		}
		
		public String get(String key){
			return words.get(key);
		}
		
		public boolean exists(String key){
			return words.containsKey(key);
		}
	}
}

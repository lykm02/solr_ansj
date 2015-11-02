package com.tzls.search.tokenizer.filter;

import java.io.IOException;
import java.util.Map;

import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;
import org.apache.lucene.analysis.tokenattributes.TypeAttribute;
import org.apache.lucene.analysis.util.TokenFilterFactory;

import com.tzls.search.enums.FilteredTermTypeEnum;
/**
 * Modify some numberic words. Such as "1日游" ==> "一日游"
 * 
 * */

public class NumbricFilterFactory extends TokenFilterFactory{
	private static final String words4Numberic = "零一二三四五六七八九";
	
	public NumbricFilterFactory(Map<String, String> args) {
		super(args);
	}

	@Override
	public TokenStream create(TokenStream ts) {
		return new NumbricFilter(ts);
	}
	/**
	 * mark first token.
	 * mark english words if includeEn == true && termType== en
	 * 
	 * */
	public static class NumbricFilter extends TokenFilter{
		//source
		private CharTermAttribute termAttr = addAttribute(CharTermAttribute.class);
		private TypeAttribute termType = addAttribute(TypeAttribute.class);
		private PositionIncrementAttribute posIncreAttr = addAttribute(PositionIncrementAttribute.class);
		private char[] TERM_BUFFER = new char[256];
		private char[] firstTerm = null;
		private int length;
		
		
		public NumbricFilter(TokenStream input){
			super(input);
			length = -1;
		}
		
		@Override
		public boolean incrementToken() throws IOException {
			if(firstTerm != null) {
				termAttr.copyBuffer(firstTerm, 0, length);
				if(termType!=null){
					termType.setType(FilteredTermTypeEnum.NUMBRIC.getTypeName());
				}
				firstTerm = null;
				posIncreAttr.setPositionIncrement(0);
				return true;
			}
			if(!input.incrementToken() ){
				return false;
			}
			if(containsNumberic()) {
				length = termAttr.length();
				if(TERM_BUFFER.length < termAttr.length() + 1) {
					TERM_BUFFER = new char[termAttr.length()*2];
				}
				firstTerm = TERM_BUFFER;
				System.arraycopy(termAttr.buffer(), 0, firstTerm, 0, length);
				modifyTermBuffer();
			}
			return true;
		}
		

		private void modifyTermBuffer() {
			for(int i=0;i<termAttr.length();i++){
				char c = firstTerm[i];
				if(Character.isDigit(c)){
					char code =  words4Numberic.charAt(c-'0');
					firstTerm[i]=code;
				}
			}
		}

		@Override
		public void reset() throws IOException{
			super.reset();
			length = -1;
			
			firstTerm = null;
		}
		
		private boolean containsNumberic(){
			for(char c : termAttr.buffer()){
				if(Character.isDigit(c)){
					return true;
				}
			}
			return false;
		}
	}
}

package com.tzls.search.tokenizer.filter;

import java.io.IOException;
import java.util.Map;

import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.tokenattributes.TypeAttribute;
import org.apache.lucene.analysis.util.TokenFilterFactory;

import com.tzls.search.enums.FilteredTermTypeEnum;
import com.tzls.search.enums.TokenMarkerEnum;

public class MarkerFilterFactory extends TokenFilterFactory{
	private boolean includeEn;
	
	public MarkerFilterFactory(Map<String, String> args) {
		super(args);
		includeEn = getBoolean(args,"includeEn",true);
	}

	@Override
	public TokenStream create(TokenStream ts) {
		return new MarkerFilter(ts, includeEn);
	}
	/**
	 * mark first token.
	 * mark english words if includeEn == true && termType== en
	 * 
	 * */
	public static class MarkerFilter extends TokenFilter{
		private boolean includeEnglish;
		//source
		private CharTermAttribute termAttr = addAttribute(CharTermAttribute.class);
		private TypeAttribute termType = addAttribute(TypeAttribute.class);
		private OffsetAttribute offsetAttr = addAttribute(OffsetAttribute.class);
		
		private char[] TERM_BUFFER = new char[256];
		private char[] firstTerm = null;
		private int startOffsetOfWord = -1;
		private int length;
		
		protected MarkerFilter(TokenStream input) {
			this(input,false);
		}
		public MarkerFilter(TokenStream input,boolean includeEn){
			super(input);
			this.includeEnglish = includeEn;
			length = -1;
		}
		
		@Override
		public boolean incrementToken() throws IOException {
			if(firstTerm != null) {
				termAttr.copyBuffer(firstTerm, 0, length);
				if(termType!=null){
					termType.setType(FilteredTermTypeEnum.MARKER.getTypeName());
				}
				firstTerm = null;
				return true;
			}
			if(!input.incrementToken() ){
				return false;
			}
			if(offsetAttr != null) {
				if(validType() && startOffsetOfWord == -1) {
					startOffsetOfWord = offsetAttr.startOffset();
				} 
				int termLength = termAttr.length();
				if(TERM_BUFFER.length < termAttr.length() + 1) {
					TERM_BUFFER = new char[termAttr.length()*2];
				}
				firstTerm = TERM_BUFFER;
				if(validType() && offsetAttr.startOffset() == startOffsetOfWord) {
					if(isEnglishWord(termAttr.buffer(), termAttr.length())){
						System.arraycopy(termAttr.buffer(), 0, firstTerm, 1, termLength);
						firstTerm[0] = TokenMarkerEnum.STARTS.getChar();
			//			firstTerm[1] = TokenMarkerEnum.EWORD_PREFIX.getChar();
						firstTerm[termLength + 1] = TokenMarkerEnum.WORD_SUFFIX.getChar();
						length = termLength + 2;
						termAttr.copyBuffer(firstTerm, 1, length-1);
					}else{
						System.arraycopy(termAttr.buffer(), 0, firstTerm, 1, termLength);
						firstTerm[0] = TokenMarkerEnum.STARTS.getChar();
						length = termLength + 1;
						termAttr.copyBuffer(firstTerm, 1, length-1);
					}
				}else{
					if(isEnglishWord(termAttr.buffer(), termAttr.length())){
//						System.arraycopy(termAttr.buffer(), 0, firstTerm, 0, termLength);
//						firstTerm[termLength] = TokenMarkerEnum.WORD_SUFFIX.getChar();
//						length = termLength + 1;
						termAttr.append(TokenMarkerEnum.WORD_SUFFIX.getChar());
						firstTerm = null;
//						return true;// keep english word
					}else{
						firstTerm = null;
					}
				}
			}
			return true;
		}
		
		private boolean isEnglishWord(char[] buffer, int length) {
			if(!includeEnglish){
				return false;
			}
			if(termType!=null && "en".equals(termType.type())){
				return true;
			}
			if(termType == null){
				for(int i=0;i<length;i++){
					if(!Character.isUpperCase(buffer[i]) && !Character.isLowerCase(buffer[i])){
						return false;
					}
				}
				return true;
			}
			return false;
		}
		@Override
		public void reset() throws IOException{
			super.reset();
			length = -1;
			startOffsetOfWord = -1;
			firstTerm = null;
		}
		/**
		 * skip those invalid type:
		 * 
		 * 2. "null"
		 * */
		private boolean validType(){
			if(termType == null){
				return true;
			}
			if("null".equals(termType.type())){
				return false; 
			}
			if(!includeEnglish&&"en".equals(termType.type())){
				return false;
			}
			return true;
		}
	}
}

package com.tzls.search.tokenizer.filter;

import java.io.IOException;
import java.util.Map;

import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;
import org.apache.lucene.analysis.tokenattributes.TypeAttribute;
import org.apache.lucene.analysis.util.TokenFilterFactory;

import com.tzls.search.enums.FilteredTermTypeEnum;
import com.tzls.search.enums.TokenMarkerEnum;

/**
 * Only required when we try to index.
 * 
 * */
public class SingleCharFilterFactory extends TokenFilterFactory {
	private static final String EXCLUDE_NULL_TYPE = "null";
	private static final String EXCLUDE_EN_TYPE = "en";
	private static final String EXCLUDE_NUMBRIC_TYPE = "m";
	public SingleCharFilterFactory(Map<String, String> args) {
		super(args);
	}

	@Override
	public TokenStream create(TokenStream input) {
		return new SingCharTokenFilter(input);
	}

	public static class SingCharTokenFilter extends TokenFilter {
		// current term status
		private TypeAttribute termType = addAttribute(TypeAttribute.class);
		private CharTermAttribute currTerm = addAttribute(CharTermAttribute.class);
		private PositionIncrementAttribute posIncreAttr = addAttribute(PositionIncrementAttribute.class);
		private OffsetAttribute offsetAttr = addAttribute(OffsetAttribute.class);
		
		private char[] TERM_BUFFER = new char[256];
		private int length;
		private int index;
		private int startOffset;

		public SingCharTokenFilter(TokenStream input) {
			super(input);
			index = -1;
		}

		// .
		@Override
		public boolean incrementToken() throws IOException {
			if (index == -1) {
				if (!input.incrementToken()) {
					return false;
				}
				if (validType() && currTerm.length()>1) {// type is valid
					int acctuallyLength = calcuateLength();
					if(acctuallyLength > 1 && acctuallyLength == currTerm.length() ){
						length = currTerm.length();
						index = 0;
						copyBufferAndExtendBufferIfNecessary(currTerm.buffer());
					}
				}
				startOffset = offsetAttr.startOffset();
				return true;
			} else {
				currTerm.copyBuffer(TERM_BUFFER, index, 1);
				if (termType != null) {
					termType.setType(FilteredTermTypeEnum.SINGLE.getTypeName());
				}
				posIncreAttr.setPositionIncrement(0);
				offsetAttr.setOffset(startOffset+index, startOffset+index+1);
				index++;
				
				if (index == length) {
					index = -1;
					length = -1;
					return true;
				}
			}

			return true;
		}

		@Override
		public void reset() throws IOException {
			super.reset();
			index = -1;
			length = -1;
		}

		private boolean validType() {
			// according to termType first
			if (termType != null) {
				if (EXCLUDE_NULL_TYPE.equals(termType.type())) {
					return false;
				}
				if (EXCLUDE_EN_TYPE.equals(termType.type())) {
					return false;
				}
				if (EXCLUDE_NUMBRIC_TYPE.equals(termType.type())) {
					return false;
				}
				if (FilteredTermTypeEnum.MERGER.getTypeName().equals(
						termType.type())) {
					return false;
				}
				
				if(FilteredTermTypeEnum.MARKER.getTypeName().equals(termType.type())){
					return false;
				}
				
				if(FilteredTermTypeEnum.NGRAM.getTypeName().equals(termType.type())){
					return false;
				}
				if(FilteredTermTypeEnum.NUMBRIC.getTypeName().equals(termType.type())){
					return false;
				}
			}
			return true;
		}

		private int calcuateLength() {
			int acctuallyLength = currTerm.length();
			if(currTerm.charAt(1)==TokenMarkerEnum.EWORD_PREFIX.getChar() ||
					currTerm.charAt(1)==TokenMarkerEnum.ABBR_PREFIX.getChar()){
				acctuallyLength -= 1;
			}
			if(currTerm.charAt(0)==TokenMarkerEnum.EWORD_PREFIX.getChar() ||
					currTerm.charAt(0)==TokenMarkerEnum.ABBR_PREFIX.getChar()||
					currTerm.charAt(0)==TokenMarkerEnum.STARTS.getChar()){
				acctuallyLength -= 1;
			}
			return acctuallyLength;
		}

		private void copyBufferAndExtendBufferIfNecessary(char[] buffer) {
			if (length > TERM_BUFFER.length) {
				TERM_BUFFER = new char[length * 2];
			}
			for (int i = 0; i < length; i++) {
				TERM_BUFFER[i] = buffer[i];
			}

		}
	}
}

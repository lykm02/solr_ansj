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
/**
 * Add two single char into one words( the words not exists in dictionary).
 * Define those words term type as "merged"
 * */
public class MergerFilterFactory extends TokenFilterFactory{
	private static final String EXCLUDE_EN_TYPE = "en";
	
	private boolean keepSingleChar;
	
	public MergerFilterFactory(Map<String, String> args) {
		super(args);
		keepSingleChar = getBoolean(args, "keepSingleChar", false);
	}

	@Override
	public TokenStream create(TokenStream input) {
		return new MergeSingleCharFilter(input, keepSingleChar);
	}
	
	public static class MergeSingleCharFilter extends TokenFilter{
		private boolean keepSingleChar;
		
		// current term status
		private TypeAttribute termType = addAttribute(TypeAttribute.class);
		private CharTermAttribute currTerm = addAttribute(CharTermAttribute.class);
		private OffsetAttribute offsetAttr = addAttribute(OffsetAttribute.class);
		
		//
		private char[] termBuffer = new char[3];
		private boolean hasMergedTerm;
		
		// pointer to last term status.
		private int lastOffset;
		private String lastTermType;
		
		protected MergeSingleCharFilter(TokenStream input) {
			this(input, false);
		}
		
		public MergeSingleCharFilter(TokenStream input, boolean keepSingleChar) {
			super(input);
			this.keepSingleChar = keepSingleChar;
			hasMergedTerm = false;
			lastOffset = -1;
		}
		
		@Override
		public boolean incrementToken() throws IOException {
			if(offsetAttr == null){// have no offset, do nothing.
				return input.incrementToken();
			}
			
			
			if(hasMergedTerm){// print single char after merged terms
				currTerm.copyBuffer(termBuffer, 0, 1);
				hasMergedTerm = false;
				if(termType!=null){
					termType.setType(lastTermType);
				}
				return true;
			}
			
			if(!input.incrementToken()){
				return false;
			}
			
			if(lastOffset > 0){//add prev char into termBuffer[0]
				if(currTerm.length() == 1){
					if(!adjacentAndValidTermTypes()){// doesn't do merger.
						termBuffer[0]=currTerm.charAt(0);
						if(termType!=null){
							lastTermType = termType.type();
						}
					}else{// print merged term firstly.
						termBuffer[1]=currTerm.charAt(0);
						currTerm.copyBuffer(termBuffer, 0, 2);
						if(termType!=null){
							lastTermType = termType.type();
							termType.setType(FilteredTermTypeEnum.MERGER.getTypeName());
						}
						if(keepSingleChar){
							hasMergedTerm = true;
						}
						//
						termBuffer[0]=currTerm.charAt(1);
					}
					lastOffset = offsetAttr.endOffset();
					return true;
				}else{
					lastOffset = -1;
					return true;
				}
			}
			
			if(currTerm.length() == 1){// cache related attributes.
				lastOffset = offsetAttr.endOffset();
				termBuffer[0]=currTerm.charAt(0);
				if(termType!=null){
					lastTermType = termType.type();
				}
			}
			return true;
		}
		
		@Override
		public void reset() throws IOException{
			super.reset();
			hasMergedTerm = false;
			lastTermType = null;
			lastOffset = -1;
		}
		
		private boolean adjacentAndValidTermTypes() {
			if(lastTermType == null){
				return false;
			}
			if(termType == null){
				return false;
			}
			if(EXCLUDE_EN_TYPE.equalsIgnoreCase(lastTermType) 
					||EXCLUDE_EN_TYPE.equalsIgnoreCase(termType.type())){
				return false;
			}
			
			if(FilteredTermTypeEnum.SINGLE.getTypeName().equalsIgnoreCase(lastTermType) 
					||FilteredTermTypeEnum.SINGLE.getTypeName().equalsIgnoreCase(termType.type())){
				return false;
			}

			if(offsetAttr == null || lastOffset != offsetAttr.startOffset()){
				return false;
			}
			return true;
		}
		
	} 
}

package com.tzls.search.tokenizer.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.FlagsAttribute;
import org.apache.lucene.analysis.tokenattributes.FlagsAttributeImpl;
import org.apache.lucene.analysis.util.CharArraySet;
import org.apache.lucene.util.Version;

public class WeakFlagFilter extends TokenFilter{
	private CharArraySet args ;
	private CharTermAttribute termAttr = addAttribute(CharTermAttribute.class);
	private FlagsAttribute flagsAttr = addAttribute(FlagsAttribute.class);
	
	private static  FlagsAttributeImpl weakFlagsAttr = new FlagsAttributeImpl();
	private static  FlagsAttributeImpl defaultFlagsAttr = new FlagsAttributeImpl();
	static {
		weakFlagsAttr.setFlags(1);
		defaultFlagsAttr.setFlags(0);
	}
	
	protected WeakFlagFilter(TokenStream input) {
		this(input,new ArrayList<String>(1));
	}
	
	@SuppressWarnings("deprecation")
	public WeakFlagFilter(TokenStream input, List<String> args){
		super(input);
		this.args = new CharArraySet(Version.LUCENE_CURRENT, args, true);
		termAttr = input.getAttribute(CharTermAttribute.class);
		flagsAttr = input.addAttribute(FlagsAttribute.class);
	}
	
	
	@Override
	public boolean incrementToken() throws IOException {
		if(!input.incrementToken()){
			return false;
		}
		if(accept()){
			 flagsAttr.setFlags(1);;
		}else{
			 flagsAttr.setFlags(0);
		}
		return true;
	}
	
	protected boolean accept() {
		 return args.contains(termAttr.buffer(), 0, termAttr.length());
	}
}

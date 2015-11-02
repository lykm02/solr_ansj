package com.tzls.search.tokenizer.filter;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import junit.framework.Assert;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.FlagsAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.tokenattributes.TypeAttribute;
import org.junit.Before;
import org.junit.Test;

import com.tzls.search.config.AnsjConfigurator;
import com.tzls.search.tokenizer.AnsjIndexTokenizer;

public class TestWeakFlagFilter {
	private List<String> list;
	@Before
	public void setup(){
		Properties prop = new Properties();
		AnsjConfigurator.init(prop , "F:/elasticsearch-analysis-ansj-1.x.1/dic");
		list = new ArrayList<String>();
		list.add("店");
		list.add("鸡");
	}
	
	
	@Test
	public void test() throws IOException{
		AnsjIndexTokenizer tokenizer = new AnsjIndexTokenizer(new StringReader("黄焖鸡烧鸭店"),true);

		TokenStream stream = new WeakFlagFilter(tokenizer,list);
		CharTermAttribute term = stream.getAttribute(CharTermAttribute.class);
		FlagsAttribute flag = stream.getAttribute(FlagsAttribute.class);
		Assert.assertTrue(stream.hasAttribute(TypeAttribute.class));
		Assert.assertTrue(stream.hasAttribute(OffsetAttribute.class));
		while(stream.incrementToken()){
			System.out.println(term);
			System.out.println(flag.getFlags());
		}
		stream.close();
	}
}

package com.tzls.search.tokenizer.filter;

import java.io.IOException;
import java.io.StringReader;
import java.util.Properties;

import org.ansj.splitWord.analysis.ToAnalysis;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.TypeAttribute;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.tzls.search.config.AnsjConfigurator;
import com.tzls.search.tokenizer.AnsjIndexTokenizer;

public class TestSingleCharTokenizer {
	private AnsjIndexTokenizer tokenizer = new AnsjIndexTokenizer(new StringReader(""),true);
	@Before
	public void setup() throws IOException{
		Properties prop = new Properties();
		AnsjConfigurator.init(prop , "F:/elasticsearch-analysis-ansj-1.x.1/dic");
		ToAnalysis.parse("");
		tokenizer.reset();
	}
	
	
	@Test
	public void test() throws IOException{
		System.out.println(" --------------   ------------ ");
		tokenizer.setReader(new StringReader("黄焖鸡烧鸭店"));
		TokenStream ts = new SingleCharFilterFactory.SingCharTokenFilter(tokenizer);
		CharTermAttribute term = ts.getAttribute(CharTermAttribute.class);
		while(ts.incrementToken()){
			System.out.println(term);
		}
		ts.close();
	}
	
	@Test
	public void testcase2() throws IOException{
		System.out.println(" --------------   ------------ ");
		tokenizer.setReader(new StringReader("上海小南国国际饭店"));
		TokenStream ts = new SingleCharFilterFactory.SingCharTokenFilter(tokenizer);
		CharTermAttribute term = ts.getAttribute(CharTermAttribute.class);
		TypeAttribute type = ts.getAttribute(TypeAttribute.class);
		while(ts.incrementToken()){
			System.out.print(term);
			System.out.println(type.type());
		}
		ts.close();
	}
	
	@Test
	public void testcase3() throws IOException{
		System.out.println(" --------------   ------------ ");
		tokenizer.setReader(new StringReader("上 海蜀九香火锅店"));
		TokenStream ts = new SingleCharFilterFactory.SingCharTokenFilter(tokenizer);
		CharTermAttribute term = ts.getAttribute(CharTermAttribute.class);
		TypeAttribute type = ts.getAttribute(TypeAttribute.class);
		while(ts.incrementToken()){
			System.out.print(term);
			System.out.println(type.type());
		}
		ts.close();
	}
	
	@Test
	public void testEn() throws IOException{
		System.out.println(" --------------   ------------ ");
		tokenizer.setReader(new StringReader("abdc上 d c天w"));
		TokenStream ts = new SingleCharFilterFactory.SingCharTokenFilter(tokenizer);
		CharTermAttribute term = ts.getAttribute(CharTermAttribute.class);
		TypeAttribute type = ts.getAttribute(TypeAttribute.class);
		while(ts.incrementToken()){
			System.out.print(term+"  ____ ");
			System.out.println(type.type());
		}
		ts.close();
	}
	
	@After
	public void teardown() throws IOException{
//		tokenizer.close();
	}
}

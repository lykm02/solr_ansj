package com.tzls.search;

import java.io.IOException;
import java.io.StringReader;
import java.util.Properties;

import org.ansj.library.UserDefineLibrary;
import org.ansj.splitWord.analysis.ToAnalysis;
import org.ansj.util.MyStaticValue;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.TypeAttribute;
import org.junit.Before;
import org.junit.Test;

import com.tzls.search.config.AnsjConfigurator;
import com.tzls.search.tokenizer.AnsjIndexTokenizer;

public class TestAnsjIndexTokenizer {
	
	@Before
	public void setup(){
		Properties prop = new Properties();
		AnsjConfigurator.init(prop , "F:/elasticsearch-analysis-ansj-1.x.1/dic");
		ToAnalysis.parse("");
//		System.out.println(UserDefineLibrary.ambiguityForest.getSize());
//		System.out.println(UserDefineLibrary.contains("马勒"));
//		MyStaticValue.isNameRecognition=false;
	}
	
	
	@Test
	public void test() throws IOException{
		AnsjIndexTokenizer tokenizer = new AnsjIndexTokenizer(new StringReader("黄焖鸡烧鸭店"),true);
		CharTermAttribute term = tokenizer.getAttribute(CharTermAttribute.class);
		while(tokenizer.incrementToken()){
			System.out.println(term);
		}
		tokenizer.close();
	}
	
	@Test
	public void testcase2() throws IOException{
		AnsjIndexTokenizer tokenizer = new AnsjIndexTokenizer(new StringReader("上海小南国国际饭店"),true);
		CharTermAttribute term = tokenizer.getAttribute(CharTermAttribute.class);
		TypeAttribute type = tokenizer.getAttribute(TypeAttribute.class);
		while(tokenizer.incrementToken()){
			System.out.println(term);
			System.out.println(type.type());
		}
		tokenizer.close();
	}
	
	@Test
	public void testcase3() throws IOException{
		AnsjIndexTokenizer tokenizer = new AnsjIndexTokenizer(new StringReader("我的确实在无聊"),true);
		CharTermAttribute term = tokenizer.getAttribute(CharTermAttribute.class);
		TypeAttribute type = tokenizer.getAttribute(TypeAttribute.class);
		while(tokenizer.incrementToken()){
			System.out.println(term);
			System.out.println(type.type());
		}
		tokenizer.close();
	}
	@Test
	public void testcase4() throws IOException{
		AnsjIndexTokenizer tokenizer = new AnsjIndexTokenizer(new StringReader("上 海小南国国际饭店"),true);
		CharTermAttribute term = tokenizer.getAttribute(CharTermAttribute.class);
		TypeAttribute type = tokenizer.getAttribute(TypeAttribute.class);
		while(tokenizer.incrementToken()){
			System.out.println(term);
			System.out.println(type.type());
		}
		tokenizer.close();
	}
	
	@Test
	public void testcase5() throws IOException{
		AnsjIndexTokenizer tokenizer = new AnsjIndexTokenizer(new StringReader("abc edf	g WI-FI"),true);
		CharTermAttribute term = tokenizer.getAttribute(CharTermAttribute.class);
		TypeAttribute type = tokenizer.getAttribute(TypeAttribute.class);
		while(tokenizer.incrementToken()){
			System.out.println(term);
			System.out.println(type.type());
		}
		tokenizer.close();
	}
	
	@Test
	public void testcase6() throws IOException{
		//上海马勒别墅
//		AnsjIndexTokenizer tokenizer = new AnsjIndexTokenizer(new StringReader("上海马勒"),true);
		AnsjIndexTokenizer tokenizer = new AnsjIndexTokenizer(new StringReader("电话卡 周杰伦摩天轮2巡演香港站门票"),true);
		CharTermAttribute term = tokenizer.getAttribute(CharTermAttribute.class);
		TypeAttribute type = tokenizer.getAttribute(TypeAttribute.class);
		while(tokenizer.incrementToken()){
			System.out.println(term);
			System.out.println(type.type());
		}
		tokenizer.close();
	}
	
	@Test
	public void testcase7() throws IOException{
		//上海马勒别墅
//		AnsjIndexTokenizer tokenizer = new AnsjIndexTokenizer(new StringReader("上海马勒"),true);
		AnsjIndexTokenizer tokenizer = new AnsjIndexTokenizer(new StringReader("西溪花间堂"),true);
		CharTermAttribute term = tokenizer.getAttribute(CharTermAttribute.class);
		TypeAttribute type = tokenizer.getAttribute(TypeAttribute.class);
		while(tokenizer.incrementToken()){
			System.out.println(term+"  "+type.type());
		}
		tokenizer.close();
	}
}

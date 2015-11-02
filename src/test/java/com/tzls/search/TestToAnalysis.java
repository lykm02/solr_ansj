package com.tzls.search;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.IndexAnalysis;
import org.ansj.splitWord.analysis.ToAnalysis;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class TestToAnalysis {
	private String input;
	
	public TestToAnalysis(String value){
		this.input = value;
	}
	
	@Parameters
	public static Collection paramter(){
		return Arrays.asList(new String[]{"我 的确实在无聊"},new String[]{"上海小南国国际饭店"},new String[]{"上(海)(hilton)"},new String[]{"北京大学"});
	}
	
	@BeforeClass
	public static void setup(){
		IndexAnalysis.parse("");
	}
	
	@Test
	public void test(){
		List<Term> terms = ToAnalysis.parse(input);
		for(Term term : terms){
			System.out.println(term.getName() + "  " + term.getNatureStr() + "  " + term.getOffe());
			List<Term> subTerm = term.getSubTerm();
			if(subTerm!=null&&!subTerm.isEmpty()){
				System.out.println(" ======> " + subTerm);
			}
		}
		System.out.println("---------------------------------");
	}
}

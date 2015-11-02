package com.tzls.search.tokenizer.filter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.util.ResourceLoader;
import org.apache.lucene.analysis.util.ResourceLoaderAware;
import org.apache.lucene.analysis.util.TokenFilterFactory;
/**
 * Use flag to denote weak words. 
 * e.g. 火锅  度假村  酒店
 * 
 * */
public class WeakFlagFilterFactory extends TokenFilterFactory implements
		ResourceLoaderAware {
	private String conf;
	private List<String> weakWords;
	
	public WeakFlagFilterFactory(Map<String, String> args) {
		super(args);
		conf = get(args, "conf");
		weakWords = new ArrayList<String>();
	}

	@Override
	public TokenStream create(TokenStream input) {
		return new WeakFlagFilter(input,weakWords);
	}

	@Override
	public void inform(ResourceLoader loader) throws IOException {
		if (conf == null) {
			//no defined configration.
			return;
		}
		if(weakWords == null){
			System.out.println("weak words still null");
			weakWords = new ArrayList<String>();
		}
		InputStream is = loader.openResource(conf);
		BufferedReader br = new BufferedReader(new InputStreamReader(is,"utf-8"));
		while(true){
			String line = br.readLine();
			if(line==null || "".equals(line.trim())){
				break;
			}
			weakWords.add(line);
		}
		br.close();
	}

}

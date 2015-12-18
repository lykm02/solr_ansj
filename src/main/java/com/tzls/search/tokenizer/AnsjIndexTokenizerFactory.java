package com.tzls.search.tokenizer;

import java.io.Reader;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.util.AttributeFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class AnsjIndexTokenizerFactory extends ReloadableTokenizerFactory {
	private static Logger logger = LoggerFactory.getLogger(AnsjIndexTokenizerFactory.class);
	private static AtomicInteger count = new AtomicInteger();
	private boolean rmPunc = true;
		
	public AnsjIndexTokenizerFactory(Map<String, String> args) {
		super(args);
		rmPunc = getBoolean(args, "rmPunc", true);
		logger.debug(":::ansj:construction::::::::::::::::::::::::::" + conf);
//		IndexAnalysis.parse("");
	}


	@Override
	public Tokenizer create(AttributeFactory factory, Reader input) {
		logger.info("AnsjIndex Tokenizer initialized times: " + count.incrementAndGet());
		return new AnsjIndexTokenizer(input, rmPunc);
	}
	
}

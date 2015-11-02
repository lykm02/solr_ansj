package com.tzls.search.tokenizer;

import java.io.Reader;
import java.util.Map;

import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.util.AttributeSource.AttributeFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class AnsjQueryTokenizerFactory extends ReloadableTokenizerFactory {
	private static Logger logger = LoggerFactory.getLogger(AnsjQueryTokenizerFactory.class);
	
	private boolean rmPunc = true;
		
	public AnsjQueryTokenizerFactory(Map<String, String> args) {
		super(args);
		rmPunc = getBoolean(args, "rmPunc", true);
		logger.debug(":::ansj:construction::::::::::::::::::::::::::" + conf);
//		ToAnalysis.parse("");
	}



//	public void inform(ResourceLoader loader) throws IOException {
//		logger.debug(":::ansj:::inform::::::::::::::::::::::::" + conf);
//		ReloaderRegister.register(this, loader, conf);		
//	}

	@Override
	public Tokenizer create(AttributeFactory factory, Reader input) {
		return new AnsjQueryTokenizer(input, rmPunc);
	}


	
}

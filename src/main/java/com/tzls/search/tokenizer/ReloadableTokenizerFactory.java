package com.tzls.search.tokenizer;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.lucene.analysis.util.ResourceLoader;
import org.apache.lucene.analysis.util.ResourceLoaderAware;
import org.apache.lucene.analysis.util.TokenizerFactory;
import org.apache.solr.core.SolrResourceLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tzls.search.config.AnsjConfigurator;

/**
 * Update user defined library
 * 
 * */
public abstract class ReloadableTokenizerFactory extends TokenizerFactory implements ResourceLoaderAware{
	protected static Logger logger = LoggerFactory.getLogger(AnsjIndexTokenizerFactory.class);
	protected String conf;
	
	protected ReloadableTokenizerFactory(Map<String, String> args) {
		super(args);
		assureMatchVersion();
		conf = get(args, "conf");
	}

	public void updateUserDefinedLibrary(List<InputStream> inputStreams){
		
	}
	
	public void inform(ResourceLoader loader) throws IOException {
		logger.debug(":::ansj:::inform::::::::::::::::::::::::" + conf);
		if(conf == null){
			if(loader instanceof SolrResourceLoader)
			AnsjConfigurator.init(new Properties(), ((SolrResourceLoader) loader).getConfigDir());
			return;
		}
		InputStream in = null;
		try{
			in = loader.openResource(conf);
		}catch(IOException e){
			logger.debug(e.getLocalizedMessage());
		}
		Properties setting = AnsjConfigurator.readAnsjConfig(in);
		if(loader instanceof SolrResourceLoader)
		AnsjConfigurator.init(setting, ((SolrResourceLoader) loader).getConfigDir());
//		ReloaderRegister.register(this, loader, conf);		
	}
	
	public String getBeanName(){
		return this.getClass().toString();
	}
}

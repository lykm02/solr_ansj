package com.tzls.search.config;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.ansj.splitWord.analysis.ToAnalysis;
import org.ansj.util.MyStaticValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * Initialize configuration for ansj.
 * 
 * */

public class AnsjConfigurator {
    public static String DEFAULT_USER_LIB_PATH = "ansj_dic/user";
    public static String DEFAULT_AMB_FILE_LIB_PATH = "ansj_dic/ambiguity.dic";
    public static String DEFAULT_STOP_FILE_LIB_PATH = "ansj_dic/stopLibrary.dic";
    protected static Logger logger = LoggerFactory.getLogger(AnsjConfigurator.class);
	private static volatile boolean isloaded;
    
	public static Properties readAnsjConfig(InputStream stream){
		Properties p = new Properties();
		if(stream == null){
			return p;
		}
		try{
			p.load(stream);
			stream.close();
		}catch(IOException e){
			if(stream!=null){
				try {
					stream.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
		return p;
	}
	
	public static void init(Properties prop,String configPath){
		if(isloaded){
			return;
		}
		String userLibraryPath = prop.getProperty("userLibrary",DEFAULT_USER_LIB_PATH);
		String ambiguityPath = prop.getProperty("ambiguity",DEFAULT_AMB_FILE_LIB_PATH);
	    //用户自定义辞典
		File path = new File(configPath,userLibraryPath);
		MyStaticValue.userLibrary = path.getAbsolutePath();
		
		logger.debug("user dir path:{}",MyStaticValue.userLibrary);
		MyStaticValue.isNameRecognition = "true".equalsIgnoreCase(prop.getProperty("name.enable", "false"));
		//用户自定义辞典
		path = new File(configPath,ambiguityPath);
		MyStaticValue.ambiguityLibrary = path.getAbsolutePath();
		System.out.println("ambiguityLibrary" + MyStaticValue.ambiguityLibrary);
		logger.debug("ambiguity dir path:{}",MyStaticValue.ambiguityLibrary );
		
		ToAnalysis.parse("");
		isloaded = true;
	}    
}

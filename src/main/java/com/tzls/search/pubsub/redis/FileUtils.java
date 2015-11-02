package com.tzls.search.pubsub.redis;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 *  Record data into files.
 * */
public class FileUtils {

	public static final Logger logger = LoggerFactory
			.getLogger("ansj-redis-msg-file");

	public static void remove(String content) {
		try {
			File file = new File("ansj/user/ext.dic");
			removeFile(content, file, false);
		} catch (FileNotFoundException e) {
			logger.error("file not found $ES_HOME/config/ansj/user/ext.dic", e,
					new Object[0]);
			e.printStackTrace();
		} catch (IOException e) {
			logger.error("read exception", e);
			e.printStackTrace();
		}
	}

	public static void append(String content) {
		try {
			File file = new File("ansj/user/ext.dic");
			appendFile(content, file);
		} catch (IOException e) {
			logger.error("read exception", e);
			e.printStackTrace();
		}
	}

	public static void removeAMB(String content) {
		try {
			File file = new File("ansj/ambiguity.dic");
			removeFile(content, file, true);
		} catch (FileNotFoundException e) {
			logger.error("file not found $ES_HOME/config/ansj/user/ext.dic", e);
			e.printStackTrace();
		} catch (IOException e) {
			logger.error("read exception", e);
			e.printStackTrace();
		}
	}

	public static void appendAMB(String content) {
		try {
			File file = new File("ansj/ambiguity.dic");
			appendFile(content, file);
		} catch (IOException e) {
			logger.error("read exception", e);
			e.printStackTrace();
		}
	}

	private static void appendFile(String content, File file)
			throws IOException {
		BufferedWriter writer = new BufferedWriter(new FileWriter(file, true));
		writer.write(content);
		writer.newLine();
		writer.close();
	}

	private static void removeFile(String content, File file, boolean head)
			throws FileNotFoundException, IOException {
		BufferedReader reader = new BufferedReader(new FileReader(file));
		List<String> list = new ArrayList<String>();
		String text = reader.readLine();
		while (text != null) {
			logger.info("match is {} text is{}",
					new Object[] { Boolean.valueOf(match(content, text, head)),
							text });
			if (match(content, text, head)) {
				list.add(text);
			}
			text = reader.readLine();
		}
		reader.close();
		BufferedWriter writer = new BufferedWriter(new FileWriter(file));
		for (String item : list) {
			writer.write(item);
			writer.newLine();
		}
		writer.close();
	}

	private static boolean match(String content, String text, boolean head) {
		if (head)
			return !text.trim().matches("^" + content + "\\D*$");
		return !text.trim().equals(content);
	}

}

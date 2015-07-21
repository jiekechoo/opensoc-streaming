package com.opensoc.parsing.parsers;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import oi.thekraken.grok.api.Match;
import oi.thekraken.grok.api.Grok;
import oi.thekraken.grok.api.exception.GrokException;

import org.apache.commons.io.IOUtils;
import org.json.simple.JSONObject;

public class GrokM0n0wallParser extends AbstractParser {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private transient InputStream pattern_url;
	Grok grok;

	public static final String PREFIX = "stream2file";
	public static final String SUFFIX = ".tmp";

	public static File stream2file(InputStream in) throws IOException {
		final File tempFile = File.createTempFile(PREFIX, SUFFIX);
		tempFile.deleteOnExit();
		try (FileOutputStream out = new FileOutputStream(tempFile)) {
			IOUtils.copy(in, out);
		}
		return tempFile;
	}

	public GrokM0n0wallParser() throws GrokException, IOException {
		// URL pattern_url = getClass().getClassLoader().getResource(
		// "pattarns/m0n0wall");
		// grok = Grok.create(pattern_url.getFile());
		// grok.compile("%{M0N0WALL}");

		pattern_url = getClass().getClassLoader().getResourceAsStream("patterns/m0n0wall");

		File file = stream2file(pattern_url);
		grok = Grok.create(file.getPath());
		grok.compile("%{M0N0WALL}");

	}

	public GrokM0n0wallParser(String filepath) throws GrokException {

		grok = Grok.create(filepath);
		grok.compile("%{M0N0WALL}");
	}

	public GrokM0n0wallParser(String filepath, String pattern) throws GrokException {

		grok = Grok.create(filepath);
		grok.compile("%{" + pattern + "}");
	}

	@Override
	public JSONObject parse(byte[] raw_message) {
		JSONObject payload = new JSONObject();
		String toParse = "";
		JSONObject toReturn;

		try {

			toParse = new String(raw_message, "UTF-8");
			Match gm = grok.match(toParse);
			gm.captures();

			toReturn = new JSONObject();

			toReturn.putAll(gm.toMap());
			toReturn.remove("M0N0WALL");
			String proto = toReturn.get("protocol").toString();
			proto = proto.replace("{", "");
			proto = proto.replace("}", "");
			toReturn.put("protocol", proto);
			return toReturn;

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

}

package rdf;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogHelper {
	private static Logger logger = LoggerFactory.getLogger("SemTechRDF Logger"); {}

	public static void logError(String string) {
		logger.error(string);
	}

	public static void logInfo(String string) {
		logger.info(string);
	}
}

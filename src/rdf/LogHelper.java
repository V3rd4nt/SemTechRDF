package rdf;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Peter & Natalia.
 */
public class LogHelper {

	public static final String ANSI_RESET = "\u001B[0m";
	public static final String ANSI_GREEN = "\u001B[32m";
	public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_BLUE = "\u001B[34m";

	private static Logger logger = LoggerFactory.getLogger(ANSI_BLUE + "SemTechRDF Logger" + ANSI_RESET); {}

	public static void logError(String string) {
		logger.error(ANSI_RED + string + ANSI_RESET);
	}

	public static void logInfo(String string) {
		logger.info(ANSI_GREEN + string + ANSI_RESET);
	}
}
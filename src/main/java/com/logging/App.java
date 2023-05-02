package com.logging;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class App {
    private static final Logger logger = LogManager.getLogger("ACTIVE");

    public static void main(String[] args) {

        LoggingLayer layer = new LoggingLayer();

        if (args.length > 0) {
            layer.addConsoleDebugOutput();
        }

        for (int i = 0; i < 3; i++) {
            String current = String.valueOf(i);
            layer.createAllTestOutputLogfile("all-" + current);
            layer.createSummaryLogFile("summary-" + current);

            logger.debug("this should be there");
            logger.info("hello");
            logger.error("hello there");
            logger.debug("this should also be there");
            logger.log(Level.getLevel("TEST"), "A TEST MESSAGE");
            logger.log(Level.getLevel("FAIL"), "A FAIL MESSAGE");
            logger.log(Level.getLevel("PASS"), "A PASS MESSAGE");
            logger.log(Level.getLevel("whoops"), "A TEST MESSAGE");

            layer.removeAppender("all-" + current);
            layer.removeAppender("summary-" + current);
        }
    }
}
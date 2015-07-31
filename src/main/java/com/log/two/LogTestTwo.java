/**
 *@author ZJ,2007-11-3
 *
 */
package com.log.two;

import org.apache.log4j.Logger;

public class LogTestTwo {
	private final static Logger log = Logger.getLogger(LogTestTwo.class);

	public void doLog() {
		log.debug("TestTwo debug");
		log.info("TestTwo info");
		log.warn("TestTwo warn");
		log.error("TestTwo error");
		log.fatal("TestTwo fatal");
	}
}

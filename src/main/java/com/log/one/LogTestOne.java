/**
 *@author ZJ,2007-11-3
 *
 */
package com.log.one;

import org.apache.log4j.Logger;

public class LogTestOne {
	private final static Logger log = Logger.getLogger(LogTestOne.class);
	
	public void doLog(){
		log.debug("TestOne debug");
		log.info("TestOne info");
		log.warn("TestOne warn");
		log.error("TestOne error");
		log.fatal("TestOne fatal");
	}
}

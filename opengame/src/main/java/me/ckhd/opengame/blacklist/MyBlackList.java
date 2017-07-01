package me.ckhd.opengame.blacklist;


import me.ckhd.opengame.common.utils.MacUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MyBlackList {
	private static Logger log = LoggerFactory.getLogger(MyBlackList.class);

	// init the module
	// return 1 for succ init
	public native int init();

	// check wheter the imsi is in black list
	// return 1 for exist
	// return 0 for no exist
	// return -1 for error
	public native int isInBlackList(String aImsi);

	static {
		try {
			if(!MacUtils.getOSName().startsWith("win")){
				System.loadLibrary("MyBlackList");
			}
		} catch (Throwable t) {
			log.error("load blacklist error!", t);
			System.exit(1);
		}
	}
}

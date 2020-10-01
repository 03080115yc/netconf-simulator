package com.gwtt.simulator.netconf.utils;

public class Constants {
	
	/**
	 * netconf消息结束标志
	 */
	public static final String MESSAGE_END_MARK = "]]>]]>";
	public static final String MSGLEN_REGEX_PATTERN = "\n#\\d+\n";
	public static final String CHUNKED_END_REGEX_PATTERN = "\n##\n";

	/**
	 * netconf消息编码
	 */
	public static final String MESSAGE_CHARSET ="UTF-8";

}

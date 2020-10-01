package com.gwtt.simulator.netconf.message;

import com.gwtt.simulator.netconf.model.hello.Capabilities;

public interface MessageHandler{

	public static final String MESSAGE_HEADER = "<?xml version=\"1.0\" encoding=\"utf-8\"?>";
	public static final String MESSAGE_NEWLINE = "\n";
	public static final String HASH = "#";
	public static final String LF = "\n";
	public static final String MSGLEN_REGEX_PATTERN = "\n#\\d+\n";
    
	/**
	 * 处理消息
	 * 
	 * @param requestMsg
	 * @return 如果可以处理，则处理并返回true
	 */
	public boolean handle(Capabilities capabilities, String requestMsg);
		
}

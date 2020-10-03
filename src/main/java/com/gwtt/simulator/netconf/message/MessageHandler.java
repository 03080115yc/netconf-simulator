package com.gwtt.simulator.netconf.message;

import com.gwtt.simulator.netconf.subsystem.NetconfClient;

public interface MessageHandler{
    
	/**
	 * 处理消息
	 * 
	 * @param client
	 * @param requestMsg
	 * @return 如果可以处理，则处理并返回true
	 */
	public boolean handle(NetconfClient client, String requestMsg);
		
}

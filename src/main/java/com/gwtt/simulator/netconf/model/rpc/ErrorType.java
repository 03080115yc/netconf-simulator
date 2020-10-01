package com.gwtt.simulator.netconf.model.rpc;

import com.gwtt.simulator.netconf.model.XstreamEnum;

public enum ErrorType implements XstreamEnum {
	/**
	 * 传输层错误
	 */
	TRANSPORT("transport"),
	/**
	 * 消息内容错误
	 */
	RPC("rpc"),
	/**
	 * 操作错误
	 */
	PROTOCOL("protocol"),
	/**
	 * 内容错误
	 */
	APPLICATION("application");

	private String value;

	private ErrorType(String value) {
		this.value = value;
	}

	@Override
	public String getValue() {
		return value;
	}

}

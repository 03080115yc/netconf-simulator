package com.gwtt.simulator.netconf.model.rpc;

import com.gwtt.simulator.netconf.model.XstreamEnum;

public enum ErrorSeverity implements XstreamEnum {
	ERROR("error"), WARNING("warning");

	private String value;

	private ErrorSeverity(String value) {
		this.value = value;
	}

	@Override
	public String getValue() {
		return value;
	}

}

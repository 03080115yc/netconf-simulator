package com.gwtt.simulator.netconf.model.rpc;

import com.gwtt.simulator.netconf.model.XstreamModel;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@XStreamAlias("rpc")
public class Rpc extends XstreamModel {
	
	@XStreamAsAttribute
	@XStreamAlias("message-id")
	private Integer messageId;

	private Get get;
	
	public Rpc() {
		super("urn:ietf:params:xml:ns:netconf:base:1.0");
	}

}

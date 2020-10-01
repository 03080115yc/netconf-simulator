package com.gwtt.simulator.netconf.model.hello;

import com.gwtt.simulator.netconf.model.XstreamModel;
import com.thoughtworks.xstream.annotations.XStreamAlias;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@XStreamAlias("hello")
public class Hello extends XstreamModel{

	@XStreamAlias("session-id")
	private Integer sessionId;
	
	private Capabilities capabilities;

	public Hello(){
		super("urn:ietf:params:xml:ns:netconf:base:1.0");
	}
	
	
}

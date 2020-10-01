package com.gwtt.simulator.netconf.model.rpc;

import com.gwtt.simulator.netconf.model.XstreamModel;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@XStreamAlias("rpc-reply")
public class RpcReply extends XstreamModel {

	@XStreamAsAttribute
	@XStreamAlias("message-id")
	private Integer messageId;
	
	@XStreamAlias("rpc-error")
	private RpcError rpcError;
	
	private RpcData data;
	
	public RpcReply(){
		super("urn:ietf:params:xml:ns:netconf:base:1.0");
	}
	
}

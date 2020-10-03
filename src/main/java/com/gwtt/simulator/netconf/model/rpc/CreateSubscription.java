package com.gwtt.simulator.netconf.model.rpc;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import lombok.Data;

@Data
@XStreamAlias("create-subscription")
public class CreateSubscription {
	
	private String stream;

}

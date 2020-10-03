package com.gwtt.simulator.netconf.model.hello;

import java.util.ArrayList;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamImplicit;

import lombok.Data;

@Data
public class Capabilities {

	public static final String CAPABILITY_BASE_1_0 = "urn:ietf:params:netconf:base:1.0";
	public static final String CAPABILITY_BASE_1_1 = "urn:ietf:params:netconf:base:1.1";
	
	public static final String CAPABILITY_NOTIFICATION_1_0 = "urn:ietf:params:netconf:capability:notification:1.0";

	@XStreamImplicit(itemFieldName = "capability")
	private List<String> capability = new ArrayList<>();

}

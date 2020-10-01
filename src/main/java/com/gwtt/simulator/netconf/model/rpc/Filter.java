package com.gwtt.simulator.netconf.model.rpc;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@XStreamAlias("filter")
public class Filter {

	@XStreamAsAttribute
	private String type;
		
}

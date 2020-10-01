package com.gwtt.simulator.netconf.model;

import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

import lombok.Data;

@Data
public class XstreamModel {

	@XStreamAsAttribute
	private String xmlns;

	public XstreamModel(String xmlns) {
		this.xmlns = xmlns;
	}

}

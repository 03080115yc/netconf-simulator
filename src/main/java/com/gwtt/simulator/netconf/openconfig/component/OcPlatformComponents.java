package com.gwtt.simulator.netconf.openconfig.component;

import java.util.List;

import com.gwtt.simulator.netconf.model.XstreamModel;
import com.thoughtworks.xstream.annotations.XStreamAlias;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@XStreamAlias("components")
public class OcPlatformComponents extends XstreamModel {

	public OcPlatformComponents() {
		super("http://openconfig.net/yang/platform");
	}

	private List<OcPlatformComponent> component;

}

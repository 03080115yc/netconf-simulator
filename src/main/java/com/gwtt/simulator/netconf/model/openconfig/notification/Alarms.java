package com.gwtt.simulator.netconf.model.openconfig.notification;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import lombok.Data;

@Data
@XStreamAlias("alarms")
public class Alarms {

	private List<Alarm> alarm;
	
}

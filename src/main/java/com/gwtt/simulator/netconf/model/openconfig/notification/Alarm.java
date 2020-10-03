package com.gwtt.simulator.netconf.model.openconfig.notification;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import lombok.Data;

@Data
@XStreamAlias("alarm")
public class Alarm {
	
	private String id;
	
	private AlarmState state;

}

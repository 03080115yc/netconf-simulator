package com.gwtt.simulator.netconf.model.openconfig.notification;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import lombok.Data;

@Data
@XStreamAlias("state")
public class AlarmState {

	private String id;
	
	private String resource;
	
	private String text;	
	
	@XStreamAlias("time-created")
	private Long timeCreated;
	
	private String severity;
	
	@XStreamAlias("type-id")
	private String typeId;
	
	@XStreamAlias("alarm-abbreviate")
	private String alarmAbbreviate;
}

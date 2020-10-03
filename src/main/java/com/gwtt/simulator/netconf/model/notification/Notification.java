package com.gwtt.simulator.netconf.model.notification;

import com.gwtt.simulator.netconf.model.XstreamModel;
import com.gwtt.simulator.netconf.model.openconfig.notification.AlarmsNotification;
import com.thoughtworks.xstream.annotations.XStreamAlias;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@XStreamAlias("notification")
public class Notification extends XstreamModel{

	public Notification(){
		super("urn:ietf:params:netconf:capability:notification:1.0");
	}
	
	@XStreamAlias("alarms-notification")
	private AlarmsNotification alarmsNotification;
	
}

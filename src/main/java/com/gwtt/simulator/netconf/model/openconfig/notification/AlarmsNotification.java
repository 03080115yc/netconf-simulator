package com.gwtt.simulator.netconf.model.openconfig.notification;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import lombok.Data;

@Data
@XStreamAlias("alarms-notification")
public class AlarmsNotification {

	private AlarmsNotificationUpdate update;
	
}

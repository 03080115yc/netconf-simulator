package com.gwtt.simulator.netconf.model.openconfig.notification;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import lombok.Data;

@Data
@XStreamAlias("update")
public class AlarmsNotificationUpdate {

	@XStreamAlias(value = "alarms", impl = List.class)
	private List<Alarm> alarms;

}

package com.gwtt.simulator.netconf.message;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.gwtt.simulator.netconf.model.notification.Notification;
import com.gwtt.simulator.netconf.model.openconfig.notification.Alarm;
import com.gwtt.simulator.netconf.model.openconfig.notification.AlarmState;
import com.gwtt.simulator.netconf.model.openconfig.notification.Alarms;
import com.gwtt.simulator.netconf.model.openconfig.notification.AlarmsNotification;
import com.gwtt.simulator.netconf.model.openconfig.notification.AlarmsNotificationUpdate;
import com.gwtt.simulator.netconf.subsystem.NetconfClient;
import com.gwtt.simulator.netconf.utils.XstreamException;
import com.gwtt.simulator.netconf.utils.XstreamUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NotificationJob extends NetconfWriter implements Job {

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		System.out.println("**************************");

		Notification notification = generateNotification();

		Set<NetconfClient> clientSet = NotificationSender.getClientSet();
		for (NetconfClient client : clientSet) {
			try {
				String body = XstreamUtil.toXML(notification);
				writeMessage(client.getOutput(), client.getCapabilities(), body);
			} catch (XstreamException e) {
				log.error("send notification err");
			}

		}

	}

	private Notification generateNotification() {
		AlarmState state = new AlarmState();
		state.setId(UUID.randomUUID().toString());
		state.setAlarmAbbreviate("ETH_LOS");
		state.setResource("PORT-1-1-L1");
		state.setSeverity("Critical");
		state.setText("ETH LOSS");
		state.setTimeCreated(System.currentTimeMillis());
		state.setTypeId("ETH-ALARM");

		Alarm alarm = new Alarm();
		alarm.setId(state.getId());
		alarm.setState(state);

		List<Alarm> alarmList = new ArrayList<>();
		alarmList.add(alarm);

		Alarms alarms = new Alarms();
		alarms.setAlarm(alarmList);

		AlarmsNotificationUpdate update = new AlarmsNotificationUpdate();
		update.setAlarms(alarmList);

		AlarmsNotification alarmsNotification = new AlarmsNotification();
		alarmsNotification.setUpdate(update);

		Notification notification = new Notification();
		notification.setAlarmsNotification(alarmsNotification);

		return notification;
	}
}

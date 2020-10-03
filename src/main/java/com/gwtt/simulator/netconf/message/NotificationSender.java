package com.gwtt.simulator.netconf.message;

import java.util.Date;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

import com.gwtt.simulator.netconf.subsystem.NetconfClient;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NotificationSender {

	private static Set<NetconfClient> clientSet = new ConcurrentSkipListSet<>();

	public static void start() {
		try {
			SchedulerFactory schedulerFactory = new StdSchedulerFactory();
			Scheduler scheduler = schedulerFactory.getScheduler();
			
			addJob(scheduler, "0/10 * * * * ?", "demo");
			
			scheduler.start();
		} catch (SchedulerException e) {
			log.error("Quartz start err");
		}


	}

	public static void subscription(NetconfClient client) {
		clientSet.add(client);
	}

	public static void unsubscription(NetconfClient client) {
		clientSet.remove(client);
	}
	
	public static Set<NetconfClient> getClientSet() {
		return clientSet;
	}

	private static void addJob(Scheduler scheduler, String cronExpression, String jobKey) throws SchedulerException {
		JobDetail jobDetail = JobBuilder.newJob(NotificationJob.class).withIdentity(jobKey, 
				"notification-group").build();

		Trigger trigger = TriggerBuilder.newTrigger().withIdentity(jobKey).startAt(new Date())
				.withSchedule(CronScheduleBuilder.cronSchedule(cronExpression)).build();

		scheduler.scheduleJob(jobDetail, trigger);
	}

}

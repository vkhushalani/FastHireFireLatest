package com.amrest.fastHire.controller;

import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/FastHireAdmin")
public class Scheduler {
	private static ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
	static ScheduledFuture<?> scheduledFuture;
	Logger logger = LoggerFactory.getLogger(Scheduler.class);

	@GetMapping(value = "/startScheduler")
	public void startScheduler() {
		if (scheduler.isShutdown()) {
			// Task to be scheduled
			Runnable task = () -> {
				logger.debug("Scheduler Running task: " + new Date());
			};
			scheduledFuture = scheduler.scheduleAtFixedRate(task, 15, 10, TimeUnit.SECONDS);
		}
	}

	@GetMapping(value = "/stopScheduler")
	public void stopScheduler() {
		if (!scheduler.isShutdown()) {
			scheduledFuture.cancel(true);
			scheduler.shutdown();
			logger.debug("Scheduler stoper: " + new Date());
		}
	}

	@GetMapping(value = "/getSchedulerState")
	public void getSchedulerState() {
		logger.debug("scheduledFuture isCancelled: " + scheduledFuture.isCancelled());
		logger.debug("scheduler isShutdown: " + scheduler.isShutdown());
	}
}

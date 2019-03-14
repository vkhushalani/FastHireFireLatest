package com.amrest.fastHire.controller;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import javax.naming.NamingException;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONObject;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.amrest.fastHire.SF.scp.users.scheduler.SCPOperationsForScheduler;

@RestController
@RequestMapping("/FastHireAdmin")
public class Scheduler {
	private static ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
	static ScheduledFuture<?> scheduledFuture;
	Logger logger = LoggerFactory.getLogger(Scheduler.class);

	@GetMapping(value = "/startScheduler")
	public ResponseEntity<?> startScheduler() {
		// Task to be scheduled
		scheduler = Executors.newScheduledThreadPool(1);
		Runnable task = () -> {
			logger.debug("Scheduler Running task: " + new Date());
			SCPOperationsForScheduler obj = new SCPOperationsForScheduler();
			try {
				obj.getUsers();
			} catch (Exception e) {
				e.printStackTrace();
			}
		};
		scheduledFuture = scheduler.scheduleAtFixedRate(task, 0, 30, TimeUnit.SECONDS);
		logger.debug("Scheduler started: " + new Date());
		return ResponseEntity.ok().body("Scheduler Started successfully!");
	}

	@GetMapping(value = "/stopScheduler")
	public ResponseEntity<String> stopScheduler() {

		if (!scheduler.isShutdown()) {
			try {
				scheduledFuture.cancel(true);
				scheduler.shutdown();
				logger.debug("Scheduler stoped: " + new Date());
				return ResponseEntity.ok().body("Scheduler Started successfully!");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return ResponseEntity.ok().body("Scheduler already stoped!");
	}

	@GetMapping(value = "/getSchedulerState")
	public String getSchedulerState() {
		JSONObject responseObj = new JSONObject();
		responseObj.put("Scheduler Running: ", !scheduler.isShutdown());
		responseObj.put("Scheduled Job Running: ", !scheduledFuture.isDone());
		return responseObj.toString();
	}

	@GetMapping(value = "/copyUsers")
	public ResponseEntity<?> getSchedulerUser()
			throws NamingException, ClientProtocolException, URISyntaxException, IOException, ParseException {
		SCPOperationsForScheduler obj = new SCPOperationsForScheduler();
		try {
			return (obj.getUsers());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ResponseEntity.ok().body("Error!");
	}

}

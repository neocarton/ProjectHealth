package me.lam.huyen;

import me.lam.huyen.channel.ProjectLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class ApplicationStartup implements ApplicationListener<ApplicationReadyEvent> {

	private Logger logger = LoggerFactory.getLogger(ApplicationStartup.class);

	@Autowired
	private ProjectLoader projectLoader;

	@Override
	public void onApplicationEvent(ApplicationReadyEvent event) {
		logger.debug("Start processing...");
        projectLoader.loadProjects();
	}
}
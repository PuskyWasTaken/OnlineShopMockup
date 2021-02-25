package com.pusky.onlineshopmockup;

import com.pusky.onlineshopmockup.config.ApplicationProperties;
import com.pusky.onlineshopmockup.constants.PuskyConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.Collection;


@SpringBootApplication
@EnableScheduling
@EnableConfigurationProperties(ApplicationProperties.class)
public class OnlineShopMockupApplication {

	private final Logger log = LoggerFactory.getLogger(OnlineShopMockupApplication.class);
	private final Environment env;

	public OnlineShopMockupApplication(Environment env) {
		this.env = env;
	}

	/**
	 * Initializes OnlineShopJHMockup.
	 */
	@PostConstruct
	public void initApplication() {
		Collection<String> activeProfiles = Arrays.asList(env.getActiveProfiles());
		if (activeProfiles.contains(PuskyConstants.SPRING_PROFILE_DEVELOPMENT) && activeProfiles.contains(PuskyConstants.SPRING_PROFILE_PRODUCTION)) {
			log.error("You have misconfigured your application! It should not run " +
					"with both the 'dev' and 'prod' profiles at the same time.");
		}
	}

	/**
	 * Main method, used to run the application.
	 *
	 * @param args the command line arguments.
	 */
	public static void main(String[] args) {

//		SpringApplication.run(OnlineShopMockupApplication.class, args);

		SpringApplication app = new SpringApplication(OnlineShopMockupApplication.class);
		DefaultProfileUtil.addDefaultProfile(app);
		Environment env = app.run(args).getEnvironment();

	}



}

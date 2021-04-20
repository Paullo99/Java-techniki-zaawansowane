package com.app;
import com.gui.MainWindow;
import java.awt.EventQueue;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class Lab03Application {

	private static RestTemplate restTemplate;
	
	public static void main(String[] args) {
		SpringApplicationBuilder builder = new SpringApplicationBuilder(Lab03Application.class);
		builder.headless(false).web(WebApplicationType.NONE);
		
		ConfigurableApplicationContext context = builder.run(args);
		
		restTemplate = context.getBean(RestTemplate.class);
		
		EventQueue.invokeLater(() -> {
			var frame = context.getBean(MainWindow.class);
			frame.setVisible(true);
		});
	}
	
	public static RestTemplate getRestTemplate() {
		return restTemplate;
	}
	
	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
		return builder.build();
	}
	
	@Bean
	public MainWindow mainWindow() {
		return new MainWindow();
	}

}

package com.dsrts.xcopilot;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.swing.*;

@SpringBootApplication
@EnableScheduling
public class XcopilotApplication {

    private static ConfigurableApplicationContext applicationContext;

    public static void main(String[] args) {
        applicationContext = new SpringApplicationBuilder(XcopilotApplication.class)
                .headless(false)
                .web(false)
                .run(args);
	}

    public static void shutdown() {
        if (applicationContext.isActive()) {
            SpringApplication.exit(applicationContext,() -> 0);
            applicationContext.close();
        }
    }

    @Bean
    public CommandLineRunner frame(Main main) {
        return (strings) -> {
            SwingUtilities.invokeLater(() -> {main.pack();main.setSize(1024,768);main.setVisible(true);});
        };
    }
}

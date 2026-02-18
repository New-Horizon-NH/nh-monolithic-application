package com.newhorizon.nhmonolithicapplication;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class NhMonolithicApplication {

    public static void main(String[] args) {
        SpringApplication.run(NhMonolithicApplication.class, args);
    }

//    @Bean
//    public CommandLineRunner sendTestMessage(MessageChannel mqttOutboundChannel) {
//        return args -> {
//            mqttOutboundChannel.send(MessageBuilder.withPayload("Hello from Spring Integration MQTT").build());
//            System.out.println("Message sent to MQTT");
//        };
//    }

}

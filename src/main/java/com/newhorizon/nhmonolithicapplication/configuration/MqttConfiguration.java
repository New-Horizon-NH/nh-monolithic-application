package com.newhorizon.nhmonolithicapplication.configuration;

import com.newhorizon.nhmonolithicapplication.processor.CommonMessageProcessor;
import com.newhorizon.nhmonolithicapplication.properties.MqttConfigurationProperties;
import io.micrometer.tracing.Span;
import io.micrometer.tracing.Tracer;
import lombok.RequiredArgsConstructor;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.core.MessageProducer;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.support.ChannelInterceptor;

@Configuration
@RequiredArgsConstructor
public class MqttConfiguration {

    private final MqttConfigurationProperties mqttConfigurationProperties;

    @Bean
    public MqttPahoClientFactory mqttClientFactory() {
        DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
        MqttConnectOptions options = new MqttConnectOptions();
        options.setServerURIs(mqttConfigurationProperties.getBootstrapServers().toArray(new String[0]));
        options.setCleanSession(true);
        options.setUserName(mqttConfigurationProperties.getCredentials().getUsername());
        options.setPassword(mqttConfigurationProperties.getCredentials().getPasswordCharArray());
        factory.setConnectionOptions(options);
        return factory;
    }

//    @Bean
//    public MessageChannel mqttOutboundChannel() {
//        return new DirectChannel();
//    }
//
//    @Bean
//    @ServiceActivator(inputChannel = "mqttOutboundChannel")
//    public MessageHandler mqttOutbound() {
//        MqttPahoMessageHandler messageHandler = new MqttPahoMessageHandler(MQTT_CLIENT_ID, mqttClientFactory());
//        messageHandler.setAsync(true);
//        messageHandler.setDefaultTopic(MQTT_TOPIC);
//        return messageHandler;
//    }

    @Bean
    @Primary
    public MessageChannel mqttInputChannel(Tracer tracer) {
        DirectChannel messageChannel = new DirectChannel();
        messageChannel.addInterceptor(new ChannelInterceptor() {
            @Override
            public Message<?> preSend(Message<?> message, MessageChannel channel) {
                // Start a new span for each message
                Span newSpan = tracer.nextSpan().name("mqtt-message").start();
                try (Tracer.SpanInScope spanInScope = tracer.withSpan(newSpan)) {
                    // Add trace information or tags to the span if necessary
                    newSpan.tag("mqtt.topic", message.getHeaders().get("mqtt_receivedTopic", String.class));
                    return message;
                } finally {
                    // Close the span after the message is processed
                    newSpan.end();
                }
            }
        });
        return messageChannel;
    }

    @Bean
    public MessageProducer inbound(MessageChannel mqttInputChannel,
                                   MqttPahoClientFactory mqttClientFactory) {
        MqttPahoMessageDrivenChannelAdapter adapter =
                new MqttPahoMessageDrivenChannelAdapter(mqttConfigurationProperties.getClientId().concat("_inbound"),
                        mqttClientFactory,
                        mqttConfigurationProperties.getTopics()
                                .values()
                                .toArray(new String[0]));
        adapter.setCompletionTimeout(5000);
        adapter.setConverter(new DefaultPahoMessageConverter());
        adapter.setQos(1);
        adapter.setOutputChannel(mqttInputChannel);
        return adapter;
    }

    @Bean
    @ServiceActivator(inputChannel = "mqttInputChannel")
    public MessageHandler mqttInputHandler(CommonMessageProcessor processor) {
        return processor;
    }

//    @Bean
//    public IntegrationFlow mqttInboundFlow(MessageChannel mqttInputChannel,
//                                           CommonMessageProcessor commonMessageProcessor) {
//        return IntegrationFlow.from(mqttInputChannel)
//                .handle(message -> System.out.println("Received message: " + message.getPayload() + "\nfrom topic: " + message.getHeaders().get("mqtt_receivedTopic")))
//                .handle(commonMessageProcessor)
//                .get();
//    }
}

package com.vee.demo.spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import reactor.core.publisher.Flux;
import reactor.core.publisher.UnicastProcessor;

/**
 * The entry point of the Spring Boot application.
 */
@SpringBootApplication
public class Application {

    @Bean
    UnicastProcessor<ChatMessage> publisher() {
        return UnicastProcessor.create();
    }
    
    @Bean
    Flux<ChatMessage> messages(UnicastProcessor<ChatMessage> publisher) {
        return publisher.replay(30).autoConnect();
    }
    
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}

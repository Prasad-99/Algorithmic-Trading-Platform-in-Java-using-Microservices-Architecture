package com.prasad.ordermanagementservice.config;

import com.prasad.ordermanagementservice.dto.SignalDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;

import static com.prasad.ordermanagementservice.executor.OMSRunner.runOrderManagementService;

@Configuration
public class SignalConsumer {

    private final Logger log = LoggerFactory.getLogger(SignalConsumer.class);

    @Bean
    public Consumer<SignalDto> processSignal() {
        return signalDto -> {
            log.info("Signal Message Received from market service");
            runOrderManagementService(signalDto);
        };
    }
}

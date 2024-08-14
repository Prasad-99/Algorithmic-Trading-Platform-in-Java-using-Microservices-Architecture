package com.prasad.tradeengineservice.service.Impl;

import com.prasad.tradeengineservice.entity.SignalData;
import com.prasad.tradeengineservice.repository.SignalDataRepository;
import com.prasad.tradeengineservice.service.ISignalData;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class SignalDataImpl implements ISignalData {

    private SignalDataRepository signalDataRepository;
    private final Logger log = LoggerFactory.getLogger(SignalDataImpl.class);

    @Override
    public void addSignalData(SignalData signal) {
//        signalDataRepository.save(signal);
        log.info("Updating signal data for signal ");
    }
}

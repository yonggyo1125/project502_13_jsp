package org.choongang.global.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.choongang.global.config.annotations.Service;

@Service
public class ObjectMapperService extends ObjectMapper {
    public ObjectMapperService() {
        registerModule(new JavaTimeModule());
    }
}

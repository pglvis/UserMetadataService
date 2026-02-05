package com.example.usermetadata.user.service.impl;

import com.example.usermetadata.common.exception.BusinessException;
import com.example.usermetadata.user.dto.UserDto;
import com.example.usermetadata.user.entity.UserEntity;
import com.example.usermetadata.user.repository.UserRepository;
import com.example.usermetadata.user.service.UserService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.slf4j.MDC;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.Duration;
import java.util.Optional;


import static com.example.usermetadata.common.metrics.MetricsConstants.*;


@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private ModelMapper mapper;
    private MeterRegistry meterRegistry;
    private static final String CIRCUIT_BREAKER = "UserServiceCB";

    public UserServiceImpl(UserRepository userRepository, ModelMapper mapper, MeterRegistry meterRegistry) {
        this.userRepository = userRepository;
        this.mapper = mapper;
        this.meterRegistry = meterRegistry;
    }

    @Override
    @Transactional
    @CircuitBreaker(name = CIRCUIT_BREAKER, fallbackMethod = "createUserFallback")
    public UserDto createUser(UserDto userDto, String key) {
        long startTs = System.currentTimeMillis();
        String requestId = MDC.get("requestId");
        log.info("Request: {} | Create User request received", requestId);
        meterRegistry.counter(TOTAL_REQUESTS).increment();

        try {
            Optional<UserEntity> existingUser = userRepository.getUserEntityByIdempotencyKeyEqualsIgnoreCase(key);
            if(existingUser.isPresent()) {
                log.info("RequestId: {} | Idempotent hit", requestId);
                meterRegistry.counter(SUCCESS_COUNT).increment();
                return mapper.map(existingUser, UserDto.class);
            }

            UserEntity userEntity = mapper.map(userDto, UserEntity.class);
            userEntity.setIdempotencyKey(key);
            UserEntity savedEntity = userRepository.save(userEntity);

            meterRegistry.counter(SUCCESS_COUNT).increment();
            long latency = System.currentTimeMillis() - startTs;
            meterRegistry.timer(REQUEST_LATENCY).record(Duration.ofMillis(latency));
            log.info("Request: {} | User created in: {} ms", requestId, latency);
            return mapper.map(savedEntity, UserDto.class);

        } catch (Exception ex) {
            meterRegistry.counter(FAILURE_COUNT).increment();
            long latency = System.currentTimeMillis() - startTs;
            log.error("RequestId={} | Error after {} ms | {}", requestId, latency, ex.getMessage(), ex);
            throw ex;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public UserDto getUser(Long userId) {
        long startTs = System.currentTimeMillis();
        String requestId = MDC.get("requestId");
        log.info("Request: {} | Get User request received", requestId);
        meterRegistry.counter(TOTAL_REQUESTS).increment();

        UserEntity user = userRepository.findById(userId).orElseThrow(() -> new BusinessException("USER_NOT_FOUND", "User not found"));
        meterRegistry.counter(SUCCESS_COUNT).increment();
        long latency = System.currentTimeMillis() - startTs;
        meterRegistry.timer(REQUEST_LATENCY).record(Duration.ofMillis(latency));
        log.info("Request: {} | User fetched successfully in {} ms", requestId, latency);
        return mapper.map(user, UserDto.class);
    }

    public UserDto createUserFallback(UserDto request, String key, Throwable t) {
        log.error("Circuit breaker open! Fallback executed: {}", t.getMessage());
        throw new RuntimeException("Service temporarily unavailable");
    }
}

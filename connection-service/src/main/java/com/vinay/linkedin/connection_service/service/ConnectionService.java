package com.vinay.linkedin.connection_service.service;

import com.vinay.linkedin.connection_service.auth.UserContextHolder;
import com.vinay.linkedin.connection_service.entity.Person;
import com.vinay.linkedin.connection_service.repository.PersonRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ConnectionService {

    private final PersonRepository personRepository;

    public List<Person> getFirstDegreeConnections() {
        Long userId = UserContextHolder.getCurrentUserId();
        log.info("Getting first degree connections user with id: {} ", userId);

        return personRepository.getFirstDegreeConnections(userId);
    }

    public List<Person> getSecondDegreeConnections() {
        Long userId = UserContextHolder.getCurrentUserId();
        log.info("Getting second degree connections user with id: {} ", userId);

        return personRepository.getSecondDegreeConnections(userId);
    }
}

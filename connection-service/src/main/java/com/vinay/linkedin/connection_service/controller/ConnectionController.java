package com.vinay.linkedin.connection_service.controller;

import com.vinay.linkedin.connection_service.entity.Person;
import com.vinay.linkedin.connection_service.service.ConnectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/core")
@RequiredArgsConstructor
public class ConnectionController {

    private final ConnectionService connectionService;

    @GetMapping("/first-degree")
    public ResponseEntity<List<Person>> getFirstConnections() {
        return ResponseEntity.ok(connectionService.getFirstDegreeConnections());
    }

    @GetMapping("/second-degree")
    public ResponseEntity<List<Person>> getSecondConnections() {
        return ResponseEntity.ok(connectionService.getSecondDegreeConnections());
    }
}

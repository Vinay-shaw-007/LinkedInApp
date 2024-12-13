package com.vinay.linkedin.connection_service.repository;

import com.vinay.linkedin.connection_service.entity.Person;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;

import java.util.List;
import java.util.Optional;

public interface PersonRepository extends Neo4jRepository<Person, Long> {

    Optional<Person> getByName(String name);

    @Query("MATCH (personA:Person) -[:CONNECTED_TO]- (personB:Person) " +
            "WHERE personA.userId = $userId " +
            "RETURN personB")
    List<Person> getFirstDegreeConnections(Long userId);

    @Query("MATCH (personA:Person) -[:CONNECTED_TO*2]- (personB:Person) " +
            "WHERE personA.userId = $userId AND " +
            "NOT (personA) -[:CONNECTED_TO]- (personB)"+
            "RETURN personB")
    List<Person> getSecondDegreeConnections(Long userId);
}

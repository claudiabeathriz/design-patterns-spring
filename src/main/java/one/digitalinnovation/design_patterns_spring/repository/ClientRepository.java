package one.digitalinnovation.design_patterns_spring.repository;

import one.digitalinnovation.design_patterns_spring.model.Client;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends CrudRepository<Client, Long> {
}
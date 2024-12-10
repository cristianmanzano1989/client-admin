package com.example.clientadmin.client_admin.repositories;

import com.example.clientadmin.client_admin.entities.Client;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends CrudRepository<Client, Long> {

  boolean existsBySharedKey(String sharedKey);

  Optional<Client> findBySharedKey(String sharedKey);
}

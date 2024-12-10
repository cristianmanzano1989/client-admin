package com.example.clientadmin.client_admin.services;

import com.example.clientadmin.client_admin.entities.Client;
import com.example.clientadmin.client_admin.repositories.ClientRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ClientServiceImpl implements ClientService {

  private final ClientRepository clientRepository;

  public ClientServiceImpl(ClientRepository clientRepository) {
    this.clientRepository = clientRepository;
  }

  @Override
  @Transactional(readOnly = true)
  public List<Client> getAllClients() {
    return (List<Client>) clientRepository.findAll();
  }

  @Override
  @Transactional(readOnly = true)
  public Optional<Client> getClientBySharedKey(String sharedKey) {
    return clientRepository.findBySharedKey(sharedKey);
  }

  @Override
  @Transactional
  public Client addClient(Client client) {
    if (clientRepository.existsBySharedKey(client.getSharedKey())) {
      throw new IllegalArgumentException(
          "El cliente con shared_key '" + client.getSharedKey() + "' ya existe.");
    }
    return clientRepository.save(client);
  }
}

package com.example.clientadmin.client_admin.services;

import com.example.clientadmin.client_admin.entities.Client;
import java.util.List;
import java.util.Optional;

public interface ClientService {

  public List<Client> getAllClients();

  public Optional<Client> getClientBySharedKey(String sharedKey);

  public Client addClient(Client client);
}

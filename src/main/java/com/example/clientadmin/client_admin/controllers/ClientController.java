package com.example.clientadmin.client_admin.controllers;

import com.example.clientadmin.client_admin.dtos.ApiResponse;
import com.example.clientadmin.client_admin.entities.Client;
import com.example.clientadmin.client_admin.services.ClientService;
import com.example.clientadmin.client_admin.utils.ConstantsResponseCode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:4200") // Permite solicitudes de localhost:4200
@RestController
@RequestMapping("/api/clients")
public class ClientController {
  private static final Logger logger = LoggerFactory.getLogger(ClientController.class);

  private final ClientService clientService;

  public ClientController(ClientService clientService) {
    this.clientService = clientService;
  }

  /**
   * Método que consulta la lista de clientes
   *
   * @return List<Client>
   */
  @GetMapping("/getClients")
  public List<Client> getAllClients() {
    logger.info("Buscando todos los clients");
    return clientService.getAllClients();
  }

  /***
   * Método que consulta un cliente por medio de sharedKey
   * @param sharedKey
   * @return client
   */
  @GetMapping("/searchClient/{sharedKey}")
  public ResponseEntity<ApiResponse> getClientBySharedKey(@PathVariable String sharedKey) {
    logger.info("Buscando cliente por sharedKey: {}", sharedKey);
    Optional<Client> clientOptional = clientService.getClientBySharedKey(sharedKey);

    if (clientOptional.isPresent()) {
      return ResponseEntity.ok(new ApiResponse(true, "Cliente encontrado", clientOptional.get()));
    }
    Map<String, String> errorDetails = new HashMap<>();
    errorDetails.put("respondeCode", ConstantsResponseCode.NO_EXIST);
    return ResponseEntity.status(404)
        .body(
            new ApiResponse(
                false,
                "No existe el cliente consultado con el sharedKey :" + sharedKey,
                errorDetails));
  }

  /***
   * Método encargado de crear un nuevo Cliente
   * @param client
   * @return ResponseEntity<Client>
   */
  @PostMapping("/createClient")
  public ResponseEntity<ApiResponse> addClient(@RequestBody Client client) {
    logger.info("Agregando nuevo cliente con sharedKey: {}", client.getSharedKey());
    try {
      Client createdClient = clientService.addClient(client);
      return ResponseEntity.ok(new ApiResponse(true, "Cliente creado exitosamente", createdClient));
    } catch (IllegalArgumentException e) {
      Map<String, String> errorDetails = new HashMap<>();
      errorDetails.put("respondeCode", ConstantsResponseCode.DUPLICATE);
      return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage(), errorDetails));
    }
  }
}

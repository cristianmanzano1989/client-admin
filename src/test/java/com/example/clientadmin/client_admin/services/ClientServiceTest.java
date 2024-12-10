package com.example.clientadmin.client_admin.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.example.clientadmin.client_admin.entities.Client;
import com.example.clientadmin.client_admin.repositories.ClientRepository;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class ClientServiceTest {

  @Mock private ClientRepository clientRepository;

  @InjectMocks private ClientServiceImpl clientService;

  private ClientService service; // Declaración basada en la interfaz

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    service = clientService; // Asignar implementación a la interfaz
  }

  @Test
  void testGetAllClients_ReturnsClientList() {
    List<Client> mockClients =
        Arrays.asList(
            new Client(
                1L,
                "key1",
                "John Doe",
                "john.doe@example.com",
                "+57 31254569",
                LocalDate.of(2023, 1, 1),
                LocalDate.of(2023, 12, 31)),
            new Client(
                2L,
                "key2",
                "Jane Smith",
                "jane.smith@example.com",
                "+57 31456489",
                LocalDate.of(2023, 6, 1),
                LocalDate.of(2023, 12, 31)));

    when(clientRepository.findAll()).thenReturn(mockClients);

    List<Client> result = service.getAllClients(); // Uso de la interfaz

    assertNotNull(result);
    assertEquals(2, result.size());
    verify(clientRepository, times(1)).findAll();
  }

  @Test
  void testGetClientBySharedKey_WhenClientExists_ReturnsClient() {
    Client mockClient =
        new Client(
            5L,
            "key1",
            "John Doe",
            "john.doe@example.com",
            "+57 31254569",
            LocalDate.of(2023, 1, 1),
            LocalDate.of(2023, 12, 31));
    when(clientRepository.findBySharedKey("key1")).thenReturn(Optional.of(mockClient));

    Optional<Client> result = service.getClientBySharedKey("key1");

    assertTrue(result.isPresent());
    assertEquals("key1", result.get().getSharedKey());
    verify(clientRepository, times(1)).findBySharedKey("key1");
  }

  @Test
  void testGetClientBySharedKey_WhenClientDoesNotExist_ReturnsEmptyOptional() {
    when(clientRepository.findBySharedKey("nonexistent")).thenReturn(Optional.empty());

    Optional<Client> result = service.getClientBySharedKey("nonexistent");

    assertFalse(result.isPresent());
    verify(clientRepository, times(1)).findBySharedKey("nonexistent");
  }

  @Test
  void testAddClient_WhenClientIsNew_SavesClient() {
    Client mockClient =
        new Client(
            5L,
            "key1",
            "John Doe",
            "john.doe@example.com",
            "+57 31254569",
            LocalDate.of(2023, 1, 1),
            LocalDate.of(2023, 12, 31));

    when(clientRepository.existsBySharedKey("key1")).thenReturn(false);
    when(clientRepository.save(mockClient)).thenReturn(mockClient);

    Client result = service.addClient(mockClient); // Uso de la interfaz

    assertNotNull(result);
    assertEquals("key1", result.getSharedKey());
    verify(clientRepository, times(1)).existsBySharedKey("key1");
    verify(clientRepository, times(1)).save(mockClient);
  }

  @Test
  void testAddClient_WhenClientAlreadyExists_ThrowsException() {
    Client mockClient =
        new Client(
            5L,
            "key1",
            "John Doe",
            "john.doe@example.com",
            "+57 31254569",
            LocalDate.of(2023, 1, 1),
            LocalDate.of(2023, 12, 31));

    when(clientRepository.existsBySharedKey("key1")).thenReturn(true);

    IllegalArgumentException exception =
        assertThrows(
            IllegalArgumentException.class,
            () -> {
              service.addClient(mockClient); // Uso de la interfaz
            });

    assertEquals("El cliente con shared_key 'key1' ya existe.", exception.getMessage());
    verify(clientRepository, times(1)).existsBySharedKey("key1");
    verify(clientRepository, never()).save(mockClient);
  }
}

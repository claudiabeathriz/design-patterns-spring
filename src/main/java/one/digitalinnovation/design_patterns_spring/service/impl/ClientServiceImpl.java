package one.digitalinnovation.design_patterns_spring.service.impl;

import java.util.Optional;

import org.springframework.stereotype.Service;

import one.digitalinnovation.design_patterns_spring.model.Client;
import one.digitalinnovation.design_patterns_spring.repository.ClientRepository;
import one.digitalinnovation.design_patterns_spring.model.Address;
import one.digitalinnovation.design_patterns_spring.repository.AddressRepository;
import one.digitalinnovation.design_patterns_spring.service.ClientService;
import one.digitalinnovation.design_patterns_spring.service.ViaCepService;

@Service
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;
    private final AddressRepository addressRepository;
    private final ViaCepService viaCepService;

    public ClientServiceImpl(
            ClientRepository clientRepository,
            AddressRepository addressRepository,
            ViaCepService viaCepService) {

        this.clientRepository = clientRepository;
        this.addressRepository = addressRepository;
        this.viaCepService = viaCepService;
    }

    @Override
    public Iterable<Client> buscarTodos() {
        return clientRepository.findAll();
    }

    @Override
    public Client buscarPorId(Long id) {
        return clientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Client not found"));
    }

    @Override
    public void inserir(Client client) {
        salvarClienteComCep(client);
    }

    @Override
    public void atualizar(Long id, Client client) {
        Optional<Client> clientBd = clientRepository.findById(id);

        if (clientBd.isPresent()) {
            client.setId(id);
            salvarClienteComCep(client);
        } else {
            throw new RuntimeException("Client not found");
        }
    }

    @Override
    public void deletar(Long id) {
        clientRepository.deleteById(id);
    }

    private void salvarClienteComCep(Client client) {

        if (client.getAddress() == null) {
            throw new RuntimeException("Address is required");
        }

        String cep = client.getAddress().getCep();

        Address address = addressRepository.findById(cep)
                .orElseGet(() -> {
                    Address newAddress = viaCepService.consultarCep(cep);
                    addressRepository.save(newAddress);
                    return newAddress;
                });

        client.setAddress(address);
        clientRepository.save(client);
    }
}

package com.example.soapImpl;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.jws.WebService;

import com.example.daoImpl.ClientDao;
import com.example.dto.ClientDTO;
import com.example.models.Client;
import com.example.soap.ClientService;

@WebService(endpointInterface = "com.example.soap.ClientService")
public class ClientServiceImpl implements ClientService {
	
	private ClientDao clientDao;
	
	@PostConstruct
	public void init() {
		clientDao = new ClientDao();
	}
	
	@Override
	public List<Client> getAllClients() {
		return clientDao.getAll();
	}

	@Override
	public int createNewClient(ClientDTO client) {
		Client clientToInsert = new Client(client.getFirstName(), client.getSurname());
		return clientDao.add(clientToInsert);
	}

	@Override
	public int deleteClient(long id) {
		Client c = new Client();
		c.setClientId(id);
		return clientDao.delete(c);
	}

	@Override
	public int updateClient(long id, ClientDTO client) {
		Client c = clientDao.get(id);
		if(!client.getFirstName().equals("") || !client.getFirstName().equals("?"))
			c.setFirstName(client.getFirstName());
		
		if(!client.getSurname().equals("") || !client.getSurname().equals("?"))
			c.setSurname(client.getSurname());

		return clientDao.update(c);
	}

	@Override
	public List<Client> getClientsWhosSurnameBeginsWith(String s) {
		return clientDao.getClientsWhosSurnameBeginsWith(s);
	}
}


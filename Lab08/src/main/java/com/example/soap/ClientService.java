package com.example.soap;

import java.util.List;

import javax.jws.WebParam;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlElement;

import com.example.dto.ClientDTO;
import com.example.models.Client;

@WebService
public interface ClientService {
    List<Client> getAllClients();
    int createNewClient(ClientDTO client);
    int deleteClient(@WebParam(name="id") @XmlElement(required=true) long id);
    int updateClient(@WebParam(name="id") @XmlElement(required=true) long id, ClientDTO client);
    List<Client> getClientsWhosSurnameBeginsWith(@WebParam(name="letter") @XmlElement(required=true)String s);
}


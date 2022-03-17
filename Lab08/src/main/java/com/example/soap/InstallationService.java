package com.example.soap;

import java.util.List;

import javax.jws.WebParam;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlElement;

import com.example.dto.InstallationDTO;
import com.example.models.Installation;

@WebService
public interface InstallationService {
	List<Installation> getAllInstallations();
	List<Installation> getAllInstallationsByClientId(@WebParam(name="id") @XmlElement(required=true)long id);
    int createNewInstallation(InstallationDTO installation);
    int deleteInstallation(@WebParam(name="id") @XmlElement(required=true) long id);
    int updateInstallation(@WebParam(name="id") @XmlElement(required=true) long id, InstallationDTO installation);
}

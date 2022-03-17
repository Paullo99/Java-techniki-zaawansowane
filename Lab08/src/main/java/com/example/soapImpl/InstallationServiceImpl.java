package com.example.soapImpl;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.jws.WebService;

import com.example.daoImpl.ClientDao;
import com.example.daoImpl.InstallationDao;
import com.example.daoImpl.PriceListDao;
import com.example.dto.InstallationDTO;
import com.example.models.Installation;
import com.example.soap.InstallationService;

@WebService(endpointInterface = "com.example.soap.InstallationService")
public class InstallationServiceImpl implements InstallationService {

	private InstallationDao installationDao;
	private PriceListDao priceListDao;
	private ClientDao clientDao;

	@PostConstruct
	public void init() {
		installationDao = new InstallationDao();
		priceListDao = new PriceListDao();
		clientDao = new ClientDao();
	}

	@Override
	public List<Installation> getAllInstallations() {
		return installationDao.getAll();
	}

	@Override
	public int createNewInstallation(InstallationDTO installation) {
		Installation installationToInsert = new Installation(installation.getRouterNumber(), installation.getCity(),
				installation.getAddress(), installation.getPostcode(), clientDao.get(installation.getClientId()),
				priceListDao.get(installation.getPriceListId()));
		return installationDao.add(installationToInsert);
	}

	@Override
	public int deleteInstallation(long id) {
		Installation i = new Installation();
		i.setInstallationId(id);
		return installationDao.delete(i);
	}

	@Override
	public int updateInstallation(long id, InstallationDTO installation) {
		Installation i = installationDao.get(id);

		if(!installation.getAddress().equals("") && !installation.getAddress().equals("?"))
			i.setAddress(installation.getAddress());
		
		if(!installation.getCity().equals("") && !installation.getCity().equals("?"))
			i.setCity(installation.getCity());
		
		if(!installation.getPostcode().equals("") && !installation.getPostcode().equals("?"))
			i.setPostcode(installation.getPostcode());
		
		if(!installation.getRouterNumber().equals("") && !installation.getRouterNumber().equals("?"))
			i.setRouterNumber(installation.getRouterNumber());

		return installationDao.update(i);
	}

	@Override
	public List<Installation> getAllInstallationsByClientId(long id) {
		return installationDao.getAllInstallationsByClientId(id);
	}

}

package com.example.soapImpl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.jws.WebService;

import com.example.daoImpl.ChargeDao;
import com.example.daoImpl.InstallationDao;
import com.example.dto.ChargeDTO;
import com.example.models.Charge;
import com.example.soap.ChargeService;

@WebService(endpointInterface = "com.example.soap.ChargeService")
public class ChargeServiceImpl implements ChargeService{
	
	private ChargeDao chargeDao;
	private InstallationDao installationDao;
	
	@PostConstruct
	public void init() {
		chargeDao = new ChargeDao();
		installationDao = new InstallationDao();
	}

	@Override
	public List<Charge> getAllCharges() {
		return chargeDao.getAll();
	}

	@Override
	public int createNewCharge(ChargeDTO charge) {
		Charge chargetoInsert = new Charge(LocalDate.parse(charge.getDeadline()), charge.getAmount(),
				installationDao.get(charge.getInstallationId()));
		return chargeDao.add(chargetoInsert);
	}

	@Override
	public int deleteCharge(long id) {
		Charge c = new Charge();
		c.setChargeId(id);
		return chargeDao.delete(c);
	}

	@Override
	public int updateCharge(long id, ChargeDTO charge) {
		Charge c = chargeDao.get(id);

		if(!charge.getDeadline().equals("") && !charge.getDeadline().equals("?"))
			c.setDeadline(LocalDate.parse(charge.getDeadline()));
		
		return chargeDao.update(c);
	}

	@Override
	public List<ChargeDTO> getAllChargesBetweenDatesFromClient(long id, String fromDate, String toDate) {
		List<ChargeDTO> chargesToReturn = new ArrayList<>();
		for(Charge c :  chargeDao.getAllChargesBetweenDatesFromClient(id, fromDate, toDate)){
			chargesToReturn.add(new ChargeDTO(c.getDeadline().toString(), c.getAmount(), c.getInstallation().getInstallationId()));
		}
		return chargesToReturn;
	}

}

package com.example.soapImpl;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.jws.WebService;

import com.example.daoImpl.PriceListDao;
import com.example.dto.PriceListDTO;
import com.example.models.PriceList;
import com.example.soap.PriceListService;

@WebService(endpointInterface = "com.example.soap.PriceListService")
public class PriceListServiceImpl implements PriceListService{
	
	private PriceListDao priceListDao;
	
	@PostConstruct
	public void init() {
		priceListDao = new PriceListDao();
	}

	@Override
	public List<PriceList> getAllPriceLists() {
		return priceListDao.getAll();
	}

	@Override
	public int createNewPriceList(PriceListDTO priceList) {
		PriceList priceListToInsert = new PriceList(priceList.getServiceType(), priceList.getPrice());
		return priceListDao.add(priceListToInsert);
	}

	@Override
	public int deletePriceList(long id) {
		PriceList p = new PriceList();
		p.setPriceListId(id);
		return priceListDao.delete(p);
	}

	@Override
	public int updatePriceList(long id, PriceListDTO priceList) {
		PriceList p = priceListDao.get(id);
		
		if(!priceList.getServiceType().equals("") || !priceList.getServiceType().equals("?"))
			p.setServiceType(priceList.getServiceType());

		return priceListDao.update(p);
	}

}

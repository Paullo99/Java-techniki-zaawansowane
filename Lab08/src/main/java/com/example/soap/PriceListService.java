package com.example.soap;

import java.util.List;

import javax.jws.WebParam;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlElement;

import com.example.dto.PriceListDTO;
import com.example.models.PriceList;

@WebService
public interface PriceListService {
	List<PriceList> getAllPriceLists();
    int createNewPriceList(PriceListDTO priceList);
    int deletePriceList(@WebParam(name="id") @XmlElement(required=true) long id);
    int updatePriceList(@WebParam(name="id") @XmlElement(required=true) long id, PriceListDTO priceList);
}

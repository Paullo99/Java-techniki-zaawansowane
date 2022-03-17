package com.example.soap;

import java.util.List;

import javax.jws.WebParam;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlElement;

import com.example.dto.ChargeDTO;
import com.example.models.Charge;

@WebService
public interface ChargeService {
    List<Charge> getAllCharges();
    int createNewCharge(ChargeDTO charge);
    int deleteCharge(@WebParam(name="id") @XmlElement(required=true) long id);
    int updateCharge(@WebParam(name="id") @XmlElement(required=true) long id, ChargeDTO charge);
    List<ChargeDTO>getAllChargesBetweenDatesFromClient(@WebParam(name="id") @XmlElement(required=true)long id, 
    		@WebParam(name="fromDate") @XmlElement(required=true)String fromDate, @WebParam(name="toDate") @XmlElement(required=true)String toDate);
}

package com.example.soapImpl;

import java.time.LocalDate;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.jws.WebService;

import com.example.daoImpl.InstallationDao;
import com.example.daoImpl.PaymentDao;
import com.example.dto.PaymentDTO;
import com.example.models.Payment;
import com.example.soap.PaymentService;

@WebService(endpointInterface = "com.example.soap.PaymentService")
public class PaymentServiceImpl implements PaymentService{
	
	private PaymentDao paymentDao;
	private InstallationDao installationDao;
	
	@PostConstruct
	public void init() {
		paymentDao = new PaymentDao();
		installationDao = new InstallationDao();
	}

	@Override
	public List<Payment> getAllPayments() {
		return paymentDao.getAll();
	}

	@Override
	public int createNewPayment(PaymentDTO payment) {
		Payment paymentToInsert = new Payment(LocalDate.parse(payment.getPaymentDate()), payment.getPaymentAmount(),
				installationDao.get(payment.getInstallationId()));
		return paymentDao.add(paymentToInsert);
	}

	@Override
	public int deletePayment(long id) {
		Payment p = new Payment();
		p.setPaymentId(id);
		return paymentDao.delete(p);
	}

	@Override
	public int updatePayment(long id, PaymentDTO payment) {
		Payment p = paymentDao.get(id);

		if(!payment.getPaymentDate().equals("") && !payment.getPaymentDate().equals("?"))
			p.setPaymentDate(LocalDate.parse(payment.getPaymentDate()));
		
		return paymentDao.update(p);
	}

}

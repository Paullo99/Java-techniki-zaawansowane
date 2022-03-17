package com.example.daoImpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.sqlite.SQLiteDataSource;

import com.example.daoApi.Dao;
import com.example.models.Payment;

public class PaymentDao implements Dao<Payment>{

	private Connection connection;
	private InstallationDao installationDao;
	
	public PaymentDao() {
		this.installationDao = new InstallationDao();
	}
	
	@Override
	public Payment get(long id) {
		String sql = "SELECT * FROM payment WHERE payment_id=?";
		Payment payment = null;
		
		try {
			connect();
			PreparedStatement pstmt = connection.prepareStatement(sql);
			pstmt.setLong(1, id);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				payment = new Payment(id, LocalDate.parse(rs.getString("payment_date")),
						rs.getFloat("payment_amount"), installationDao.get(rs.getLong("installation_id")));			
				}
			pstmt.close();
			disconnect();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return payment;
	}

	@Override
	public List<Payment> getAll() {
		String sql = "SELECT * FROM payment";
		List<Payment> payments = new ArrayList<>();
		
		try {
			connect();
			Statement statement = connection.createStatement();
			ResultSet rs = statement.executeQuery(sql);
			while(rs.next()) {
				payments.add(new Payment(rs.getLong("payment_id"), LocalDate.parse(rs.getString("payment_date")),
						rs.getFloat("payment_amount"), installationDao.get(rs.getLong("installation_id"))));
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return payments;
	}

	@Override
	public int add(Payment payment) {
		String sql = "INSERT INTO payment(payment_date, payment_amount, installation_id) VALUES (?,?,?)";

		int added = 0;
		
		try {
			connect();
			PreparedStatement pstmt = connection.prepareStatement(sql);
			pstmt.setString(1, payment.getPaymentDate().toString());
			pstmt.setFloat(2, payment.getPaymentAmount());
			pstmt.setLong(3, payment.getInstallation().getInstallationId());
			added  = pstmt.executeUpdate();
			pstmt.close();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return added;
	}

	@Override
	public int update(Payment payment) {
		String sql = "UPDATE payment SET payment_date = ?, payment_amount = ?, installation_id = ?" + "WHERE payment_id = ?";
		int modified = 0;
		try {
			connect();
			PreparedStatement pstmt = connection.prepareStatement(sql);
			pstmt.setString(1, payment.getPaymentDate().toString());
			pstmt.setFloat(2, payment.getPaymentAmount());
			pstmt.setLong(3, payment.getInstallation().getInstallationId());
			pstmt.setLong(4, payment.getPaymentId());
			modified  = pstmt.executeUpdate();
			pstmt.close();
			disconnect();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return modified;
	}

	@Override
	public int delete(Payment payment) {
		String sql = "DELETE FROM payment WHERE payment_id=?";
		int deleted = 0;
		try {
			connect();
			PreparedStatement pstmt = connection.prepareStatement(sql);
			pstmt.setLong(1, payment.getPaymentId());
			deleted = pstmt.executeUpdate();
			disconnect();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return deleted;
	}
	
	private void connect() throws SQLException {
		SQLiteDataSource dataSource = new SQLiteDataSource();
		dataSource.setUrl("jdbc:sqlite:D:\\Studia Paulo\\Semestr_6\\Programowanie w Javie\\Laby - Eclipse Workspace\\Lab08CXF\\isp-sqlite.db");
		connection = dataSource.getConnection();
	}
	
	private void disconnect() throws SQLException {
		if(connection==null)
			return;
		connection.close();
		connection = null;
	}

}

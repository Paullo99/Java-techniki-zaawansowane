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
import com.example.models.Charge;

public class ChargeDao implements Dao<Charge>{

	private Connection connection;
	private InstallationDao installationDao;
	
	public ChargeDao() {
		this.installationDao = new InstallationDao();
	}
	
	@Override
	public Charge get(long id) {
		String sql = "SELECT * FROM charge WHERE charge_id=?";
		Charge charge = null;
		
		try {
			connect();
			PreparedStatement pstmt = connection.prepareStatement(sql);
			pstmt.setLong(1, id);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				charge = new Charge(id, LocalDate.parse(rs.getString("deadline")),
						rs.getFloat("amount"), installationDao.get(rs.getLong("installation_id")));			
				}
			pstmt.close();
			disconnect();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return charge;
	}

	@Override
	public List<Charge> getAll() {
		String sql = "SELECT * FROM charge";
		List<Charge> charges = new ArrayList<>();
		
		try {
			connect();
			Statement statement = connection.createStatement();
			ResultSet rs = statement.executeQuery(sql);
			while(rs.next()) {
				charges.add(new Charge(rs.getLong("charge_id"), LocalDate.parse(rs.getString("deadline")),
						rs.getFloat("amount"), installationDao.get(rs.getLong("installation_id"))));
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return charges;
	}

	@Override
	public int add(Charge charge) {
		String sql = "INSERT INTO charge(deadline, amount, installation_id) VALUES (?,?,?)";
		int added = 0;
		try {
			connect();
			PreparedStatement pstmt = connection.prepareStatement(sql);
			pstmt.setString(1, charge.getDeadline().toString());
			pstmt.setFloat(2, charge.getAmount());
			pstmt.setLong(3, charge.getInstallation().getInstallationId());
			added = pstmt.executeUpdate();
			pstmt.close();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return added;
	}

	@Override
	public int update(Charge charge) {
		String sql = "UPDATE charge SET deadline = ?, amount = ?, installation_id = ?" + "WHERE charge_id = ?";
		int modified = 0;
		try {
			connect();
			PreparedStatement pstmt = connection.prepareStatement(sql);
			pstmt.setString(1, charge.getDeadline().toString());
			pstmt.setFloat(2, charge.getAmount());
			pstmt.setLong(3, charge.getInstallation().getInstallationId());
			pstmt.setLong(4, charge.getChargeId());
			modified  = pstmt.executeUpdate();
			pstmt.close();
			disconnect();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return modified;
	}

	@Override
	public int delete(Charge charge) {
		String sql = "DELETE FROM charge WHERE charge_id=?";
		int deleted = 0;
		try {
			connect();
			PreparedStatement pstmt = connection.prepareStatement(sql);
			pstmt.setLong(1, charge.getChargeId());
			deleted = pstmt.executeUpdate();
			disconnect();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return deleted;
	}
	
	public List<Charge> getAllChargesBetweenDatesFromClient(long id, String fromDate, String toDate){
		String sql = "SELECT * FROM charge c INNER JOIN installation inst ON c.installation_id = inst.installation_id "
				+ "WHERE inst.client_id = ? AND (c.deadline BETWEEN ? AND ?)";
		List<Charge> charges = new ArrayList<>();
		try {
			connect();
			PreparedStatement pstmt = connection.prepareStatement(sql);
			pstmt.setLong(1, id);
			pstmt.setString(2, fromDate);
			pstmt.setString(3, toDate);
			ResultSet rs  = pstmt.executeQuery();
			while(rs.next()) {
				charges.add(new Charge(rs.getLong("charge_id"), LocalDate.parse(rs.getString("deadline")),
						rs.getFloat("amount"), installationDao.get(rs.getLong("installation_id"))));
			}
			pstmt.close();
			disconnect();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return charges;
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

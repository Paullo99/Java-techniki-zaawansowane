package com.example.daoImpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.sqlite.SQLiteDataSource;

import com.example.daoApi.Dao;
import com.example.models.Installation;

public class InstallationDao implements Dao<Installation>{
	
	private Connection connection;
	private ClientDao clientDao;
	private PriceListDao priceListDao;
	
	public InstallationDao() {
		this.clientDao = new ClientDao();
		this.priceListDao = new PriceListDao();
	}

	@Override
	public Installation get(long id) {
		String sql = "SELECT * FROM installation WHERE installation_id=?";
		Installation installation = null;
		
		try {
			connect();
			PreparedStatement pstmt = connection.prepareStatement(sql);
			pstmt.setLong(1, id);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				installation = new Installation(id, rs.getString("router_number"),
						rs.getString("city"), rs.getString("address"),
						rs.getString("postcode"), clientDao.get(rs.getLong("client_id")),
						priceListDao.get(rs.getLong("price_list_id")));
			}
			pstmt.close();
			disconnect();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return installation;
	}

	@Override
	public List<Installation> getAll() {
		String sql = "SELECT * FROM installation";
		List<Installation> installations = new ArrayList<>();
		
		try {
			connect();
			Statement statement = connection.createStatement();
			ResultSet rs = statement.executeQuery(sql);
			while(rs.next()) {
				installations.add(new Installation(rs.getLong("installation_id"), rs.getString("router_number"),
						rs.getString("city"), rs.getString("address"),
						rs.getString("postcode"), clientDao.get(rs.getLong("client_id")),
						priceListDao.get(rs.getLong("price_list_id"))));
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return installations;
	}

	@Override
	public void add(Installation installation) {
		String sql = "INSERT INTO installation(router_number, city, address,"
				+ "postcode, client_id, price_list_id) VALUES (?,?,?,?,?,?)";

		try {
			connect();
			PreparedStatement pstmt = connection.prepareStatement(sql);
			pstmt.setString(1, installation.getRouterNumber());
			pstmt.setString(2, installation.getCity());
			pstmt.setString(3, installation.getAddress());
			pstmt.setString(4, installation.getPostcode());
			pstmt.setLong(5, installation.getClient().getClientId());
			pstmt.setLong(6, installation.getPriceList().getPriceListId());
			pstmt.executeUpdate();
			pstmt.close();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	@Override
	public int update(Installation installation) {
		String sql = "UPDATE price_list SET router_number = ?, city = ?, address = ?, "
				+ "postcode = ?, client_id = ?, price_list_id = ?" + "WHERE installation_id = ?";
		int modified = 0;
		try {
			connect();
			PreparedStatement pstmt = connection.prepareStatement(sql);
			pstmt.setString(1, installation.getRouterNumber());
			pstmt.setString(2, installation.getCity());
			pstmt.setString(3, installation.getAddress());
			pstmt.setString(4, installation.getPostcode());
			pstmt.setLong(5, installation.getClient().getClientId());
			pstmt.setLong(6, installation.getPriceList().getPriceListId());
			pstmt.setLong(6, installation.getInstallationId());
			modified  = pstmt.executeUpdate();
			pstmt.close();
			disconnect();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return modified;
	}

	@Override
	public int delete(Installation installation) {
		String sql = "DELETE FROM installation WHERE installation_id=?";
		int deleted = 0;
		try {
			connect();
			PreparedStatement pstmt = connection.prepareStatement(sql);
			pstmt.setLong(1, installation.getInstallationId());
			deleted = pstmt.executeUpdate();
			disconnect();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return deleted;
	}
	
	private void connect() throws SQLException {
		SQLiteDataSource dataSource = new SQLiteDataSource();
		dataSource.setUrl("jdbc:sqlite:isp-sqlite.db");
		connection = dataSource.getConnection();
	}
	
	private void disconnect() throws SQLException {
		if(connection==null)
			return;
		connection.close();
		connection = null;
	}

}

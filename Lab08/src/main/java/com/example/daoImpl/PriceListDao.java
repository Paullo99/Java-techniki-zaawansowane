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
import com.example.models.PriceList;

public class PriceListDao implements Dao<PriceList>{
	
	private Connection connection = null;

	@Override
	public PriceList get(long id) {
		String sql = "SELECT price_list_id, service_type, price FROM price_list WHERE price_list_id=?";
		PriceList priceList = null;
		
		try {
			connect();
			PreparedStatement pstmt = connection.prepareStatement(sql);
			pstmt.setLong(1, id);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				priceList = new PriceList(id, rs.getString("service_type"), rs.getFloat("price"));
			}
			pstmt.close();
			disconnect();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return priceList;
	}

	@Override
	public List<PriceList> getAll() {
		String sql = "SELECT price_list_id, service_type, price FROM price_list";
		List<PriceList> priceLists = new ArrayList<>();
		
		try {
			connect();
			Statement statement = connection.createStatement();
			ResultSet rs = statement.executeQuery(sql);
			while(rs.next()) {
				priceLists.add(new PriceList(rs.getLong("price_list_id"), rs.getString("service_type"), rs.getFloat("price")));
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return priceLists;
	}

	@Override
	public int add(PriceList priceList) {
		String sql = "INSERT INTO price_list(service_type, price) VALUES (?,?)";

		int added = 0;
		
		try {
			connect();
			PreparedStatement pstmt = connection.prepareStatement(sql);
			pstmt.setString(1, priceList.getServiceType());
			pstmt.setFloat(2, priceList.getPrice());
			added = pstmt.executeUpdate();
			pstmt.close();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return added;
	}

	@Override
	public int update(PriceList priceList) {
		String sql = "UPDATE price_list SET service_type = ?, price = ? " + "WHERE price_list_id = ?";
		int modified = 0;
		try {
			connect();
			PreparedStatement pstmt = connection.prepareStatement(sql);
			pstmt.setString(1, priceList.getServiceType());
			pstmt.setFloat(2, priceList.getPrice());
			pstmt.setLong(3, priceList.getPriceListId());
			modified  = pstmt.executeUpdate();
			pstmt.close();
			disconnect();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return modified;
	}

	@Override
	public int delete(PriceList priceList) {
		String sql = "DELETE FROM price_list WHERE price_list_id=?";
		int deleted = 0;
		try {
			connect();
			PreparedStatement pstmt = connection.prepareStatement(sql);
			pstmt.setLong(1, priceList.getPriceListId());
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

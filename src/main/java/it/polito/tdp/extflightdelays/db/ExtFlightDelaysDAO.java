package it.polito.tdp.extflightdelays.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import it.polito.tdp.extflightdelays.model.Airline;
import it.polito.tdp.extflightdelays.model.Airport;
import it.polito.tdp.extflightdelays.model.CoppiaId;
import it.polito.tdp.extflightdelays.model.Flight;

public class ExtFlightDelaysDAO {

	public List<Airline> loadAllAirlines() {
		String sql = "SELECT * from airlines";
		List<Airline> result = new ArrayList<Airline>();

		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				result.add(new Airline(rs.getInt("ID"), rs.getString("IATA_CODE"), rs.getString("AIRLINE")));
			}

			conn.close();
			return result;

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
	}

	public List<Airport> loadAllAirports() {
		String sql = "SELECT * FROM airports";
		List<Airport> result = new ArrayList<Airport>();

		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				Airport airport = new Airport(rs.getInt("ID"), rs.getString("IATA_CODE"), rs.getString("AIRPORT"),
						rs.getString("CITY"), rs.getString("STATE"), rs.getString("COUNTRY"), rs.getDouble("LATITUDE"),
						rs.getDouble("LONGITUDE"), rs.getDouble("TIMEZONE_OFFSET"));
				result.add(airport);
			}

			conn.close();
			return result;

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
	}
	/**
	 * Data una distanza minima, ritorna tutti gli aereoporti connessi con
	 * almeno una tratta di almeno quella distanza
	 * @param distanza
	 * @return
	 */
	public List<Airport> getAirportsConnected(int distanza) {
		String sql = "WITH listaAereoporti "
				+ "AS "
				+ "	( "
				+ "		SELECT f.ORIGIN_AIRPORT_ID AS ap,  AVG(f.DISTANCE) AS n "
				+ "		FROM flights f, airports a "
				+ "		WHERE a.ID=f.ORIGIN_AIRPORT_ID "
				+ "		GROUP BY f.ORIGIN_AIRPORT_ID, f.DESTINATION_AIRPORT_ID "
				+ "	) "
				+ "SELECT	DISTINCT a.ID,a.IATA_CODE,a.AIRPORT,a.CITY,a.STATE,a.COUNTRY,a.LATITUDE,a.LONGITUDE,a.TIMEZONE_OFFSET "
				+ "FROM listaAereoporti la, airports a "
				+ "WHERE n>=? AND la.ap=a.ID";
		
		List<Airport> result = new ArrayList<Airport>();

		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, distanza);
		
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				Airport airport = new Airport(rs.getInt("ID"), rs.getString("IATA_CODE"), rs.getString("AIRPORT"),
						rs.getString("CITY"), rs.getString("STATE"), rs.getString("COUNTRY"), rs.getDouble("LATITUDE"),
						rs.getDouble("LONGITUDE"), rs.getDouble("TIMEZONE_OFFSET"));
				result.add(airport);
			}

			conn.close();
			return result;

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
	}
	
	

	public List<Flight> loadAllFlights() {
		String sql = "SELECT * FROM flights";
		List<Flight> result = new LinkedList<Flight>();

		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				Flight flight = new Flight(rs.getInt("ID"), rs.getInt("AIRLINE_ID"), rs.getInt("FLIGHT_NUMBER"),
						rs.getString("TAIL_NUMBER"), rs.getInt("ORIGIN_AIRPORT_ID"),
						rs.getInt("DESTINATION_AIRPORT_ID"),
						rs.getTimestamp("SCHEDULED_DEPARTURE_DATE").toLocalDateTime(), rs.getDouble("DEPARTURE_DELAY"),
						rs.getDouble("ELAPSED_TIME"), rs.getInt("DISTANCE"),
						rs.getTimestamp("ARRIVAL_DATE").toLocalDateTime(), rs.getDouble("ARRIVAL_DELAY"));
				result.add(flight);
			}

			conn.close();
			return result;

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
	}
	/**
	 * Data una distanza, ritorna tutte le coppie di id di aereoporti con una media distanza maggiore o uguale del parametro
	 * @param distanza
	 * @return
	 */
	public List<CoppiaId> getCoppieId(int distanza){
		String sql = "WITH listaVoli "
				+ "AS "
				+ "	( "
				+ "	SELECT f.ORIGIN_AIRPORT_ID AS oid, f.DESTINATION_AIRPORT_ID AS did, AVG(f.DISTANCE) AS n "
				+ "	FROM flights f "
				+ "	GROUP BY f.ORIGIN_AIRPORT_ID, f.DESTINATION_AIRPORT_ID "
				+ "	) "
				+ "SELECT oid, did, n "
				+ "FROM listaVoli "
				+ "WHERE n>=?";
		List<CoppiaId> result = new ArrayList<CoppiaId>();
		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, distanza);
			ResultSet rs = st.executeQuery();
			
			while(rs.next()) {
				CoppiaId cid = new CoppiaId(rs.getInt("oid"), rs.getInt("did"), rs.getInt("n"));
				result.add(cid);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
	}
}

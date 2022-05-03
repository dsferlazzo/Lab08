package it.polito.tdp.extflightdelays.db;

public class TestDAO {

	public static void main(String[] args) {

		ExtFlightDelaysDAO dao = new ExtFlightDelaysDAO();

		System.out.println(dao.loadAllAirlines());
		System.out.println(dao.loadAllAirports());
		System.out.println(dao.loadAllFlights().size());
		System.out.println(dao.getAirportsConnected(500).size());
	}

}

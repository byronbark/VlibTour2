/**
This file is part of the course CSC5002.

Copyright (C) 2017 Télécom SudParis

This is free software: you can redistribute it and/or modify
it under the terms of the GNU Lesser General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This software platform is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public License
along with the muDEBS platform. If not, see <http://www.gnu.org/licenses/>.

Initial developer(s): Chantal Taconet
Contributor(s): Denis Conan
				Rafael Blanco
				Zujany Salazar
 */
package vlibtour.vlibtour_bikestation.client;
import vlibtour.vlibtour_bikestation.client.generated_from_json.Position;
import vlibtour.vlibtour_bikestation.client.generated_from_json.Station;
import vlibtour.vlibtour_bikestation.emulatedserver.Stations;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.util.Properties;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;

import java.util.Arrays;
import java.util.List;

import vlibtour.vlibtour_visit_emulation.GPSPosition;


/**
 * The bike station REST client example.
 */
public final class BikeStationClient {

	private static String restURI;
	private static WebTarget service;
	private static Properties properties;
	
	/**
	 * utility class with no instance.
	 */
	public BikeStationClient() throws IOException {
		properties = new Properties();
		FileInputStream input = new FileInputStream("src/main/resources/rest.properties");
		properties.load(input);
		// restURI = "http://" + properties.getProperty("rest.serveraddress") + "/MyServer";
		restURI = properties.getProperty("jcdecaux.rooturl");
		Client client = ClientBuilder.newClient();
		URI uri = UriBuilder.fromUri(restURI).build();
		service = client.target(uri);
	}

	public BikeStationClient(final String url) throws IOException {
		properties = new Properties();
		restURI = url;
		Client client = ClientBuilder.newClient();
		URI uri = UriBuilder.fromUri(restURI).build();
		service = client.target(uri);
	}

	/**
	 * the main method.
	 * 
	 * @param args
	 *            there is no command line arguments.
	 * @throws IOException
	 *             problem with HTTP connection.
	 */
	public static void main(final String[] args) throws IOException {
		BikeStationClient station = new BikeStationClient();
		// https://api.jcdecaux.com/vls/v1/stations?contract=lyon&apiKey=91f170cdabb4c3227116c3e871a63e8d3ad148ee
		try {
			System.out.println("Example 1 from website, Get one station information selected with its number: \n"+ station.getStationById(2010).toString());		
			System.out.println("\nExample 2 from website, Get all station information: \n"+ station.getAllStation().toString());
			System.out.println("\nATTENTION, The following examples corresponds to the step 4 and they works only when the information of the stations come from the local REST server.");
			System.out.println("\nExample 3, station near (300 m) from GPS position of station 2010: \n" + station.findBikestationsNearGPS(48.860854, 2.335812, 300));
			System.out.println("\nExample 4, station near (500 m) from POI Pyramide du Louvre to return 10 bikes: \n" + station.findStationToReturn(48.860854, 2.335812, 500, 10));
			System.out.println("\nExample 5, station near (500 m) from POI Musée Grévin to bring 5 bikes: \n" + station.findStationToGetBikes(48.8718378, 2.3422204, 500, 5));
			System.out.println("\nExample 6, availability of station near (500 m) from POI Musée Grévin: \n" + station.getAvailabilityNearPOI(48.8718378, 2.3422204, 500));
		} catch (Exception e) {
			System.out.println("FAIL!!");
			System.out.println(e);
		}
	}

    /**
     * Get the information of a single station.
     * 
     * @param stationId
     *            station's id.
     * @return Information of the station.
     */
	public static Station getStationById(final long stationId){
		return service.path("stations/"+Long.toString(stationId)).queryParam("contract", properties.getProperty("jcdecaux.contract")).queryParam("apiKey", properties.getProperty("jcdecaux.apiKey")).request().accept(MediaType.APPLICATION_JSON).get(Station.class);
	}

    /**
     * Get the information of all the stations.
     * 
     * @return Array with the information of all the stations.
     */
	public static Stations getAllStation(){
		List stationList;
		stationList = Arrays.asList(service.path("stations").queryParam("contract", properties.getProperty("jcdecaux.contract")).queryParam("apiKey", properties.getProperty("jcdecaux.apiKey")).request().accept(MediaType.APPLICATION_JSON).get(Station[].class));
		Stations stations= new Stations(stationList);
		return stations;
	}

    /**
     * Get all the stations near a GPS location.
     * 
     * @param latitude
     *            latitude of GPS location.
     * @param longitude
     *            logitude of GPS location.
     * @param distance
     *            maximun radius to search. 
     * @return stations near a GPS location.
     */
	public static Stations findBikestationsNearGPS(final double latitude, final double longitude, final double distance){
		return service.path("stations/findBikestationsNearGPS").queryParam("lat", latitude).queryParam("long", longitude).queryParam("distance", distance).queryParam("contract", properties.getProperty("jcdecaux.contract")).queryParam("apiKey", properties.getProperty("jcdecaux.apiKey")).request().accept(MediaType.APPLICATION_JSON).get(Stations.class);
	}
    /**
     * Get a station with availables stands to return a number of bikes.
     * 
     * @param latitude
     *            latitude of POI.
     * @param longitude
     *            logitude of POI. 
     * @param distance
     *            maximun radius to search.  
     * @param groupSize
     *            number of bikes to return. 
     * @return station.
     */
	public static Station findStationToReturn(final double latitude, final double longitude, final double distance, final int groupSize){
		return service.path("stations/findStationToReturn").queryParam("lat", latitude).queryParam("long", longitude).queryParam("distance", distance).queryParam("groupSize", groupSize).queryParam("contract", properties.getProperty("jcdecaux.contract")).queryParam("apiKey", properties.getProperty("jcdecaux.apiKey")).request().accept(MediaType.APPLICATION_JSON).get(Station.class);
	}
    /**
     * Get a station with availables bikes to bring.
     * 
     * @param latitude
     *            latitude of POI.
     * @param longitude
     *            logitude of POI. 
     * @param distance
     *            maximun radius to search.  
     * @param groupSize
     *            number of bikes to bring. 
     * @return station.
     */
	public static Station findStationToGetBikes(final double latitude, final double longitude, final double distance, final int groupSize){
		return service.path("stations/findStationToGetBikes").queryParam("lat", latitude).queryParam("long", longitude).queryParam("distance", distance).queryParam("groupSize", groupSize).queryParam("contract", properties.getProperty("jcdecaux.contract")).queryParam("apiKey", properties.getProperty("jcdecaux.apiKey")).request().accept(MediaType.APPLICATION_JSON).get(Station.class);
	}
    /**
     * Get availability of the stations near a GPS location.
     * 
     * @param latitude
     *            latitude of GPS location.
     * @param longitude
     *            logitude of GPS location.
     * @param distance
     *            maximun radius to search. 
     * @return availability of stations near a GPS location.
     */
	public static String getAvailabilityNearPOI(final double latitude, final double longitude, final double distance){
		return service.path("stations/getAvailabilityNearPOI").queryParam("lat", latitude).queryParam("long", longitude).queryParam("distance", distance).queryParam("contract", properties.getProperty("jcdecaux.contract")).queryParam("apiKey", properties.getProperty("jcdecaux.apiKey")).request().accept(MediaType.TEXT_PLAIN).get(String.class);
	}
}

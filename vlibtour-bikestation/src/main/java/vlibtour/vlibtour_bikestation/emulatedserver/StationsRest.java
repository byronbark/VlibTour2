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
package vlibtour.vlibtour_bikestation.emulatedserver;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.JAXBException;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Collections;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.DELETE;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import vlibtour.vlibtour_bikestation.emulatedserver.generated_from_json.Position;
import vlibtour.vlibtour_bikestation.emulatedserver.generated_from_json.Station;

/**
 * The bike stations emulated REST server.
 */
@Path("/stations")
public final class StationsRest {
	/**
	 * the collection of stations.
	 */
	private Stations stations;
	/**
	 * the file name of the data base.
	 */
	// private String fileName = "vsrc/main/resources/paris.json";
	private String fileName = "vlibtour-bikestation/src/main/resources/paris.json";

	/**
	 * reads the collection of stations from a file.
	 * 
	 * @param fileName
	 *            the name of the file.
	 * @throws JAXBException
	 *             the problem when un-marshaling.
	 * @throws IOException
	 *             the problem when reading to the file.
	 */
	private void getStationsFromFile(final String fileName) throws JAXBException, IOException {
		try {

			ObjectMapper mapper = new ObjectMapper(); //jackson class for converting from json to java 
			List stationList; //JSON from file to Object
			stationList = Arrays.asList(mapper.readValue(new File(fileName), Station[].class)); // get the array of stations and convert to a list
			this.stations=new Stations(stationList);// create a Stations instance

		} catch (IOException e) {
			System.err.println(e);
		}
	}

	/**
	 * gets all the station as JSON text.
	 * 
	 * @return the collection of stations
	 * @throws JAXBException
	 *             the problem when un-marshaling.
	 * @throws IOException
	 *             the problem when reading to the file.
	 */
	@GET
	@Path("/")
	@Produces(MediaType.APPLICATION_JSON)
	public Station[] allStations() throws JAXBException, IOException {
		getStationsFromFile(fileName);
		List<Station> s = stations.getStations();
		return s.toArray(new Station[s.size()]);
	}

	/**
	 * get the station of a given number as a JSON text.
	 * 
	 * @param number
	 *            the criterion for the selection.
	 * @return the station
	 * @throws JAXBException
	 *             the problem when un-marshaling.
	 * @throws IOException
	 *             the problem when reading to the file.
	 */
	@GET
	@Path("/{number}")
	@Produces({ MediaType.APPLICATION_JSON })
	public Station stationsByNumber(@PathParam("number") final long number) throws JAXBException, IOException {
		getStationsFromFile(fileName);
		return stations.lookupNumber(number);
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
	@GET
	@Path("/findBikestationsNearGPS")
	@Produces({ MediaType.APPLICATION_JSON })
	public Stations findBikestationsNear(@QueryParam("lat") final double latitude, @QueryParam("long") final double longitude, @DefaultValue("1000") @QueryParam("distance") final double distance) throws JAXBException, IOException{
		getStationsFromFile(fileName);
		return stations.nearGPSPosition(latitude, longitude, distance);
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
	@GET
	@Path("/getAvailabilityNearPOI")
	@Produces("text/plain")
	public String getAvailabilityNearPOI(@QueryParam("lat") final double latitude, @QueryParam("long") final double longitude, @DefaultValue("1000") @QueryParam("distance") final double distance) throws JAXBException, IOException{
		getStationsFromFile(fileName);
		String message = "";
		Stations stationsNear = stations.nearGPSPosition(latitude, longitude, distance);
		for (Station station : stationsNear.getStations()) {
			message += "Name: "+station.name+", availability: "+station.availableBikes+"\n";
		}
		return message;
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

	@GET
	@Path("/findStationToGetBikes")
	@Produces({ MediaType.APPLICATION_JSON })
	public Station findStationToGetBikes(@QueryParam("lat") final double latitude, @QueryParam("long") final double longitude,  @DefaultValue("1000") @QueryParam("distance") final double distance, @QueryParam("groupSize") final int groupSize) throws JAXBException, IOException{
		getStationsFromFile(fileName);
		List<Station> stationsNear = stations.nearGPSPosition(latitude, longitude, distance).getStations();
		Comparator<Station> compareByDistance = (Station o1, Station o2) ->
		                                    	(int) Math.round(o1.distanceFrom(latitude, longitude)*1000 - o2.distanceFrom(latitude, longitude)*1000);
		Collections.sort(stationsNear, compareByDistance);
		for (Station station : stationsNear) {
			if (station.availableBikes >= groupSize) return station;
		}
		return new Station();
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

	@GET
	@Path("/findStationToReturn")
	@Produces({ MediaType.APPLICATION_JSON })
	public Station findStationToReturn(@QueryParam("lat") final double latitude, @QueryParam("long") final double longitude,  @DefaultValue("1000") @QueryParam("distance") final double distance, @QueryParam("groupSize") final int groupSize) throws JAXBException, IOException{
		getStationsFromFile(fileName);
		List<Station> stationsNear = stations.nearGPSPosition(latitude, longitude, distance).getStations();
		Comparator<Station> compareByDistance = (Station o1, Station o2) ->
		                                    	(int) Math.round(o1.distanceFrom(latitude, longitude)*1000 - o2.distanceFrom(latitude, longitude)*1000);
		Collections.sort(stationsNear, compareByDistance);
		for (Station station : stationsNear) {
			if (station.availableBikeStands >= groupSize) return station;
		}
		return new Station();
	}

}

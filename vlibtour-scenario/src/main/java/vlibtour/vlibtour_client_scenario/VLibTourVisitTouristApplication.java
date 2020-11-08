/**
This file is part of the course CSC5002.

Copyright (C) 2017-2019 Télécom SudParis

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

Initial developer(s): Denis Conan
Contributor(s): Rafael BLANCO
 */
package vlibtour.vlibtour_client_scenario;

import static vlibtour.vlibtour_visit_emulation.Log.EMULATION;
import static vlibtour.vlibtour_visit_emulation.Log.LOG_ON;

import java.awt.Color;
import java.awt.Font;
import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.TimeoutException;

import javax.naming.NamingException;

import org.openstreetmap.gui.jmapviewer.MapMarkerDot;

import com.rabbitmq.tools.jsonrpc.JsonRpcException;

import vlibtour.vlibtour_client_emulation_visit.VLibTourVisitEmulationClient;
import vlibtour.vlibtour_client_group_communication_system.VLibTourGroupCommunicationSystemClient;
import vlibtour.vlibtour_client_lobby_room.VLibTourLobbyRoomClient;
import vlibtour.vlibtour_client_scenario.map_viewer.BasicMap;
import vlibtour.vlibtour_client_scenario.map_viewer.MapHelper;
import vlibtour.vlibtour_lobby_room_api.InAMQPPartException;
import vlibtour.vlibtour_tour_management.entity.VlibTourTourManagementException;
import vlibtour.vlibtour_visit_emulation.ExampleOfAVisitWithTwoTourists;
import vlibtour.vlibtour_visit_emulation.Position;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import vlibtour.vlibtour_visit_emulation.GPSPosition;
import java.util.Random;
import vlibtour.vlibtour_bikestation.client.BikeStationClient;
import vlibtour.vlibtour_bikestation.emulatedserver.Stations;
import vlibtour.vlibtour_bikestation.emulatedserver.generated_from_json.Station;
// import java.net.URISyntaxException;
// import java.security.NoSuchAlgorithmException;
// import java.security.KeyManagementException;


/**
 * This class is the client application of the tourists. The access to the lobby
 * room server, to the group communication system, and to the visit emulation
 * server visit should be implemented using the design pattern Delegation (see
 * https://en.wikipedia.org/wiki/Delegation_pattern).
 * 
 * A client creates two queues to receive messages from the broker; the binding
 * keys are of the form "{@code *.*.identity}" and "{@code *.*.location}" while
 * the routing keys are of the form "{@code sender.receiver.identity|location}".
 * 
 * This class uses the classes {@code MapHelper} and {@code BasicMap} for
 * displaying the tourists on the map of Paris. Use the attributes for the
 * color, the map, the map marker dot, etc.
 * 
 * @author Denis Conan
 */
public class VLibTourVisitTouristApplication {
	/**
	 * the colour onto the map of the user identifier of the first tourist.
	 */
	private static final Color colorJoe = Color.RED;
	/**
	 * the colour onto the map of the user identifier of the second tourist.
	 */
	private static final Color colorAvrel = Color.GREEN;
	/**
	 * the colour onto the map of the bike stations.
	 */
	private static final Color colorBikeStations = Color.YELLOW;
	/**
	 * the map to manipulate. Not all the clients need to have a map; therefore we
	 * use an optional.
	 */
	@SuppressWarnings("unused")
	private Optional<BasicMap> map = Optional.empty();
	/**
	 * the dot on the map for the first tourist.
	 */
	@SuppressWarnings("unused")
	private static MapMarkerDot mapDotJoe;
	/**
	 * the dot on the map for the second tourist.
	 */
	@SuppressWarnings("unused")
	private static MapMarkerDot mapDotAvrel;
	/**
	 * delegation to the client of type
	 * {@link vlibtour.vlibtour_client_emulation_visit.VLibTourVisitEmulationClient}.
	 */
	@SuppressWarnings("unused")
	private VLibTourVisitEmulationClient emulationVisitClient;
	/**
	 * delegation to the client of type
	 * {@link vlibtour.vlibtour_client_group_communication_system.VLibTourGroupCommunicationSystemClient}.
	 */
	@SuppressWarnings("unused")
	private VLibTourLobbyRoomClient lobbyRoomClient;
	/**
	 * delegation to the client of type
	 * {@link vlibtour.vlibtour_client_group_communication_system.VLibTourGroupCommunicationSystemClient}.
	 * The identifier of the user is obtained from this reference.
	 */
	@SuppressWarnings("unused")
	private VLibTourGroupCommunicationSystemClient groupCommClient;

	private BikeStationClient bikeStationsClient;

	/**
	 * creates a client application, which will join a group that must already
	 * exist. The group identifier is optional whether this is the first user of the
	 * group ---i.e. the group identifier is built upon the user identifier.
	 * Concerning the tour identifier, it must be provided by the calling method.
	 * 
	 * @param tourId  the tour identifier of this application.
	 * @param groupId the group identifier of this client application.
	 * @param userId  the user identifier of this client application.
	 * @throws InAMQPPartException             the exception thrown in case of a
	 *                                         problem in the AMQP part.
	 * @throws VlibTourTourManagementException problem in the name or description of
	 *                                         POIs.
	 * @throws IOException                     problem when setting the
	 *                                         communication configuration with the
	 *                                         broker.
	 * @throws TimeoutException                problem in creation of connection,
	 *                                         channel, client before the RPC to the
	 *                                         lobby room.
	 * @throws JsonRpcException                problem in creation of connection,
	 *                                         channel, client before the RPC to the
	 *                                         lobby room.
	 * @throws InterruptedException            thread interrupted in call sleep.
	 * @throws NamingException                 the EJB server has not been found
	 *                                         when getting the tour identifier.
	 */
	// public VLibTourVisitTouristApplication(final String tourId, final Optional<String> groupId, final String userId, final Boolean group_creator)
	public VLibTourVisitTouristApplication(final String tourId, final String groupId, final String userId, final Boolean group_creator)
			throws InAMQPPartException, VlibTourTourManagementException, IOException, JsonRpcException,
			TimeoutException, InterruptedException, NamingException, Exception {
			emulationVisitClient = new VLibTourVisitEmulationClient();
			System.out.println("Emulation visit client started by " + userId);
			// System.out.println("Creating BikeStation proxy");
			bikeStationsClient = new BikeStationClient("http://localhost:8890/MyServer/");
			System.out.println("Bike Stations client started by " + userId);
			//System.out.println("Creating communication system proxy and joinnig communication system");
			groupCommClient = new VLibTourGroupCommunicationSystemClient(groupId, tourId, ExampleOfAVisitWithTwoTourists.USER_ID_JOE, userId, group_creator);
			System.out.println("Group communication system started by " + userId);

	}

	public static Boolean allTouristInPOI(final String userId, final HashMap<String, String> allTouristPosition){
		String poiAddress = allTouristPosition.get(userId);

		Iterator hmIterator = allTouristPosition.entrySet().iterator();
		while (hmIterator.hasNext()) { 
			Map.Entry mapElement = (Map.Entry)hmIterator.next(); 
			if (!poiAddress.equals((String) mapElement.getValue())){
				return false;
			}
		} 
		return true;
	}

	/**
	 * executes the tourist application. <br>
	 * We prefer insert comments in the method instead of detailing the method here.
	 * 
	 * @param args the command line arguments.
	 * @throws Exception all the potential problems (since this is a demonstrator,
	 *                   apply the strategy "fail fast").
	 */
	public static void main(final String[] args) throws Exception {
		System.out.println("STARTING VLibTourVisitTouristApplication: " + args[0]);
		String usage = "USAGE: " + VLibTourVisitTouristApplication.class.getCanonicalName()
				+ " userId (either Joe or Avrel)";

		String userId;
		if (args.length != 1) {
	
			throw new IllegalArgumentException(usage);
	
		}
		userId = args[0];
		@SuppressWarnings("unused")
		// final VLibTourVisitTouristApplication client = null;
		final VLibTourVisitTouristApplication client = new VLibTourVisitTouristApplication("disney","perros",userId, (userId.equals(ExampleOfAVisitWithTwoTourists.USER_ID_JOE)) ? true : false);
		if (LOG_ON && EMULATION.isInfoEnabled()) {
			EMULATION.info(userId + "'s application is starting");
		}
		// the tour is empty, this client gets it from the data base (first found)
		// TODO
		// start the VLibTourVisitTouristApplication applications
		// TODO
		// set the map viewer of the scenario (if this is this client application that
		// has created the group [see #VLibTourVisitTouristApplication(...)])
		// the following code should be completed
		// FIXME
		// if (LOG_ON && EMULATION.isDebugEnabled()) {
		// 	EMULATION.debug("Current directory = " + System.getProperty("user.dir") + ".\n" + "We assume that class "
		// 			+ client.getClass().getCanonicalName() + " is launched from directory "
		// 			+ "./vlibtour-scenario/src/main/resources/osm-mapnik/");
		// }
		if (client == null) {
			throw new UnsupportedOperationException("No implemented, yet");
		}
		EMULATION.info("Map initialized");
		client.map = Optional.of(MapHelper.createMapWithCenterAndZoomLevel(48.851412, 2.343166, 14));
		//MY SOLUTION: 
		// client.map = Optional.of(MapHelper.createMapWithCenterAndZoomLevel("src/main/resources/osm-mapnik/", 48.851412, 2.343166,
		// 		14));
		//CREATING INITIAL MAP WITH 2 TOURIST AND 3 POIS
		Font font = new Font("name", Font.BOLD, 20);
		client.map.ifPresent(m -> {
			MapHelper.addMarkerDotOnMap(m, 48.871799, 2.342355, Color.BLACK, font, "Musée Grevin");
			MapHelper.addMarkerDotOnMap(m, 48.860959, 2.335757, Color.BLACK, font, "Pyramide du Louvres");
			MapHelper.addMarkerDotOnMap(m, 48.833566, 2.332416, Color.BLACK, font, "Les catacombes");
			Position positionOfJoe = client.emulationVisitClient.getCurrentPosition(ExampleOfAVisitWithTwoTourists.USER_ID_JOE);
			mapDotJoe = MapHelper.addTouristOnMap(m, colorJoe, font, ExampleOfAVisitWithTwoTourists.USER_ID_JOE,
					positionOfJoe);
			Position positionOfAvrel = client.emulationVisitClient.getCurrentPosition(ExampleOfAVisitWithTwoTourists.USER_ID_AVREL);
			mapDotAvrel = MapHelper.addTouristOnMap(m, colorAvrel, font, ExampleOfAVisitWithTwoTourists.USER_ID_AVREL,
					positionOfAvrel);
			client.map.get().repaint();
			// wait for painting the map
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		});

		//Additional time to create the communication vhost
		if (!userId.equals(ExampleOfAVisitWithTwoTourists.USER_ID_JOE)){
			Thread.sleep(5000);
		}
		// start the consumption of messages (e.g. positions of group members) from the group communication system 
		// TODO
		// repainting # approximately 3s => delay only non-leader clients => not all
		// the clients at the same speed
		// TODO
		// loop until the end of the visit is detected
		// FIXME
		
		// BikeStationClient bikeStationsClient = new BikeStationClient("http://localhost:8890/MyServer/");


		// client.groupCommClient = new VLibTourGroupCommunicationSystemClient("perros", "disney", ExampleOfAVisitWithTwoTourists.USER_ID_JOE, userId, (userId.equals(ExampleOfAVisitWithTwoTourists.USER_ID_JOE)) ? true : false);
		// Thread.sleep(10000);

		//Define some variables to the emulation
		Position userPosition, userPoi, oldPoi;
		String message; 
		int counter = 0;
		Iterator hmIterator;
		Boolean allPOIsReached = false;
		Random randomGenerator = new Random();
		int randomExecTime = randomGenerator.nextInt(10) + 1;
		int stepToDisconnect, stepToReconnect, currentStep;
		stepToDisconnect = 50;
		stepToReconnect = 50;
		currentStep = 0;
		Boolean communication_system_available = true;

		System.out.println("Starting to publish and waiting for having the two users connected and receiving messages");
		while (true){
			userPosition = client.emulationVisitClient.getCurrentPosition(userId);
			message = userPosition.getGpsPosition().getLatitude()+" , "+ userPosition.getGpsPosition().getLongitude();
			client.groupCommClient.publish(userId, "all", "position", message);
			if (client.groupCommClient.getPatnersPosition().size() < 2){
				Thread.sleep(100);
			} else{
				break;
			}
		}

		System.out.println("Starting Tour");
		while (true){
			userPosition = client.emulationVisitClient.getCurrentPosition(userId);
			userPoi = client.emulationVisitClient.getNextPOIPosition(userId);

			//Disconnect from cs
			if (userId.equals(ExampleOfAVisitWithTwoTourists.USER_ID_AVREL) && currentStep == stepToDisconnect){
				client.groupCommClient.leaveAGroup();
				communication_system_available = false;
			}
			//Reconnect to cs
			if (userId.equals(ExampleOfAVisitWithTwoTourists.USER_ID_AVREL) && currentStep == stepToReconnect){
				client.groupCommClient = new VLibTourGroupCommunicationSystemClient("perros", "disney", ExampleOfAVisitWithTwoTourists.USER_ID_JOE, userId, false);
				communication_system_available = true;
			} 

			//Update map with communication systems
			MapHelper.moveTouristOnMap(userId.equals(ExampleOfAVisitWithTwoTourists.USER_ID_JOE)? mapDotJoe : mapDotAvrel, userPosition);
			if (communication_system_available){
			hmIterator = client.groupCommClient.getPatnersPosition().entrySet().iterator();
				while (hmIterator.hasNext()) { 
					Map.Entry mapElement = (Map.Entry)hmIterator.next(); 
					if (mapElement.getKey().equals(userId)){
						continue;
					}
					MapHelper.moveTouristOnMap(mapElement.getKey().equals(ExampleOfAVisitWithTwoTourists.USER_ID_JOE)? mapDotJoe : mapDotAvrel, new Position("Test_name", new GPSPosition((Double.parseDouble(((String) mapElement.getValue()).split("\\,",2)[0])), (Double.parseDouble(((String) mapElement.getValue()).split("\\,",2)[1])))));
				} 
			}
			client.map.get().repaint();

			//Finish the loop
			if (allPOIsReached) break;
			
			//Publish position
			if (communication_system_available){
				message = userPosition.getGpsPosition().getLatitude()+" , "+ userPosition.getGpsPosition().getLongitude();
				client.groupCommClient.publish(userId, "all", "position", message);
			}

			counter++;
			//Decide if make a step in visit, step in path or finish the tour
			if (counter>randomExecTime){
				//Define new random time
				randomExecTime = randomGenerator.nextInt(10) + 1;
				counter = 0;
				if (!userPosition.equals(userPoi)){
					client.emulationVisitClient.stepInCurrentPath(userId);
					currentStep++;
				} else {
					if (communication_system_available){
						HashMap<String,String> allTouristPosition = client.groupCommClient.getPatnersPosition();
						if (allTouristInPOI(userId, allTouristPosition)){
							oldPoi = userPoi;
							// client.emulationVisitClient.stepsInVisit(userId);
							//All tourist make step in visit
							hmIterator = client.groupCommClient.getPatnersPosition().entrySet().iterator();
							while (hmIterator.hasNext()) { 
								Map.Entry mapElement = (Map.Entry)hmIterator.next(); 
								client.emulationVisitClient.stepsInVisit( (String) mapElement.getKey());
								currentStep++;
							} 
							// When the user arrives to the final POI
							if (oldPoi.equals(client.emulationVisitClient.getNextPOIPosition(userId))) allPOIsReached = true;
						}
					} else{
						oldPoi = userPoi;
						client.emulationVisitClient.stepsInVisit(userId);
						if (oldPoi.equals(client.emulationVisitClient.getNextPOIPosition(userId))) allPOIsReached = true;
					} 
				}
			}
			Thread.sleep(400);
		}

		userPosition = client.emulationVisitClient.getCurrentPosition(userId);
		double userPositionLatitude = userPosition.getGpsPosition().getLatitude();
		double userPositionLongitude = userPosition.getGpsPosition().getLongitude();
		Stations stations = client.bikeStationsClient.findBikestationsNearGPS(userPositionLatitude, userPositionLongitude, 500);

		// Draw in map all the bike station within 500 m radius around last POI
		Font font2 = new Font("name", Font.PLAIN, 10);
		client.map.ifPresent(m -> {
			for (Station station : stations.getStations()) {
				MapHelper.addMarkerDotOnMap(m, station.position.lat, station.position.lng, colorBikeStations, font2, station.name);
			}

			client.map.get().repaint();
			// wait for painting the map
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		});

		// Draw in map the bike station selected taking into consideration the distance and the availables places to leave the bikes
		vlibtour.vlibtour_bikestation.client.generated_from_json.Station station = client.bikeStationsClient.findStationToReturn(userPositionLatitude, userPositionLongitude, 500, 2);
		client.map.ifPresent(m -> {
			MapHelper.addMarkerDotOnMap(m, station.position.lat, station.position.lng, Color.BLUE, font, station.name);
			client.map.get().repaint();
			// wait for painting the map
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		});

		System.out.println("Closing communication system");
		client.groupCommClient.close();

		Thread.sleep(10000);
		System.out.println("\nEnd of simulation!\n");
		System.exit(0);
	}
}

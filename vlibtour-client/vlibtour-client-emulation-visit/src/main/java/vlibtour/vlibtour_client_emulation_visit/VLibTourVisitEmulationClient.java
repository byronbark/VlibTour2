/**
This file is part of the course CSC5002.

Copyright (C) 2019 Télécom SudParis

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

Initial developer(s): Chantal Taconet and Denis Conan
Contributor(s): Rafael Blanco
				Zujany Salazar
 */
package vlibtour.vlibtour_client_emulation_visit;

import java.io.IOException;
import java.net.URI;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import vlibtour.vlibtour_visit_emulation.ExampleOfAVisitWithTwoTourists;
import vlibtour.vlibtour_visit_emulation.Position;
import org.glassfish.jersey.client.ClientConfig;


/**
 * The REST client of the VLibTour Emulation Visit Server.
 */
public final class VLibTourVisitEmulationClient {

	Client client;
	URI uri;
	WebTarget service;
	Position position;

	public VLibTourVisitEmulationClient (){
		// client = ClientBuilder.newClient(new ClientConfig());
		client = ClientBuilder.newClient();
		uri = UriBuilder.fromUri(ExampleOfAVisitWithTwoTourists.BASE_URI_WEB_SERVER).build();
		service = client.target(uri);
		position=service
					.path("visitemulation/getNextPOIPosition/" + ExampleOfAVisitWithTwoTourists.USER_ID_JOE).request()
					.accept(MediaType.APPLICATION_JSON).get().readEntity(Position.class);
	}

    /**
     * Get the position of the current POI.
     * 
     * @param user_id
     *            user.
     * @return Position of the POI of the user.
     */
	public Position getNextPOIPosition(String user_id){
		return service
					.path("visitemulation/getNextPOIPosition/" + user_id).request()
					.accept(MediaType.APPLICATION_JSON).get().readEntity(Position.class);
	}

    /**
     * Get the current position of an user.
     * 
     * @param user_id
     *            user.
     * @return Current position of the user.
     */
	public Position getCurrentPosition(String user_id){
		return service
					.path("visitemulation/getCurrentPosition/" + user_id).request()
					.accept(MediaType.APPLICATION_JSON).get().readEntity(Position.class);
	}

    /**
     * Change the next POI of an user.
     * 
     * @param user_id
     *            user.
     * @return Next POI of the user.
     */
	public Position stepInCurrentPath(String user_id){
		return service
					.path("visitemulation/stepInCurrentPath/" + user_id).request()
					.accept(MediaType.APPLICATION_JSON).get().readEntity(Position.class);
	}

    /**
     * Change the position of an user to the next position in their path.
     * 
     * @param user_id
     *            user.
     * @return New position of the user.
     */
	public Position stepsInVisit(String user_id){
		return service
					.path("visitemulation/stepsInVisit/" + user_id).request()
					.accept(MediaType.APPLICATION_JSON).get().readEntity(Position.class);
	}

}

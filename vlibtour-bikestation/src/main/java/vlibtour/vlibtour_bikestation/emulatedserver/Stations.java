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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import vlibtour.vlibtour_bikestation.emulatedserver.generated_from_json.Position;
import vlibtour.vlibtour_bikestation.emulatedserver.generated_from_json.Station;

/**
 * The stations REST server.
 */
public class Stations {
	/**
	 * the collection of stations.
	 */
	@XmlElementWrapper(name = "stations")
	@XmlElement(name = "station")
	private List<Station> stations = new ArrayList<Station>();

	/**
	 * default constructor.
	 */
	public Stations() {
	}

	/**
	 * constructs the object with the given collection of stations.
	 * 
	 * @param stations
	 *            the collection of stations.
	 */
	public Stations(final List<Station> stations) {
		this.stations = stations;
	}

	@Override
	public String toString() {
		StringBuilder output = new StringBuilder();
		for (Iterator<Station> it = stations.iterator(); it.hasNext();) {
			output = output.append(it.next() + "\n");
		}
		return output.toString();
	}

	public List<Station> getStations(){
		return this.stations;
	}


	/**
	 * searches for the station of a given number.
	 * 
	 * @param number
	 *            the number for the search.
	 * @return the station matching the criteria.
	 */
	public Station lookupNumber(final long number) {
		for (Station station : stations) {
			if (station.sameNumber(number)) {
				return station;
			}
		}
		return new Station();
	}

	/**
	 * add a station to the collection.
	 * 
	 * @param station
	 *            the station to add.
	 */
	public void add(final Station station) {
		stations.add(station);
	}

	/**
	 * get stations within a radius around a GPS position.
	 * 
     * @param latitude
     *            latitude of GPS location.
     * @param longitude
     *            logitude of GPS location.
     * @param distance
     *            maximun radius to search. 
     * @return Stations around the GPS position.
	 */
	public Stations nearGPSPosition(final double latitude, final double longitude, final double distance) {
		Stations foundStations = new Stations();
		for (Station station : stations) {
			if (station.nearGPSPosition(latitude, longitude, distance)) {
				foundStations.add(station);
			}
		}
		return foundStations;
	}
}

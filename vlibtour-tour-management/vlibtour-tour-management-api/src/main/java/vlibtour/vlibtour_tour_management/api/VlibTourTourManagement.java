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
				Zujany Salazar
 */
package vlibtour.vlibtour_tour_management.api;

import javax.ejb.Remote;
import java.util.List;

import vlibtour.vlibtour_tour_management.entity.POI;
import vlibtour.vlibtour_tour_management.entity.Tour;
import vlibtour.vlibtour_visit_emulation.GPSPosition;
import vlibtour.vlibtour_tour_management.entity.VlibTourTourManagementException;


/**
 * This interface defines the operation for managing POIs and Tours.
 * 
 * @author Denis Conan
 */
@Remote
public interface VlibTourTourManagement {
	/**
	 * Create and insert a POI.
	 *
	 * @param name
	 *            POI's name.
	 *
	 * @param description
	 *            POI's description.
	 *
	 * @param gpslocation
	 *            structure to manage POI's gpslocation.
	 */
	public void createPOI(String name, String description, GPSPosition gpslocation);
	/**
	 * Create and insert a Tour.
	 *
	 * @param name
	 *            Tour's name.
	 *
	 * @param description
	 *            Tour's description.
	 *
	 * @param pois
	 *            List of pois to visit in the tour.
	 */
	public void createTour(String name, String description, List<POI> pois) throws VlibTourTourManagementException;
	/**
	 * Insert a POI into a Tour.
	 *
	 * @param poi
	 *            instance of the class POI.
	 *
	 * @param tour
	 *            instance of the class tour.
	 */
	public void addPOItoTour(POI poi, Tour tour) throws VlibTourTourManagementException;
	/**
	 * Get all the tours inserted.
	 *
	 * @return 
	 *            List that contains all tours in the database.
	 */
	public List<Tour> getTours();
	/**
	 * Get all the tours with the same name inserted.
	 *
	 * @param name
	 *            name of tours to find.
	 *
	 * @return 
	 *            List that contains all tours with name 'name' in the database.
	 */
	public List<Tour> getToursByName(String name);
	/**
	 * Get all the POIs with the same name inserted.
	 *
	 * @param name
	 *            name of POIs to find.
	 *
	 * @return 
	 *            List that contains all POIs with name 'name' in the database.
	 */
	public List<POI> getPOIsByName(String name);
	/**
	 * Get all the POIs inserted.
	 *
	 * @return 
	 *            List that contains all POIs in the database.
	 */
	public List<POI> getPOIs();
	/**
	 * Modify description of an existent POI.
	 *
	 * @param poi
	 *            instance of the class POI.
	 *
	 * @param newDescription
	 *            new description to add.
	 */
	public void modifyDescriptionPOI(POI poi, String newDescription);
	/**
	 * Remove a POI from a Tour.
	 *
	 * @param poi
	 *            instance of the class POI.
	 *
	 * @param tour
	 *            instance of the class tour.
	 */
	public void removePOIFromTour(POI poi, Tour tour) throws VlibTourTourManagementException;
	/**
	 * Remove a POI from database.
	 *
	 * @param poi
	 *            instance of the class POI.
	 *
	 */
	public void removePOI(POI poi) throws VlibTourTourManagementException;
	/**
	 * Move a POI from the sequence of a Tour.
	 *
	 * @param poi
	 *            instance of the class POI.
	 *
	 * @param tour
	 *            instance of the class tour.
	 *
	 * @param index
	 *            new position of POI in the sequence.
	 */
	public void movePOIInSequence(Tour tour, POI poi, int index) throws VlibTourTourManagementException;

}

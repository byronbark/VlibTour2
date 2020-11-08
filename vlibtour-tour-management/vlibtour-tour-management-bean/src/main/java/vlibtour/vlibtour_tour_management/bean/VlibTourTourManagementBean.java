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
Contributor(s): Rafael Blanco
				Zujany Salazar
 */
package vlibtour.vlibtour_tour_management.bean;


// import java.util.List;
// import java.util.Lists;
import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import vlibtour.vlibtour_tour_management.entity.POI;
import vlibtour.vlibtour_tour_management.entity.Tour;
import vlibtour.vlibtour_visit_emulation.GPSPosition;
import vlibtour.vlibtour_tour_management.api.VlibTourTourManagement;
import vlibtour.vlibtour_tour_management.entity.VlibTourTourManagementException;


/**
 * This class defines the EJB Bean of the VLibTour tour management.
 * 
 * @author Denis Conan
 */
@Stateless
public class VlibTourTourManagementBean implements VlibTourTourManagement {

	/**
	 * the reference to the entity manager, which persistence context is "pu1".
	 */
	@PersistenceContext(unitName = "pu2")
	private EntityManager em;


	@Override
	public void createPOI(String name, String description, GPSPosition gpslocation){
		// Create a new poi
		POI poi = new POI();
		poi.setName(name);
		poi.setDescription(description);
		poi.setGPSlocation(gpslocation);

		// Persist the poi
		em.persist(poi);
		return;
	}
	@Override
	public void createTour(String name, String description, List<POI> pois) throws VlibTourTourManagementException{

		if (pois == null || pois.size() == 0) {
			throw new VlibTourTourManagementException(
					"Unexpected number of pois at function createTour, number of pois: " + ((pois == null) ? "null" : "" + pois.size()));
		}

		Tour tour = new Tour();
		tour.setName(name);
		tour.setDescription(description);
		tour.setPOIs(new ArrayList<POI>());
		em.persist(tour);
		// tour.setPOIs(pois);
		// em.persist(tour);

		addTourToPois(tour, pois);
		return;
	}
	@Override
	public void addPOItoTour(POI poi, Tour tour) throws VlibTourTourManagementException{
		if (tour.poiAlreadyAdded(poi)){
			throw new VlibTourTourManagementException(
				"Error: POI already inside Tour");
		}
		poi.getTours().add(tour);
		tour.getPOIs().add(poi);
		em.merge(poi);
		em.merge(tour);
		return;
	}
	@Override
	public List<Tour> getTours(){
		Query q = em.createQuery("select t from Tour t");
		return (List<Tour>) q.getResultList();
	}
	@Override
	public List<Tour> getToursByName(String name){
		Query q = em.createQuery("select t from Tour t where t.name = :name");
		q.setParameter("name", name);
		return (List<Tour>) q.getResultList();
	}
	@Override
	public List<POI> getPOIsByName(String name){
		Query q = em.createQuery("select c from POI c where c.name = :name");
		q.setParameter("name", name);
		return (List<POI>) q.getResultList();
	}

	@Override
	public List<POI> getPOIs(){
		Query q = em.createQuery("select p from POI p");
		return (List<POI>) q.getResultList();
	}

	/**
	 * Add reference of a tour to the database table of a set of POIs.
	 *
	 * @param tour
	 *			tour composed by pois
	 *
	 * @param pois
	 *			pois to add reference 
	 */
	public void addTourToPois(Tour tour, List<POI> pois) throws VlibTourTourManagementException{
		for (Iterator<POI> it = pois.iterator(); it.hasNext();) {
			addPOItoTour(it.next(), tour);
		}
	}

	@Override
	public void modifyDescriptionPOI(POI poi, String newDescription){
		poi.setDescription(newDescription);
		em.merge(poi);
		return;
	}

	@Override
	public void removePOIFromTour(POI poi, Tour tour) throws VlibTourTourManagementException{
		if (!tour.poiAlreadyAdded(poi) || !poi.tourAlreadyAdded(tour)){
			throw new VlibTourTourManagementException(
				"Error: Tour not added to POI or POI not added to Tour");
		}
		poi.removeTour(tour);
		tour.removePOI(poi);
		em.merge(poi);
		em.merge(tour);
		return;
	}

	@Override
	public void removePOI(POI poi) throws VlibTourTourManagementException{
		List<Tour> tours = new ArrayList<Tour>(poi.getTours());
		for (Iterator<Tour> it = tours.iterator(); it.hasNext();) {
			Tour t = it.next();
			removePOIFromTour(poi, t);
		}
		if (!em.contains(poi)) {
		    poi = em.merge(poi);
		}
		em.remove(poi);
		return;
	}

	@Override
	public void movePOIInSequence(Tour tour, POI poi, int index)throws VlibTourTourManagementException{
		if (tour.getPOIs().size() < index || index < 1){
			throw new VlibTourTourManagementException(
				"Error: Can not change order in sequence because the new position is not valid. Size of sequence: "+tour.getPOIs().size()+", value provided: "+index);
		}
		if (!tour.poiAlreadyAdded(poi) || !poi.tourAlreadyAdded(tour)){
			throw new VlibTourTourManagementException(
				"Error: Tour not added to POI or POI not added to Tour");
		}
		int position = 1;
		int size = tour.getPOIs().size();
		List<POI> newPOIs = new ArrayList<POI>();
		Iterator<POI> it = tour.getPOIs().iterator();
		while (position <= size){
			if (position == index){
				newPOIs.add(poi);
				position = position + 1;
				continue;
			}
			POI p = it.next();
			if (p.getId().equals(poi.getId())) continue;
			newPOIs.add(p);
			position = position + 1;
		}
        tour.setPOIs(newPOIs);
        em.merge(tour);
		return;
	}


}

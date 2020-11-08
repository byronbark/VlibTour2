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
Contributor(s):	Rafael Blanco
				Zujany Salazar
 */
package vlibtour.vlibtour_tour_management.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.ManyToMany;
import javax.persistence.OrderColumn;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Table;
import javax.persistence.JoinTable;
import javax.persistence.JoinColumn;
import javax.persistence.FetchType;
/**
 * The entity bean defining a tour in the VLibTour case study. A tour is a
 * sequence of points of interest ({@link POI}).
 * 
 * For the sake of simplicity, we suggest that you use named queries.
 * 
 * @author Denis Conan
 */
@Entity
@Table(name="TOUR_TABLE")
public class Tour implements Serializable {
// public class Tour {
	/**
	 * the serial version UID.
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "TOUR_ID")
	@GeneratedValue(strategy=GenerationType.AUTO)
	private String tid;
	private String name;
	private String description;
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(
	  name = "pois_in_tour", 
	  joinColumns = @JoinColumn(name = "tour_id"), 
	  inverseJoinColumns = @JoinColumn(name = "poi_id"))
	@OrderColumn(name="POIS_ORDER")
	// private List<POI> pois;
	// @ManyToMany
	private List<POI> pois = new ArrayList<POI>();


	// @Id
	// @Column(name = "TOUR_ID")
	// @GeneratedValue(strategy=GenerationType.AUTO)
	public String getId() {
		return tid;
	}

	public void setId(final String id) {
		this.tid = id;
	}
	/**
	 * gets the name of the tour.
	 * 
	 * @return the name of the tour.
	 */
	@Column(name = "TOUR_NAME")
	public String getName(){
		return name;
	}

	public void setName(final String name){
		this.name = name;
	}
	@Column(name = "TOUR_DESCRIPTION")
	public String getDescription(){
		return description;
	}

	public void setDescription(final String description){
		this.description = description;
	}

	/**
	 * gets the List of POI.
	 * 
	 * @return the List.
	 */
	// @ManyToMany(mappedBy = "tour")
	//ALERT
	// @ManyToMany
	// @ManyToMany
	// @JoinTable(
	//   name = "pois_in_tour", 
	//   joinColumns = @JoinColumn(name = "tour_id"), 
	//   inverseJoinColumns = @JoinColumn(name = "poi_id"))
	public List<POI> getPOIs() {
		return pois;
	}

	/**
	 * sets the List of orders.
	 * 
	 * @param newValue
	 *            the new List.
	 */
	public void setPOIs(final List<POI> newValue) {
		this.pois = newValue;
	}

	public void addPOI(final POI poi){
		System.out.println("NEW POI: ");
		System.out.println(poi);
		this.pois.add(poi);
	}

	public Boolean poiAlreadyAdded(final POI poi){
		for (Iterator<POI> it = pois.iterator(); it.hasNext();) {
			if (it.next().getId().equals(poi.getId())){
				return true;
			}
		}
		return false;
	}

	public void removePOI(final POI poi){
		for (Iterator<POI> it = pois.iterator(); it.hasNext();) {
			if (it.next().getId().equals(poi.getId())){
				it.remove();
			}
		}
		return;
	}

	@Override
	public String toString() {
		StringBuilder output = new StringBuilder();
		output.append("Tour:\n    Id: "+this.tid+", Name: "+this.name+", Description: "+this.description);
		if (pois.size()>0){
			output.append("\n    "+pois.size()+" POIs:\n");
			for (Iterator<POI> it = pois.iterator(); it.hasNext();) {
				output = output.append("      "+it.next() + "\n");
			}
		} else{
			output.append("\n    No POIs in tour.\n");
		}
		return output.toString();
	}

}

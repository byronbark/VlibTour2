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
package vlibtour.vlibtour_tour_management.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;
import vlibtour.vlibtour_visit_emulation.GPSPosition;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;

import javax.persistence.CascadeType;
import javax.persistence.Column;
// import javax.persistence.Entity;
// import javax.persistence.Id;
import javax.persistence.Table;
// import javax.persistence.ManyToMany;
import javax.persistence.JoinTable;
import javax.persistence.JoinColumn;
// import javax.persistence.CascadeType;

/**
 * The entity bean defining a point of interest (POI). A {@link Tour} is a
 * sequence of points of interest.
 * 
 * For the sake of simplicity, we suggest that you use named queries.
 * 
 * @author Denis Conan
 */

@Entity
@Table(name="POI_TABLE")
public class POI implements Serializable {
// public class POI{
	/**
	 * the serial version UID.
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "POI_ID")
	@GeneratedValue(strategy=GenerationType.AUTO)
	private String poid;
	private String name;
	private String description;
	private GPSPosition gpslocation;
	@ManyToMany(mappedBy = "pois", fetch = FetchType.EAGER)
	// @JoinTable(
	//   name = "pois_in_tour", 
	//   joinColumns = @JoinColumn(name = "poi_id"), 
	//   inverseJoinColumns = @JoinColumn(name = "tour_id"))
	// private List<Tour> tours;
	// @ManyToMany
	private List<Tour> tours = new ArrayList<Tour>();

	// @Id
	// @Column(name = "POI_ID")
	// @GeneratedValue(strategy=GenerationType.AUTO)
	public String getId() {
		return poid;
	}

	public void setId(final String id) {
		this.poid = id;
	}
	/**
	 * gets the name of the POI.
	 * 
	 * @return the name of the POI.
	 */
	@Column(name = "POI_NAME")
	public String getName(){
		return name;
	}

	public void setName(final String name){
		this.name = name;
	}
	@Column(name = "POI_DESCRIPTION")
	public String getDescription(){
		return description;
	}

	public void setDescription(final String description){
		this.description = description;
	}
	@Column(name = "POI_GPSLOCATION")
	public GPSPosition getGPSlocation(){
		return gpslocation;
	}

	public void setGPSlocation(final GPSPosition gpslocation){
		this.gpslocation = gpslocation;
	}

	// @ManyToMany
	// @ManyToMany(mappedBy = "pois")
	public List<Tour> getTours() {
		return tours;
	}

	public void setTours(final List<Tour> newValue) {
		this.tours = newValue;
	}

	public Boolean tourAlreadyAdded(final Tour tour){
		for (Iterator<Tour> it = tours.iterator(); it.hasNext();) {
			if (it.next().getId().equals(tour.getId())){
				return true;
			}
		}
		return false;
	}

	public void removeTour(final Tour tour){
		for (Iterator<Tour> it = tours.iterator(); it.hasNext();) {
			if (it.next().getId().equals(tour.getId())){
				it.remove();
			}
		}
		return;
	}

	@Override
	public String toString() {
		StringBuilder output = new StringBuilder();
		output.append("POI:\n      Id: "+this.poid+", Name: "+this.name+", Description: "+this.description);
		if (tours.size()>0){
			output.append("\n      "+tours.size()+" Tours:\n");
			for (Iterator<Tour> it = tours.iterator(); it.hasNext();) {
				output = output.append("          "+it.next().getName() + "\n");
			}
		} else{
			output.append("\n      No Tours in POI.\n");
		}
		return output.toString();
	}
}

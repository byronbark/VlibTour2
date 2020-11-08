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
Contributor(s):
 */
package vlibtour.vlibtour_tour_management.bean;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

import javax.ejb.embeddable.EJBContainer;
import javax.naming.Context;
import javax.naming.NamingException;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import vlibtour.vlibtour_tour_management.entity.VlibTourTourManagementException;
import vlibtour.vlibtour_tour_management.api.VlibTourTourManagement;
import vlibtour.vlibtour_tour_management.entity.POI;
import vlibtour.vlibtour_tour_management.entity.Tour;
import vlibtour.vlibtour_visit_emulation.GPSPosition;

public class TestVlibTourTourManagementBean {

	private static EJBContainer ec;
	private static Context ctx;
	// private static VlibTourTourManagement sb;

	@BeforeClass
	public static void setUpClass() throws Exception {
	    Map<String, Object> properties = new HashMap<String, Object>();
	    properties.put(EJBContainer.MODULES, new File("target/classes"));
	    ec = EJBContainer.createEJBContainer(properties);
	    ctx = ec.getContext();
		VlibTourTourManagement sb = (VlibTourTourManagement) ctx.lookup("vlibtour.vlibtour_tour_management.api.VlibTourTourManagement");
		sb.createPOI("DisneyLand Paris", "Where dreams come true", new GPSPosition(48.8672, 2.7838));
		sb.createPOI("Musée D'Orsay", "Famous Musée", new GPSPosition(48.8672, 2.7838));
		sb.createPOI("Musée Grévin", "Important place", new GPSPosition(48.8718, 2.3422));
		sb.createPOI("La Tour Eiffel", "Important place", new GPSPosition(48.858370, 2.294481));
		List<POI> poisMagicalTour = new ArrayList<POI>();
		poisMagicalTour.add(sb.getPOIsByName("DisneyLand Paris").iterator().next());
		poisMagicalTour.add(sb.getPOIsByName("Musée D'Orsay").iterator().next());
		poisMagicalTour.add(sb.getPOIsByName("La Tour Eiffel").iterator().next());
		sb.createTour("Magical Tour", "Disneyland Tour", poisMagicalTour);

		List<POI> poisTourFrancesByRafael = new ArrayList<POI>();
		poisTourFrancesByRafael.add(sb.getPOIsByName("DisneyLand Paris").iterator().next());
		sb.createTour("Tour Frances", "Created by Rafael", poisTourFrancesByRafael);

		List<POI> poisTourFrancesByBlanco = new ArrayList<POI>(); //They have to be different, I don't know why
		poisTourFrancesByBlanco.add(sb.getPOIsByName("DisneyLand Paris").iterator().next());
		sb.createTour("Tour Frances", "Created by Blanco", poisTourFrancesByBlanco);

	}

	// @Ignore
	// @Test(expected = VlibTourTourManagementException.class)
	@Test
	public void createPOITest1() throws Exception {
		VlibTourTourManagement sb = (VlibTourTourManagement) ctx.lookup("vlibtour.vlibtour_tour_management.api.VlibTourTourManagement");
		System.out.println("\n\n\nTEST: create POI Pyramide du Louvre. \nList of POIs before operation:\n"+sb.getPOIs());
		sb.createPOI("Pyramide du Louvre", "Important place", new GPSPosition(48.860854, 2.335812));
		System.out.println("createPOITest1 OK. \nList of POIs after operation:\n"+sb.getPOIs());
		return;
	}

	@Ignore
	@Test(expected = VlibTourTourManagementException.class)
	public void findPOIWithPIDTest1() throws Exception {
	}

	// @Ignore
	@Test
	public void findAllPOIsWithNameTest1() throws Exception {
		System.out.println("\n\n\nTEST: find all POIs (Musée Grévin) using the name");
		VlibTourTourManagement sb = (VlibTourTourManagement) ctx.lookup("vlibtour.vlibtour_tour_management.api.VlibTourTourManagement");
		// sb.createPOI("Musée Grévin", "Important place", new GPSPosition(48.8718, 2.3422));
		System.out.println(sb.getPOIsByName("Musée Grévin"));
		System.out.println("findAllPOIsWithNameTest1 OK\n");
		return;
	}

	// @Ignore
	@Test
	public void findAllPOIsTest1() throws Exception {
		System.out.println("\n\n\nTEST: find All POIs");
		VlibTourTourManagement sb = (VlibTourTourManagement) ctx.lookup("vlibtour.vlibtour_tour_management.api.VlibTourTourManagement");
		System.out.println(sb.getPOIs());
		System.out.println("findAllPOIsTest1 OK\n");
	}

	// @Ignore
	@Test(expected = VlibTourTourManagementException.class)
	public void createTourTest1() throws Exception {
		VlibTourTourManagement sb = (VlibTourTourManagement) ctx.lookup("vlibtour.vlibtour_tour_management.api.VlibTourTourManagement");
		System.out.println("\n\n\nTEST: create Tour without POI.");
		List<POI> pois = new ArrayList<POI>();
		// pois.add(sb.getPOIsByName("DisneyLand Paris").iterator().next());
		sb.createTour("Tour Venezolano", "Important Tour", pois);
		// System.out.println("createTourTest1 OK. \nList of Tours after operation:\n"+sb.getTours());
	}

	// @Ignore
	@Test
	public void createTourTest2() throws Exception {
		VlibTourTourManagement sb = (VlibTourTourManagement) ctx.lookup("vlibtour.vlibtour_tour_management.api.VlibTourTourManagement");
		System.out.println("\n\n\nTEST: create Tour Venezolano. \nList of Tours before operation:\n"+sb.getTours());
		List<POI> pois = new ArrayList<POI>();
		pois.add(sb.getPOIsByName("DisneyLand Paris").iterator().next());
		sb.createTour("Tour Venezolano", "Important Tour", pois);
		System.out.println("createTourTest2 OK. \nList of Tours after operation:\n"+sb.getTours());
	}

	@Ignore
	@Test(expected = VlibTourTourManagementException.class)
	public void findTourWithTIDTest1() throws Exception {
	}

	// @Ignore
	@Test
	public void findAllToursWithNameTest1() throws Exception {
		System.out.println("\n\n\nTEST: find all Tours (Tour Frances) using the name");
		VlibTourTourManagement sb = (VlibTourTourManagement) ctx.lookup("vlibtour.vlibtour_tour_management.api.VlibTourTourManagement");
		// List<POI> pois = new ArrayList<POI>();
		// pois.add(sb.getPOIsByName("DisneyLand Paris").iterator().next());
		// sb.createTour("Tour Frances", "Important Tour", pois);
		// List<POI> pois2 = new ArrayList<POI>(); //They have to be different, I don't know why
		// pois2.add(sb.getPOIsByName("DisneyLand Paris").iterator().next());
		// sb.createTour("Tour Frances", "More Important Tour", pois2);
		System.out.println(sb.getToursByName("Tour Frances"));
		System.out.println("findAllToursWithNameTest1 OK\n");
	}

	// @Ignore
	@Test
	public void findAllToursTest1() throws Exception {
		System.out.println("\n\n\nTEST: find All Tours");
		VlibTourTourManagement sb = (VlibTourTourManagement) ctx.lookup("vlibtour.vlibtour_tour_management.api.VlibTourTourManagement");
		// List<POI> pois = new ArrayList<POI>();
		// pois.add(sb.getPOIsByName("DisneyLand Paris").iterator().next());
		// sb.createTour("Tour Europa", "Important Tour", pois);
		// sb.createTour("Tour Latinoamerica", "Important Tour", pois);
		System.out.println(sb.getTours());
		System.out.println("findAllToursTest1 OK\n");
	}

	// By RB
	// @Ignore
	@Test
	public void addPOIToTour() throws Exception {
		VlibTourTourManagement sb = (VlibTourTourManagement) ctx.lookup("vlibtour.vlibtour_tour_management.api.VlibTourTourManagement");
		System.out.println("\n\n\nTEST: Add POI (Musée Grévin) to TOUR (Magical Tour).\nList of Tours before operation:\n"+sb.getTours());
		// sb.createPOI("La Tour Eiffel", "Important place", new GPSPosition(48.858370, 2.294481));
		// List<POI> pois = new ArrayList<POI>();
		// pois.add(sb.getPOIsByName("DisneyLand Paris").iterator().next());
		// sb.createTour("Tour Americano", "Important Tour", pois);
		// sb.createTour("Tour Europa", "Important Tour", new List<POI>());
		// sb.createTour("Tour Latinoamerica", "Important Tour", new ArrayList<POI>());
		// System.out.println(sb.getTours());
		sb.addPOItoTour(sb.getPOIsByName("Musée Grévin").iterator().next(), sb.getToursByName("Magical Tour").iterator().next());
		// sb.addPOItoTour(sb.getPOIsByName("La Tour Eiffel").iterator().next(), sb.getToursByName("Tour Americano").iterator().next());
		// sb.getTour("Tour Americano").getPOIs().add(sb.getPOI("La Tour Eiffel"));
		// System.out.println(sb.getTour("Tour Frances"));
		// System.out.println(sb.getTours());
		System.out.println("addPOIToTour OK. \nList of Tours after operation:\n"+sb.getTours());
	}

	@Test
	public void modifyDescriptionPOITest() throws Exception {
		VlibTourTourManagement sb = (VlibTourTourManagement) ctx.lookup("vlibtour.vlibtour_tour_management.api.VlibTourTourManagement");
		System.out.println("\n\n\nTEST: Modify the description of a POI (Musée Grévin). \nList of POIs before operation:\n"+sb.getPOIs());
		sb.modifyDescriptionPOI(sb.getPOIsByName("Musée Grévin").iterator().next(), "Musée à Paris.");
		// sb.getPOIsByName("DisneyLand Paris").iterator().next().setDescription("Paris");
		// System.out.println(sb.getTours());
		System.out.println("modifyDescriptionPOITest OK. \nList of POIs after operation:\n"+sb.getPOIs());
	}

	@Test
	public void removePOIFromTourTest() throws Exception {
		VlibTourTourManagement sb = (VlibTourTourManagement) ctx.lookup("vlibtour.vlibtour_tour_management.api.VlibTourTourManagement");
		System.out.println("\n\n\nTEST: remove POI (Musée D'Orsay) from Tour (Magical Tour).\nList of Tours before operation:\n"+sb.getTours());
		sb.removePOIFromTour(sb.getPOIsByName("Musée D'Orsay").iterator().next(), sb.getToursByName("Magical Tour").iterator().next());
		// System.out.println(sb.getTours());
		System.out.println("removePOIFromTourTest OK. \nList of Tours after operation:\n"+sb.getTours());
	}

	@Test
	public void removePOITest() throws Exception {
		VlibTourTourManagement sb = (VlibTourTourManagement) ctx.lookup("vlibtour.vlibtour_tour_management.api.VlibTourTourManagement");
		System.out.println("\n\n\nTEST: remove POI (DisneyLand Paris). \nList of Tours before operation:\n"+sb.getTours()+"\nList of POIs before operation:\n"+sb.getPOIs());
		// System.out.println(sb.getTours());
		// System.out.println("comienza la eliminacion");
		sb.removePOI(sb.getPOIsByName("DisneyLand Paris").iterator().next());
		// System.out.println(sb.getTours());
		System.out.println("removePOITest OK.  \nList of Tours after operation:\n"+sb.getTours()+"\nList of POIs after operation:\n"+sb.getPOIs());
	}

	@Test
	public void movePOIToTest() throws Exception {
		VlibTourTourManagement sb = (VlibTourTourManagement) ctx.lookup("vlibtour.vlibtour_tour_management.api.VlibTourTourManagement");
		System.out.println("\n\n\nTEST: move POI (La Tour Eiffel) in sequence of Tour (Magical Tour).\nList of Tours before operation:\n"+sb.getTours());
		// System.out.println(sb.getTours());
		sb.movePOIInSequence(sb.getToursByName("Magical Tour").iterator().next(), sb.getPOIsByName("La Tour Eiffel").iterator().next(), 2);
		// System.out.println(sb.getTours());
		// System.out.println("comienza la eliminacion");
		// sb.removePOI(sb.getPOIsByName("DisneyLand Paris").iterator().next());
		// System.out.println(sb.getTours());
		System.out.println("movePOIToTest OK. \nList of Tours after operation:\n"+sb.getTours());
	}

	@After
	public void tearDown() throws Exception {
	}

	@AfterClass
	public static void tearDownClass() throws Exception {
		if (ec != null) {
			ec.close();
		}
	}

}

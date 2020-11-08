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
package vlibtour.vlibtour_admin_client;

import javax.naming.InitialContext;

import vlibtour.vlibtour_tour_management.api.VlibTourTourManagement;
import vlibtour.vlibtour_tour_management.entity.POI;
import vlibtour.vlibtour_tour_management.entity.Tour;
import vlibtour.vlibtour_visit_emulation.GPSPosition;

import java.util.ArrayList;
import java.util.List;
/**
 * This class defines the administration client of the case study vlibtour.
 * <ul>
 * <li>USAGE:
 * <ul>
 * <li>vlibtour.vlibtour_admin_client.VlibTourAdminClient populate toursAndPOIs
 * </li>
 * <li>vlibtour.vlibtour_admin_client.VlibTourAdminClient empty toursAndPOIs
 * </li>
 * </ul>
 * </li>
 * </ul>
 * 
 * @author Denis Conan
 */
public class VlibTourTourManagementAdminClient {
	/**
	 * constructs an instance of the administration client.
	 * 
	 * @throws Exception
	 *             the exception thrown by the lookup.
	 */
	public VlibTourTourManagementAdminClient() throws Exception {
		throw new UnsupportedOperationException("Not implemented, yet.");
	}

	/**
	 * the main of the administration client.
	 * 
	 * @param args
	 *            the command line arguments
	 * @throws Exception
	 *             the exception that can be thrown (none is treated).
	 */
	public static void main(final String[] args) throws Exception {
		VlibTourTourManagement sb;
		try {
			InitialContext ic = new InitialContext();
			sb = (VlibTourTourManagement) ic.lookup("vlibtour.vlibtour_tour_management.api.VlibTourTourManagement");

			System.out.println("VlibTourTourManagementAdminClient, args: "+args[0]);
			if (args[0].equals("populate")){
				System.out.println("\n\n\nCreating POIs (DisneyLand Paris, Musée D'Orsay, La Tour Eiffel, Musée Grévin) and Tours (Magical Tour).");
				sb.createPOI("DisneyLand Paris", "Where dreams come true", new GPSPosition(48.8672, 2.7838));
				sb.createPOI("Musée D'Orsay", "Famous Musée", new GPSPosition(48.8672, 2.7838));
				sb.createPOI("Musée Grévin", "Important place", new GPSPosition(48.8718, 2.3422));
				sb.createPOI("La Tour Eiffel", "Important place", new GPSPosition(48.858370, 2.294481));
				List<POI> poisMagicalTour = new ArrayList<POI>();
				poisMagicalTour.add(sb.getPOIsByName("DisneyLand Paris").iterator().next());
				poisMagicalTour.add(sb.getPOIsByName("Musée D'Orsay").iterator().next());
				poisMagicalTour.add(sb.getPOIsByName("La Tour Eiffel").iterator().next());
				sb.createTour("Magical Tour", "Disneyland Tour", poisMagicalTour);
			} else{
				if (args[0].equals("empty")){
					System.out.println("\n\n\nRemoving POIs (DisneyLand Paris, Musée D'Orsay, La Tour Eiffel, Musée Grévin).");
					sb.removePOI(sb.getPOIsByName("DisneyLand Paris").iterator().next());
					sb.removePOI(sb.getPOIsByName("Musée D'Orsay").iterator().next());
					sb.removePOI(sb.getPOIsByName("Musée Grévin").iterator().next());
				}
			}


		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

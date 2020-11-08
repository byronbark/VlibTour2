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
// CHECKSTYLE:OFF
package vlibtour.vlibtour_client_group_communication_system;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import com.rabbitmq.http.client.Client;

import vlibtour.vlibtour_lobby_room_api.InAMQPPartException;
import com.rabbitmq.tools.jsonrpc.JsonRpcException;
// import java.net.URISyntaxException;
import java.security.NoSuchAlgorithmException;
import java.security.KeyManagementException;

public class TestScenario {

	private static Client c;

	@BeforeClass
	public static void setUp() throws IOException, InterruptedException, URISyntaxException {
		try {
			new ProcessBuilder("rabbitmqctl", "stop").inheritIO().start().waitFor();
		} catch (IOException | InterruptedException e) {
		}
		Thread.sleep(1000);
		new ProcessBuilder("rabbitmq-server", "-detached").inheritIO().start().waitFor();
		new ProcessBuilder("rabbitmqctl", "stop_app").inheritIO().start().waitFor();
		new ProcessBuilder("rabbitmqctl", "reset").inheritIO().start().waitFor();
		new ProcessBuilder("rabbitmqctl", "start_app").inheritIO().start().waitFor();
		c = new Client("http://127.0.0.1:15672/api/", "guest", "guest");
	}

	@Ignore
	@Test
	public void test()
			throws IOException, TimeoutException, InterruptedException, ExecutionException, InAMQPPartException, JsonRpcException, URISyntaxException, NoSuchAlgorithmException, KeyManagementException {
				VLibTourGroupCommunicationSystemClient cs_zujany = new VLibTourGroupCommunicationSystemClient("grupo_1", "tour_1", "zujany", "zujany", true);
				VLibTourGroupCommunicationSystemClient cs_rafael = new VLibTourGroupCommunicationSystemClient("grupo_1", "tour_1", "zujany", "rafael", false);
				// cs_zujany.publish("zujany", "all", "regano", "Hola mundo de perros");
				// Thread.sleep(10000);
				cs_zujany.close();
				cs_rafael.close();
				// System.out.println("RFAefrrrrmefnjernboernber");
	}

	@AfterClass
	public static void tearDown() throws InterruptedException, IOException {
		new ProcessBuilder("rabbitmqctl", "stop_app").inheritIO().start().waitFor();
		new ProcessBuilder("rabbitmqctl", "stop").inheritIO().start().waitFor();
	}
}

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
package vlibtour.vlibtour_lobby_room_server;

import vlibtour.vlibtour_lobby_room_api.InAMQPPartException;
import vlibtour.vlibtour_lobby_room_api.VLibTourLobbyService;

import java.io.IOException;
import java.util.concurrent.TimeoutException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.tools.jsonrpc.JsonRpcServer;

/**
 * This class implements the VLibTour lobby room service. This is the entry
 * point of the system for the clients when they want to start a tour.
 * <p>
 * When launched in its own process via a {@code java} command in shell script,
 * there is no call to {@link #close()}: the process is killed in the shell
 * script that starts this process. But, the class is a
 * {@link java.lang.Runnable} so that the lobby room server can be integrated in
 * another process.
 * 
 * @author Denis Conan
 */
public class VLibTourLobbyServer implements Runnable, VLibTourLobbyService {

	/**
	 * creates the lobby room server and the corresponding JSON server object.
	 * 
	 * @throws InAMQPPartException the exception thrown in case of a problem in the
	 *                             AMQP part.
	 */

	/**
	 * the connection to the RabbitMQ broker.
	 */
	private Connection connection;
	/**
	 * the channel for consuming and producing.
	 */
	private Channel channel;
	/**
	 * the RabbitMQ JSON RPC server.
	 */
	private JsonRpcServer rpcServer;
	/**
	 * global factory.
	 */
	private ConnectionFactory factory;

	/**
	 * class constructore.
	 */
	public VLibTourLobbyServer() throws InAMQPPartException, IOException, TimeoutException {
		factory = new ConnectionFactory();
		factory.setHost("localhost");
		connection = factory.newConnection();
		channel = connection.createChannel();
		channel.exchangeDeclare(VLibTourLobbyService.EXCHANGE_NAME, "direct");
		String queueName = channel.queueDeclare().getQueue();
		channel.queueBind(queueName, VLibTourLobbyService.EXCHANGE_NAME, VLibTourLobbyService.BINDING_KEY);
		rpcServer = new JsonRpcServer(channel, queueName, VLibTourLobbyService.class, this);
		System.out.println("Creation VLibTourLobbyServer finished");
	}

	@Override
	public String createGroupAndJoinIt(final String groupId, final String userId){
		String password = "1234";

		try {
			// Creating a virtual host to isolate the group
			new ProcessBuilder("rabbitmqctl", "add_vhost", groupId).inheritIO().start().waitFor();
			// Creating a new user in the group and setting its permissions
			new ProcessBuilder("rabbitmqctl", "add_user", userId, password).inheritIO().start().waitFor();
			new ProcessBuilder("rabbitmqctl", "set_permissions","-p", groupId, userId, ".*",".*",".*").inheritIO().start().waitFor();
		} catch (IOException | InterruptedException e) {
			System.out.println("catch error");
		}
		return "amqp://" + userId + ":" + password + "@" + factory.getHost() + ":" + factory.getPort() + "/" + groupId;
	}

	@Override
	public String joinAGroup(final String groupId, final String userId) {
		String password = "5678";

		try {
			// Creating a new user in the group and setting its permissions
			new ProcessBuilder("rabbitmqctl", "add_user", userId, password).inheritIO().start().waitFor();
			new ProcessBuilder("rabbitmqctl", "set_permissions","-p", groupId, userId, ".*",".*",".*").inheritIO().start().waitFor();
		} catch (IOException | InterruptedException e) {
			System.out.println("catch error");
		}
		return "amqp://" + userId + ":" + password + "@" + factory.getHost() + ":" + factory.getPort() + "/" + groupId;
	}

	@Override
	public void leaveAGroup(final String groupId, final String userId) {
		try {
			// Deleting a user of the group
			new ProcessBuilder("rabbitmqctl", "delete_user", userId).inheritIO().start().waitFor();
		} catch (IOException | InterruptedException e) {
			System.out.println("catch error");
		}
		return; 
	}

	/**
	 * creates the JSON RPC server and enters into the main loop of the JSON RPC
	 * server. The exit to the main loop is done when calling
	 * {@code stopLobbyRoom()} on the administration server. At the end of this
	 * method, the connectivity is closed.
	 */
	@Override
	public void run() {
		// throw new UnsupportedOperationException("No implemented, yet");
		try {
			rpcServer.mainloop();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * calls for the termination of the main loop if not already done and then
	 * closes the connection and the channel of this server.
	 * 
	 * @throws InAMQPPartException the exception thrown in case of a problem in the
	 *                             AMQP part.
	 */
	public void close() throws InAMQPPartException, IOException, TimeoutException {
		// throw new UnsupportedOperationException("No implemented, yet");
		if (rpcServer != null) {
			rpcServer.terminateMainloop();
			rpcServer.close();
		}
		if (channel != null) {
			channel.close();
		}
		if (connection != null) {
			connection.close();
		}
	}

	/**
	 * starts the lobby server.
	 * <p>
	 * When launched in its own process via a {@code java} command in shell script,
	 * there is no call to {@link #close()}: the process is killed in the shell
	 * script that starts this process.
	 * 
	 * @param args command line arguments
	 * @throws Exception all the potential problems (since this is a demonstrator,
	 *                   apply the strategy "fail fast").
	 */
	public static void main(final String[] args) throws Exception {
		System.out.println("Initiating VLibTourLobbyServer");
		VLibTourLobbyServer rpcServer = new VLibTourLobbyServer();
		rpcServer.run();
	}
}

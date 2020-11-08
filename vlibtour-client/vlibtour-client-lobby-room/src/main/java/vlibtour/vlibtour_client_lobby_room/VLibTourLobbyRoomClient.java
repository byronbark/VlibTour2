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

Initial developer(s): Denis Conan
Contributor(s):	Rafael Blanco
				Zujany Salazar
 */
package vlibtour.vlibtour_client_lobby_room;

// import vlibtour.vlibtour_lobby_room_server.VLibTourLobbyServer;
import vlibtour.vlibtour_lobby_room_api.VLibTourLobbyService;


import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.tools.jsonrpc.JsonRpcClient;
import com.rabbitmq.tools.jsonrpc.JsonRpcException;

/**
 * This class is the client application of the tourists.
 * 
 * @author Denis Conan
 */
public class VLibTourLobbyRoomClient {
	/**
	 * the connection to the RabbitMQ broker.
	 */
	private Connection connection;
	/**
	 * the channel for producing and consuming.
	 */
	private Channel channel;
	/**
	 * the RabbitMQ JSON RPC client.
	 */
	private JsonRpcClient jsonRpcClient;
	/**
	 * the Lobby Room service.
	 */
	private VLibTourLobbyService client;
	/**
	 * group id that will be assgined as name of the vhost
	 */
	private String groupId;
	/**
	 * id ohf the user that wants to use the system
	 */
	private String userId;

	/**
	 * constructore of the Looby Room proxy.
	 * @param tourId
	 *				id of the tour 
	 * 
	 * @param userId
	 *				id ohf the user that wants to use the system
	 * 
	 */

	public VLibTourLobbyRoomClient(String tourId, String userId) throws IOException, TimeoutException, JsonRpcException{
		System.out.println("Initiating VLibTourLobbyRoomClient for create group and join it");
		this.groupId = tourId + VLibTourLobbyService.GROUP_TOUR_USER_DELIMITER + userId;
		this.userId = userId;
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("localhost");
		connection = factory.newConnection();
		channel = connection.createChannel();
		jsonRpcClient = new JsonRpcClient(channel, VLibTourLobbyService.EXCHANGE_NAME, VLibTourLobbyService.BINDING_KEY);
		client = (VLibTourLobbyService) jsonRpcClient.createProxy(VLibTourLobbyService.class);
	}

	/**
	 * constructore of the Looby Room proxy.
	 *
	 * @param groupId
	 *				id of the group that other tourist already create
	 *
	 * @param tourId
	 *				id of the tour 
	 * 
	 * @param userId
	 *				id ohf the user that wants to use the system
	 * 
	 */
	public VLibTourLobbyRoomClient(String groupId, String tourId, String userId) throws IOException, TimeoutException, JsonRpcException{
		System.out.println("Initiating VLibTourLobbyRoomClient only for join group");
		this.groupId = groupId;
		this.userId = userId;
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("localhost");
		connection = factory.newConnection();
		channel = connection.createChannel();
		jsonRpcClient = new JsonRpcClient(channel, VLibTourLobbyService.EXCHANGE_NAME, VLibTourLobbyService.BINDING_KEY);
		client = (VLibTourLobbyService) jsonRpcClient.createProxy(VLibTourLobbyService.class);
	}

	/**
	 * communicate with the server to create and join a vhost that host the communication.
	 *
	 * @return url to configure the communication system
	 * 
	 */
	public String createGroupAndJoinIt(){
		System.out.println("Requesting createGroupAndJoinIt");
		return client.createGroupAndJoinIt(this.groupId, this.userId);
	}

	/**
	 * communicate with the server to join a vhost that host the communication.
	 *
	 * @return url to configure the communication system
	 * 
	 */
	public String joinAGroup(){
		System.out.println("Requesting joinAGroup");
		return client.joinAGroup(this.groupId, this.userId);
	}

	/**
	 * communicate with the server to leave the communication system.
	 * 
	 */
	public void leaveAGroup(){
		System.out.println("Requesting leaveAGroup");
		client.leaveAGroup(this.groupId, this.userId);
		return;
	}
}

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
package vlibtour.vlibtour_client_group_communication_system;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;
import java.util.HashMap;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

import vlibtour.vlibtour_client_lobby_room.VLibTourLobbyRoomClient;
import com.rabbitmq.tools.jsonrpc.JsonRpcException;
import java.net.URISyntaxException;
import java.security.NoSuchAlgorithmException;
import java.security.KeyManagementException;

/**
 * This class is the client application of the tourists.
 * 
 * @author Denis Conan
 */
public class VLibTourGroupCommunicationSystemClient {
	private Connection connection;
	/**
	 * the channel for producing.
	 */
	private Channel channel;
	/**
	 * the routing key for producing.
	 */
	private String routingKey;
	/**
	 * the message to produce.
	 */
	private String message;
	/**
	 * name of the exchange for the communication.
	 */
	private String exchange_name;

	private Consumer consumer;
	/**
	 * name of the queue for the communication.
	 */
	private String queueName; //QUEDA PENDIENTE EL NOMBRE DE LA COLA
	/**
	 * dic that contains information provided by the other tourists.
	 */
	private HashMap<String, String> partners_position; 
	/**
	 * client to interact with the lobby room server.
	 */
	private VLibTourLobbyRoomClient lobby_room_client;

	// public VLibTourGroupCommunicationSystemClient(final String group_id, final String tour_id, final String user_id, final String consumer_id, final String topic, final String message){
	public VLibTourGroupCommunicationSystemClient(final String group_id, final String tour_id, final String user_id_group_creator, final String user_id, final Boolean group_creator)  throws IOException, TimeoutException,JsonRpcException, URISyntaxException, NoSuchAlgorithmException, KeyManagementException{

		partners_position = new HashMap<String, String>();

		lobby_room_client = group_creator ? (new VLibTourLobbyRoomClient(tour_id, user_id)) : (new VLibTourLobbyRoomClient(tour_id+"_"+user_id_group_creator, tour_id, user_id));

		String urlToGCS = group_creator ? lobby_room_client.createGroupAndJoinIt() : lobby_room_client.joinAGroup();

		System.out.println("url returned: " + urlToGCS + ", para setUri");


		// EXCHANGE CREATION
		ConnectionFactory factory = new ConnectionFactory();
		factory.setUri(urlToGCS);
		// factory.setHost("localhost");
		connection = factory.newConnection();
		channel = connection.createChannel();
		exchange_name = group_id+"_"+user_id_group_creator;
		channel.exchangeDeclare(exchange_name, BuiltinExchangeType.TOPIC);
		//QUEUE CREATION
		queueName = tour_id+"_"+user_id;
		channel.queueDeclare(queueName, true, false, false, null);
		// System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

		consumer = new DefaultConsumer(channel) {
			@Override
			public void handleDelivery(final String consumerTag, final Envelope envelope,
					final AMQP.BasicProperties properties, final byte[] body) throws IOException {
				String message = new String(body, "UTF-8");
				partners_position.put(envelope.getRoutingKey().split("\\.",3)[0],message);
			}
		};
		//BIDING PROCESS
		this.addConsumer(queueName, "*.all.#");
		this.addConsumer(queueName, "*."+queueName+".#");
		//CONSUME
		this.startConsumption(consumer, queueName);
	}

	/**
	 * publishes a message.
	 * @param user_id
	 *				id of the user that wants to publish
	 * 
	 * @param consumer_id
	 *				id of the user that will be able to consume
	 * 
	 * @param type
	 *				type of message (location, advertisement, etc)
	 * 
	 * @param message
	 *				message to publish
	 * 
	 * @throws UnsupportedEncodingException
	 *             problem when encoding the message.
	 * @throws IOException
	 *             communication problem with the broker.
	 */
	public void publish(final String user_id, final String consumer_id, final String type, final String message) throws UnsupportedEncodingException, IOException {
		channel.basicPublish(this.exchange_name, user_id+"."+consumer_id+"."+type, null, message.getBytes("UTF-8"));
		return;
	}

	/**
	 * Bind a queue to consume.
	 * @param queueName
	 *				name of the queue
	 * 
	 * @param bindingKey
	 *				key used to subscribe to a topic content
	 * 
	 * @throws TimeoutException
	 *             problem with response time.
	 * @throws IOException
	 *             communication problem with the broker.
	 */
	public void addConsumer(final String queueName, final String bindingKey) throws IOException, TimeoutException{
		channel.queueBind(queueName, this.exchange_name, bindingKey);
		return;
	}

	/**
	 * Bind a queue to consume.
	 * @param consumer
	 *				actions to do when consume.
	 *
	 * @param queueName
	 *				name of the queue
	 * 
	 * @throws IOException
	 *             communication problem with the broker.
	 *
	 * @return process message.
	 *	
	 */
	public String startConsumption(final Consumer consumer, final String queueName) throws IOException{
		return channel.basicConsume(queueName, true, consumer);
	}

	/**
	 * get dictionary that contains information communicated.
	 * @return dictionary that contains information communicated.
	 *			
	 */
	public HashMap<String, String> getPatnersPosition(){
		return this.partners_position;
	}

	/**
	 * Use the lobby room proxy to leave the communication group.
	 */
	public void leaveAGroup() {
		lobby_room_client.leaveAGroup();
		return;
	}

	/**
	 * closes the channel and the connection with the broker.
	 * 
	 * @throws IOException
	 *             communication problem.
	 * @throws TimeoutException
	 *             broker to long to communicate with.
	 */
	public void close() throws IOException, TimeoutException {
		channel.close();
		connection.close();
	}

	/**
	 * Main class.
	 * @param argv
	 *				list arguments used to construct the client.
	 *
	 * 
	 * @throws Exception
	 *             problems.
	 */
	public static void main(final String[] argv) throws Exception {
		String group_id = argv[0];
		String tour_id = argv[1];
		String user_id_group_creator = argv[2];
		String user_id = argv[3];
		Boolean group_creator = argv[4].equals("true") ? true : false;
		VLibTourGroupCommunicationSystemClient emitVlibTopic = new VLibTourGroupCommunicationSystemClient(group_id, tour_id, user_id_group_creator, user_id, group_creator);
	}

}

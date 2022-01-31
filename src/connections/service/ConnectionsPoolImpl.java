package connections.service;

import java.util.HashMap;

import connections.dto.Connection;

public class ConnectionsPoolImpl implements ConnectionsPool {
	private static class Node {
		 Connection connection;
		 Node prev;
		 Node next;
		 
		 Node(Connection c) {
			 this.connection = c;
		 }
	}
	private static class ConnectionsList {
		Node head;
		Node tail;
	}
	ConnectionsList list = new ConnectionsList();
	HashMap<Integer, Node> mapConnections = new HashMap<>();
	int connectionsPoolLimit;
	public ConnectionsPoolImpl(int limit) {
		this.connectionsPoolLimit = limit;
	}

	@Override
	public boolean addConnection(Connection connection) {
		// TODO Auto-generated method stub
		Node newNode = new Node(connection);
		if (mapConnections.size() == connectionsPoolLimit) {
			mapConnections.remove(list.head.connection.getId());
			list.head.next.prev = null;
			list.head = list.head.next;
		}
		if (list.head == null) {
			list.head = list.tail = newNode;
			mapConnections.put(connection.getId(), newNode);
			return true;
		}
		insertTail(newNode);
		
		mapConnections.put(connection.getId(), newNode);
		return true;
	}

	@Override
	public Connection getConnection(int id) {
		// TODO Auto-generated method stub
		Connection resConnection = mapConnections.containsKey(id) ? mapConnections.get(id).connection : null;
		
		Node resNode = mapConnections.get(id);
		if (resNode.next == null) {
			return resConnection;
		}
		if (resNode.prev == null) {
			resNode.next.prev = null;
			list.head = resNode.next;
		} else {
			resNode.prev.next = resNode.next;
			resNode.next.prev = resNode.prev;
		}
		insertTail(resNode);
		resNode.next = null;
		
		return resConnection;
	}
	
	private void insertTail(Node node) {
		list.tail.next = node;
		node.prev = list.tail;
		list.tail = node;
	}

}

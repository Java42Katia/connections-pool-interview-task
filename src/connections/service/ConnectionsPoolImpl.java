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
		int size = 0;
		
		boolean add(Node node) {
			if (this.size == 0) {
				this.head = this.tail = node;
			}
			if (this.size == 5) {
				this.tail = this.tail.prev;
				this.tail.next = null;
				size--;
			}
			insertNewest(node);
			size++;
			return true;
		}
		
		void update(Node node) {
			if (node != null && node.prev != null) {
				if (node.next != null) {
				node.next.prev = node.prev;
				node.prev.next = node.next;
				}
				insertNewest(node);
			}
		}
		
		private void insertNewest(Node node) {
			this.head.prev = node;
			node.next = this.head;
			this.head = node;
		}
		public int size() {
			return this.size;
		}

		public Node getOldestNode() {
			return this.tail;
		}
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
		if (mapConnections.containsValue(newNode)) {
			
		} else {
			list.add(newNode);
			if (mapConnections.size() == 5) {
				mapConnections.remove(list.getOldestNode());
			}
		}
		mapConnections.put(connection.getId(), newNode);
		return true;
	}

	@Override
	public Connection getConnection(int id) {
		// TODO Auto-generated method stub
		
		Node resNode = mapConnections.get(id);
		list.update(resNode);
		
		
		return mapConnections.containsKey(id) ? mapConnections.get(id).connection : null;
	}
	
	
	public Connection getNewestConnectionInfo() {
		return list.head.connection;
	}
 
	public int size() {
		return this.list.size();
	}

}

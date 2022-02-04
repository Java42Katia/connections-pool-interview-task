package connections.service;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Random;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import connections.dto.Connection;

class ConnectionsPoolTest {
	static int connectionsPoolLimit = 5;
	static int globalId = 1001;
	ConnectionsPoolImpl connections;

	@BeforeEach
	void setUp() throws Exception {
		connections = new ConnectionsPoolImpl(connectionsPoolLimit);
	}

	@Test
	void testAddConnection() {
		addConnections(1);
		assertEquals(1, connections.size());
		connections.addConnection(new Connection(125, "10.0.0.125", 80));
		connections.addConnection(new Connection(125, "10.0.0.125", 80));
		Connection ac = new Connection(123, "10.0.0.1", 80);
		connections.addConnection(ac);
		assertEquals(123, connections.getNewestConnectionInfo().getId());
		addConnections(4);
		assertEquals(5, connections.size());
		addConnections(1);
		assertEquals(5, connections.size());
	}

	@Test
	void testGetConnection() {
		connections.addConnection(new Connection(123, "10.0.0.1", 80));
		addConnections(2);
		assertEquals(80, connections.getConnection(123).getPort());
		addConnections(4);
		connections.addConnection(new Connection(123, "10.0.0.1", 80));
		assertEquals(123, connections.getNewestConnectionInfo().getId());
		connections.addConnection(new Connection(125, "10.0.0.125", 80));
		addConnections(5);
		connections.getConnection(125);
		assertEquals(125, connections.getNewestConnectionInfo().getId());
		
	}
	
	private void addConnections(int count) {
		for (int i = 0; i < count; i++) {
			connections.addConnection(new Connection(globalId++, getRandomIp(), 80));
		}
	}

	private String getRandomIp() {
		Random r = new Random();
		return r.nextInt(256) + "." + r.nextInt(256) + "." + r.nextInt(256) + "." + r.nextInt(256);
	}

}

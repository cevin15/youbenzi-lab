package com.youbenzi.zookeeper.servicepool;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.KeeperException.NoNodeException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;

/**
 * 服务池的Zookeeper实现
 * @author yangyingqiang
 * @time Jan 12, 2018 4:18:43 PM
 */
class ServicePoolZookeeperImpl implements ServicePool {

	private static ZooKeeper zk;
	private static Map<String, List<String>> serviceAndHosts = new ConcurrentHashMap<>();

	@Override
	public void init(String connectString, int timout) throws ServicePoolException {
		try {
			zk = new ZooKeeper(connectString, timout, null);
		} catch (IOException e) {
			throw new ServicePoolException(e);
		}
	}

	@Override
	public void register(String service, String host) throws ServicePoolException {
		try {
			try {
				createDataNode(service, host);
			} catch (NoNodeException e) {
				createServiceNode(service);
				createDataNode(service, host);
			}
		} catch (KeeperException | InterruptedException e) {
			throw new ServicePoolException(e);
		}
	}
	
	private void createDataNode(String service, String host) throws KeeperException, InterruptedException {
		zk.create("/" + service + "/data", host.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE,
				CreateMode.EPHEMERAL_SEQUENTIAL);
	}
	
	private void createServiceNode(String service) throws KeeperException, InterruptedException {
		zk.create("/" + service, null, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
	}

	@Override
	public String getHost(String service) throws ServicePoolException {
		List<String> list = serviceAndHosts.get(service);
		if (list == null) {
			synchronized (this) {
				list = serviceAndHosts.get(service);
				if (list == null) {
					try {
						list = initHosts(service);
					} catch (KeeperException | InterruptedException e) {
						throw new ServicePoolException(e);
					}	
				}	
			}
		}
		if (list.size() == 0) {
			return null;
		}
		Random random = new Random();
		return list.get(random.nextInt(list.size()));
	}
	
	private List<String> initHosts(String service) throws KeeperException, InterruptedException {
		
		List<String> keys = zk.getChildren("/" + service, new Watcher() {
			@Override
			public void process(WatchedEvent event) {
				if (event.getType() == Event.EventType.NodeChildrenChanged) {
					try {
						initHosts(service);
					} catch (KeeperException | InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		});
		List<String> hosts = new ArrayList<>();
		for (String key : keys) {
			hosts.add(new String(zk.getData("/" + service + "/" + key, false, null)));	
		}
		serviceAndHosts.put(service, hosts);
		
		return hosts;
	}

	@Override
	public void close() throws ServicePoolException {
		try {
			zk.close();
		} catch (InterruptedException e) {
			throw new ServicePoolException(e);
		}
	}

}

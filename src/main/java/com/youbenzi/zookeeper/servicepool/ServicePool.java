package com.youbenzi.zookeeper.servicepool;

/**
 * 服务池的接口
 * @author yangyingqiang
 * @time Jan 12, 2018 4:13:53 PM
 */
public interface ServicePool {

	public static final ServicePool ME = new ServicePoolZookeeperImpl();
	
	/**
	 * 初始化服务池
	 * @param connectString
	 * @param timout
	 * @throws ServicePoolException
	 */
	public void init(String connectString, int timout) throws ServicePoolException;
	
	/**
	 * 注册一个服务到连接池
	 * @param service
	 * @param host
	 * @throws ServicePoolException
	 */
	public void register(String service, String host) throws ServicePoolException;
	
	/**
	 * 随机获取一个服务Host。
	 * 如果池中的服务发生变化，池会重新加载可用的服务Host
	 * @param service
	 * @return
	 * @throws ServicePoolException
	 */
	public String getHost(String service) throws ServicePoolException;
	
	/**
	 * 关闭服务池
	 * @throws ServicePoolException
	 */
	public void close() throws ServicePoolException;
	
}

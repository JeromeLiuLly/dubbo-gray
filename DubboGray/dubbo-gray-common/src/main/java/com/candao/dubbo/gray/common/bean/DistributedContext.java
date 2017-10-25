package com.candao.dubbo.gray.common.bean;

import java.io.Serializable;
import java.util.UUID;

public class DistributedContext implements Serializable {
	private static final long serialVersionUID = -1574291343703475131L;

	/**
	 * B端登录token
	 */
	public String token;

	/**
	 * log日志id
	 */
	private String logId;

	/**
	 * 额外自定义对象
	 */
	public Object extData;

	/** 请求Ip */
	public String ip;
	
	/**
	 * 灰度对象信息
	 */
	public GrayBean grayBean;
	
	/**
	 * 灰度开关
	 */
	public boolean graySwitch;

	/**
	 * 自动生成一个logId
	 */
	public DistributedContext() {
		this(0);
	}

	public DistributedContext(int clientType) {
		this(clientType, "");
	}

	public DistributedContext(int clientType, String platformKey) {
		this.logId = UUID.randomUUID().toString();
	}

	/**
	 * 创建一个空的DistributedContext，只有logid
	 * 
	 * @return
	 */
	public static DistributedContext newInstance() {
		DistributedContext context = new DistributedContext(0);
		return context;
	}

	/**
	 * 创建一个空的DistributedContext，保留参数对应的logid、step值 串联所有请求、响应日志id
	 * 
	 * @return
	 */
	public static DistributedContext newInstance(DistributedContext logContext) {
		DistributedContext context = new DistributedContext(0);
		context.logId = logContext.logId;
		return context;
	}

	/**
	 * 获取日志id
	 * 
	 * @return
	 */
	public String getLogId() {
		return this.logId;
	}
	
	public GrayBean getGrayBean() {
		return grayBean;
	}

	public void setGrayBean(GrayBean grayBean) {
		this.grayBean = grayBean;
	}

	public static class GrayBean implements Serializable{
		
		/**
		 * 
		 */
		private static final long serialVersionUID = -1604853169834915132L;


		/**
		 * 灰度对象
		 */
		private String userName;
		
		
		/**
		 * 灰度对象key
		 */
		private String grayKey;
		
		
		/**
		 * 灰度对象value
		 */
		private String grayValue;
		
		
		/**
		 * 灰度对象策略
		 */
		private Integer grayStrategy;


		public String getUserName() {
			return userName;
		}


		public void setUserName(String userName) {
			this.userName = userName;
		}


		public String getGrayKey() {
			return grayKey;
		}


		public void setGrayKey(String grayKey) {
			this.grayKey = grayKey;
		}


		public String getGrayValue() {
			return grayValue;
		}


		public void setGrayValue(String grayValue) {
			this.grayValue = grayValue;
		}


		public Integer getGrayStrategy() {
			return grayStrategy;
		}


		public void setGrayStrategy(Integer grayStrategy) {
			this.grayStrategy = grayStrategy;
		}
	}

}

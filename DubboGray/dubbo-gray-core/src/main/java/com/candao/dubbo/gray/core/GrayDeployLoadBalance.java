package com.candao.dubbo.gray.core;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.rpc.Invocation;
import com.alibaba.dubbo.rpc.Invoker;
import com.alibaba.dubbo.rpc.cluster.loadbalance.AbstractLoadBalance;
import com.candao.dubbo.gray.common.bean.DistributedContext;
import com.candao.dubbo.gray.common.bean.DistributedContext.GrayBean;
import com.candao.dubbo.gray.core.rule.RandomLoadBalance;
import com.candao.dubbo.gray.core.rule.RoundRobinLoadBalance;

public class GrayDeployLoadBalance extends AbstractLoadBalance {

	public static final String NAME = "grayrandom";

	public GrayDeployLoadBalance() {
		System.out.println("灰度发布-服务选择器已启动...");
	}

	/* (non-Javadoc)
	 * @see com.alibaba.dubbo.rpc.cluster.loadbalance.AbstractLoadBalance#doSelect(java.util.List, com.alibaba.dubbo.common.URL, com.alibaba.dubbo.rpc.Invocation)
	 */
	@Override
	protected <T> Invoker<T> doSelect(List<Invoker<T>> invokers, URL url, Invocation invocation) {
		// 拿到调用的方法的参数
		Object[] args = invocation.getArguments();
		DistributedContext context = null;
		if (args != null) {
			for(Object object : args){
				if (object instanceof DistributedContext){
					context = (DistributedContext) object;
					break;
				}
			}
		}
		
		System.out.println("url::::" + url);
		
		//定义新的Invoker列表
        List<Invoker<T>> newInvokers = new ArrayList<Invoker<T>>();
        
        
        //断言不存在上下文对象或者灰度开关是关闭状态【存在上下文,灰度开发是关闭状态】
        if (context == null || !context.graySwitch) {
        	newInvokers = doSelectWithRelease(invokers);
        	if (newInvokers.isEmpty()) {
            	System.out.println("找不到对应的服务调用");
            	return null;
            }
        	//默认走轮需的策略
        	return RoundRobinLoadBalance.getInstance().select(newInvokers, url, invocation);
        }
        
		//断言存在上下文对象和灰度开关是启动状态
		if (context != null && context.graySwitch) {
			//上下文对象存在,且灰度开关是启动状态,正常用户对象,走正常标签服务
			GrayBean gray = context.getGrayBean();
			if (gray == null) {
				newInvokers = doSelectWithRelease(invokers);
				return RoundRobinLoadBalance.getInstance().select(newInvokers, url, invocation);
			}
			for (Invoker<T> invoker : invokers) {
				URL serviceUrl = invoker.getUrl();
				String grayKey = gray.getGrayKey();
				if (serviceUrl.hasParameter(grayKey) && gray.getGrayValue().equals(serviceUrl.getParameter(grayKey))) {
					newInvokers.add(invoker);
				}
			}
			
			if (!newInvokers.isEmpty() && newInvokers.size() > 0) {
				
				//根据灰度策略进行选择路由的规则
				if (gray.getGrayStrategy() == null || gray.getGrayStrategy() == 1) {
					return RoundRobinLoadBalance.getInstance().select(newInvokers, url, invocation);
				}else if(gray.getGrayStrategy() == 2){
					return RandomLoadBalance.getInstance().select(newInvokers, url, invocation);
				}
			//遇到灰度开关启动,并且是灰度对象,但根据灰度key查找不到对应的灰度服务,默认走正常服务
			}else{
				newInvokers = doSelectWithRelease(invokers);
				//根据灰度策略进行选择路由的规则
				if (gray.getGrayStrategy() == null || gray.getGrayStrategy() == 1) {
					return RoundRobinLoadBalance.getInstance().select(newInvokers, url, invocation);
				}else if(gray.getGrayStrategy() == 2){
					return RandomLoadBalance.getInstance().select(newInvokers, url, invocation);
				}
			}
		}
		
		//根据策略进行选择随机策略
		return RoundRobinLoadBalance.getInstance().select(newInvokers, url, invocation);
	}
	
	private <T> List<Invoker<T>> doSelectWithRelease(List<Invoker<T>> invokers){
		List<Invoker<T>> newInvokers = new ArrayList<Invoker<T>>();
		for (Invoker<T> invoker : invokers) {
    		//特定的正常服务标签
    		if (invoker.getUrl().hasParameter("release")) {
    			newInvokers.add(invoker);
    		}
    	}
		return newInvokers;
	}
}

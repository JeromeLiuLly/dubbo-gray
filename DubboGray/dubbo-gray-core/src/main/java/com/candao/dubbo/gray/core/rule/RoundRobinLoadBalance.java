package com.candao.dubbo.gray.core.rule;

import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.common.utils.AtomicPositiveInteger;
import com.alibaba.dubbo.rpc.Invocation;
import com.alibaba.dubbo.rpc.Invoker;
import com.alibaba.dubbo.rpc.cluster.loadbalance.AbstractLoadBalance;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class RoundRobinLoadBalance extends AbstractLoadBalance {

	public static final String NAME = "roundrobin";

	private final ConcurrentMap<String, AtomicPositiveInteger> sequences = new ConcurrentHashMap<String, AtomicPositiveInteger>();
	private static RoundRobinLoadBalance instance;

	private RoundRobinLoadBalance() {
	}

	public static RoundRobinLoadBalance getInstance() {
		if (instance == null) {
			instance = new RoundRobinLoadBalance();
		}
		return instance;
	}

	protected <T> Invoker<T> doSelect(List<Invoker<T>> invokers, URL url, Invocation invocation) {
		String key = invokers.get(0).getUrl().getServiceKey() + "." + invocation.getMethodName();
		int length = invokers.size(); // 总个数
		int maxWeight = 0; // 最大权重
		int minWeight = Integer.MAX_VALUE; // 最小权重
		final LinkedHashMap<Invoker<T>, IntegerWrapper> invokerToWeightMap = new LinkedHashMap<Invoker<T>, IntegerWrapper>();
		int weightSum = 0;
		for (int i = 0; i < length; i++) {
			int weight = getWeight(invokers.get(i), invocation);
			maxWeight = Math.max(maxWeight, weight); // 累计最大权重
			minWeight = Math.min(minWeight, weight); // 累计最小权重
			if (weight > 0) {
				invokerToWeightMap.put(invokers.get(i), new IntegerWrapper(weight));
				weightSum += weight;
			}
		}
		AtomicPositiveInteger sequence = sequences.get(key);
		if (sequence == null) {
			sequences.putIfAbsent(key, new AtomicPositiveInteger());
			sequence = sequences.get(key);
		}
		int currentSequence = sequence.getAndIncrement();
		if (maxWeight > 0 && minWeight < maxWeight) { // 权重不一样
			int mod = currentSequence % weightSum;
			for (int i = 0; i < maxWeight; i++) {
				for (Map.Entry<Invoker<T>, IntegerWrapper> each : invokerToWeightMap.entrySet()) {
					final Invoker<T> k = each.getKey();
					final IntegerWrapper v = each.getValue();
					if (mod == 0 && v.getValue() > 0) {
						return k;
					}
					if (v.getValue() > 0) {
						v.decrement();
						mod--;
					}
				}
			}
		}
		// 取模轮循
		return invokers.get(currentSequence % length);
	}

	private static final class IntegerWrapper {
		private int value;

		public IntegerWrapper(int value) {
			this.value = value;
		}

		public int getValue() {
			return value;
		}

		public void setValue(int value) {
			this.value = value;
		}

		public void decrement() {
			this.value--;
		}
	}

}
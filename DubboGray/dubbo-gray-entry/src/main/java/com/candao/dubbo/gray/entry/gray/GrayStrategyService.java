package com.candao.dubbo.gray.entry.gray;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.fastjson.JSONObject;
import com.candao.dubbo.gray.common.bean.DistributedContext.GrayBean;
import com.candao.dubbo.gray.entry.net.HttpClient;
import com.candao.dubbo.gray.entry.net.HttpResult;

public class GrayStrategyService {

	// 检测灰度开关是否启动
	public static HttpResult checkGraySwitch() {
		String url = "http://10.200.102.136:8080/eureka/apps/switch";
		HttpResult result = new HttpResult();
		result.statusCode = 500;
		try {
			result = HttpClient.get(url, null);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return result;
	}

	public static GrayBean getUser(String userName) {
		String url = "http://10.200.102.136:8080/user/" + userName + "/getUser";
		try {
			HttpResult result = HttpClient.get(url, null);
			if (!StringUtils.isEmpty(result.content)) {
				GrayBean grayUser = JSONObject.parseObject(result.content, GrayBean.class);
				return grayUser;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}
}

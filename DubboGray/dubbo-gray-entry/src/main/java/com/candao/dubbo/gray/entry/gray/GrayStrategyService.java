package com.candao.dubbo.gray.entry.gray;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.fastjson.JSONObject;
import com.candao.dubbo.gray.common.bean.DistributedContext.GrayBean;
import com.candao.dubbo.gray.entry.net.HttpClient;
import com.candao.dubbo.gray.entry.net.HttpResult;
import com.candao.dubbo.gray.user.api.bean.GrayUser;

public class GrayStrategyService {

	// 检测灰度开关是否启动
	public static HttpResult checkGraySwitch() {
		String url = "http://localhost:8080/eureka/apps/switch";
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
		String url = "http://localhost:8080/user/" + userName + "/getUser";
		try {
			HttpResult result = HttpClient.get(url, null);
			if (!StringUtils.isEmpty(result.content)) {
				GrayUser grayUser = JSONObject.parseObject(result.content, GrayUser.class);
				GrayBean grayBean = new GrayBean();
				grayBean.setGrayKey(grayUser.getServiceTag());
				//grayBean.setGrayStrategy(grayUser.getStrategy());
				grayBean.setGrayValue(grayUser.getServiceValue());
				grayBean.setUserName(grayUser.getUserName());
				return grayBean;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}
}

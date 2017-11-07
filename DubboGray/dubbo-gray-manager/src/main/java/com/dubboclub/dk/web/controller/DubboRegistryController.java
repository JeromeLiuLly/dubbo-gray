package com.dubboclub.dk.web.controller;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.dubboclub.dk.admin.model.Override;
import com.dubboclub.dk.admin.model.Provider;
import com.dubboclub.dk.admin.service.OverrideService;
import com.dubboclub.dk.admin.service.ProviderService;
import com.dubboclub.dk.web.bean.GrayStrategy;
import com.dubboclub.dk.web.bean.Switch;
import com.dubboclub.dk.web.model.OverrideInfo;
import com.dubboclub.dk.web.service.GrayStrategyService;
import com.dubboclub.dk.web.service.GraySwitchService;
import com.dubboclub.dk.web.utils.HttpResult;

@RestController
@RequestMapping("/eureka")
public class DubboRegistryController {
	
	@Autowired
	private GrayStrategyService grayStrategyService;
	
	@Autowired
	private GraySwitchService graySwitchService;
	
	@Autowired
	private ProviderService providerService;
	
	@Autowired
	private OverrideService overrideService;
	
	@RequestMapping(value = "/apps/allStrategy", method = RequestMethod.GET)
	@ResponseBody
	public List<GrayStrategy> getAllStrategy(){
		return grayStrategyService.getAllStrategy();
	}
	
	@RequestMapping(value = "/apps/{id}/{applicationName}/{instanceId}/synMetaDataStrategy", method = RequestMethod.GET)
	@ResponseBody
	public HttpResult synMetaDataStrategy(
			@PathVariable("id") Integer id,
			@PathVariable("applicationName") String applicationName,
			@PathVariable("instanceId") String instanceId,
			HttpServletRequest request) throws Exception{
		
		HttpResult httpResult = new HttpResult();
		GrayStrategy grayStrategy = grayStrategyService.getStrategyById(id);
		if (grayStrategy == null) {
			httpResult.statusCode = 500;
			httpResult.errorMsg = "找不到灰度服务对象";
			return httpResult;
		}
		
		if (grayStrategy.getStatus() == 0) {
			httpResult.statusCode = 500;
			httpResult.errorMsg = "灰度服务的状态是关闭状态,请开启生效后操作";
			return httpResult;
		}
		
		List<Provider> providers = providerService.listProviderByApplication(instanceId);
		
		if (providers.isEmpty()) {
			httpResult.statusCode = 500;
			httpResult.errorMsg = "从注册中心上,获取不到对应的服务[应用]";
			return httpResult;
		}
		
		for(Provider provider : providers){
			List<Override> overrides = overrideService.listByProvider(provider);
			if (!provider.getAddress().equals(applicationName)) {
				continue;
			}
			if (!overrides.isEmpty()) {
				for(Override override : overrides){
					override.setParams(override.getParams()+"&"+request.getQueryString());
					overrideService.update(override);
				}
			}else{
				Override override = new Override();
				override.setAddress(provider.getAddress());
				override.setEnabled(true);
				override.setParams(request.getQueryString());
				override.setService(provider.getServiceKey());
				overrideService.add(override);
			}
		}
		httpResult.statusCode = 200;
		httpResult.errorMsg = "同步成功";
		return httpResult;
	}
	
	@RequestMapping(value = "/apps/{id}/{applicationName}/{instanceId}/removeMetaDataStrategy", method = RequestMethod.GET)
	@ResponseBody
	public HttpResult removeMetaDataStrategy(
			@PathVariable("id") Integer id,
			@PathVariable("applicationName") String applicationName,
			@PathVariable("instanceId") String instanceId,
			HttpServletRequest request) throws Exception{
		
		HttpResult httpResult = new HttpResult();
		GrayStrategy grayStrategy = grayStrategyService.getStrategyById(id);
		if (grayStrategy == null) {
			httpResult.statusCode = 500;
			httpResult.errorMsg = "找不到灰度服务对象";
			return httpResult;
		}
		
		if (grayStrategy.getStatus() == 0) {
			httpResult.statusCode = 500;
			httpResult.errorMsg = "灰度服务的状态是关闭状态,请开启生效后操作";
			return httpResult;
		}
		
		List<Provider> providers = providerService.listProviderByApplication(instanceId);
		
		if (providers.isEmpty()) {
			httpResult.statusCode = 500;
			httpResult.errorMsg = "从注册中心上,获取不到对应的服务[应用]";
			return httpResult;
		}
		
		for(Provider provider : providers){
			if (!provider.getAddress().equals(applicationName)) {
				continue;
			}
			List<Override> overrides = overrideService.listByProvider(provider);
			if (!overrides.isEmpty()) {
				for(Override override : overrides){
					OverrideInfo overrideInfo = new OverrideInfo();
					BeanUtils.copyProperties(override, overrideInfo);
					String key ="";
					String[] urlParams = override.getParams().split("&");
					List<String> newUrlParams = new ArrayList<String>();
					for(String param : urlParams){
						if (!param.equals(request.getQueryString())) {
							newUrlParams.add(param);
						}
					}
					for(String param : newUrlParams){
						key += param +"&";
					}
					if (!StringUtils.isEmpty(key)) {
						overrideInfo.setParameters(key.substring(0, key.length()-1));
						Override overrideTemp = overrideInfo.toOverride();
						overrideTemp.setId(override.getId());
						overrideTemp.setService(override.getService());
						overrideTemp.setUsername(override.getUsername());
						overrideTemp.setEnabled(true);
						overrideService.update(overrideTemp);
					}else{
						overrideService.delete(override);
					}
				}
			}
		}
		
		httpResult.statusCode = 200;
		httpResult.errorMsg = "分离成功";
		return httpResult;
	}
	
	@RequestMapping(value = "/apps/{id}/updateStrategy", method = RequestMethod.GET)
	@ResponseBody
	public HttpResult updateStrategy(@PathVariable("id") Integer id) throws Exception{
		
		HttpResult httpResult = new HttpResult();
		GrayStrategy grayStrategy = grayStrategyService.getStrategyById(id);
		if (grayStrategy == null) {
			httpResult.statusCode = 500;
			httpResult.errorMsg = "找不到灰度服务对象";
			return httpResult;
		}
		
		grayStrategy.setUpdateTime(new Timestamp(System.currentTimeMillis()));
		Integer status = grayStrategy.getStatus() > 0 ? 0 : 1;
		grayStrategy.setStatus(status);
		grayStrategyService.updateStrategyById(grayStrategy);
		
		httpResult.statusCode = 200;
		httpResult.errorMsg = "状态更新成功";
		return httpResult;
	}
	
	@RequestMapping(value = "/apps/editStrategy", method = RequestMethod.POST)
	@ResponseBody
	public HttpResult editStrategy(GrayStrategy grayStrategy) throws Exception{
		
		HttpResult httpResult = new HttpResult();
		if (grayStrategy == null) {
			httpResult.statusCode = 500;
			httpResult.errorMsg = "页面灰度服务对象传递失败";
			return httpResult;
		}
		GrayStrategy grayStrategyDB = grayStrategyService.getStrategyById(grayStrategy.getId());
		
		grayStrategyDB.setUpdateTime(new Timestamp(System.currentTimeMillis()));
		
		grayStrategyDB.setStatus(grayStrategy.getStatus());
		grayStrategyDB.setServiceTag(grayStrategy.getServiceTag());
		grayStrategyDB.setStrategyName(grayStrategy.getStrategyName());
		grayStrategyDB.setVersion(grayStrategy.getVersion());
		grayStrategyDB.setStrategyValue(grayStrategy.getStrategyValue());
		grayStrategyDB.setWeight(grayStrategy.getWeight());
		
		grayStrategyService.updateStrategyById(grayStrategyDB);
		
		
		httpResult.statusCode = 200;
		httpResult.errorMsg = "灰度服务更新成功";
		return httpResult;
	}
	
	@RequestMapping(value = "/apps/addStrategy", method = RequestMethod.POST)
	@ResponseBody
	public HttpResult addStrategy(GrayStrategy grayStrategy) throws Exception{
		
		GrayStrategy grayStrategyDB = new GrayStrategy();
		
		grayStrategyDB.setUpdateTime(new Timestamp(System.currentTimeMillis()));
		grayStrategyDB.setCreateTime(new Timestamp(System.currentTimeMillis()));
		grayStrategyDB.setServiceName(grayStrategy.getServiceName());
		grayStrategyDB.setServiceId(grayStrategy.getServiceId());
		grayStrategyDB.setStatus(grayStrategy.getStatus());
		grayStrategyDB.setServiceTag(grayStrategy.getServiceTag());
		grayStrategyDB.setStrategyName(grayStrategy.getStrategyName());
		grayStrategyDB.setVersion(grayStrategy.getVersion());
		grayStrategyDB.setStrategyValue(grayStrategy.getStrategyValue());
		grayStrategyDB.setWeight(grayStrategy.getWeight());
		HttpResult httpResult = new HttpResult();
		try {
			grayStrategyService.saveStrategy(grayStrategyDB);
			httpResult.statusCode = 200;
			httpResult.errorMsg = "灰度服务添加成功";
		} catch (Exception e) {
			e.printStackTrace();
			httpResult.statusCode = 500;
			httpResult.errorMsg = "灰度服务添加失败";
		}
		
		return httpResult;
	}
	
	@RequestMapping(value = "/apps/{id}/deleteStrategy", method = RequestMethod.GET)
	@ResponseBody
	public HttpResult deleteStrategy(@PathVariable("id") Integer id) throws Exception{
		
		HttpResult httpResult = new HttpResult();
		grayStrategyService.deleteStrategyById(id);
		
		httpResult.statusCode = 200;
		httpResult.errorMsg = "灰度服务删除成功";
		return httpResult;
	}
	
	@RequestMapping(value = "/apps/switch", method = RequestMethod.GET)
	@ResponseBody
	public HttpResult getSwitch() throws Exception{
		
		HttpResult httpResult = new HttpResult();
		Switch graySwitch = graySwitchService.getSwitch();
		if (graySwitch == null) {
			httpResult.statusCode = 500;
			httpResult.errorMsg = "灰度服务开关数据不存在";
			return httpResult;
		}
		
		httpResult.statusCode = 200;
		httpResult.errorMsg = graySwitch.getGraySwitch() == 1  ? "true" : "false";
		return httpResult;
	}
	
	@RequestMapping(value = "/apps/updateSwitch", method = RequestMethod.GET)
	@ResponseBody
	public HttpResult updateSwitch() throws Exception{
		
		HttpResult httpResult = new HttpResult();
		Switch graySwitch = graySwitchService.getSwitch();
		if (graySwitch == null) {
			httpResult.statusCode = 500;
			httpResult.errorMsg = "灰度服务开关数据不存在";
			return httpResult;
		}
		
		Integer status = graySwitch.getGraySwitch() == 1 ? 0 : 1;
		graySwitch.setGraySwitch(status);
		
		try {
			graySwitchService.updateSwitch(graySwitch);
			httpResult.statusCode = 200;
			httpResult.errorMsg = "灰度服务开关更新成功";
		} catch (Exception e) {
			e.printStackTrace();
			httpResult.statusCode = 500;
			httpResult.errorMsg = "灰度服务开关更新失败";
		}
		
		return httpResult;
	}

}

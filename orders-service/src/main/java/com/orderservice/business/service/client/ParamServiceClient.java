package com.orderservice.business.service.client;

import com.orderservice.business.web.dto.ParamDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Component
@FeignClient(url="${params-url}", name="ParamServiceClient")
public interface ParamServiceClient
{
	@GetMapping(value = "/param/api/v1/params/by-key/{key}")
	ParamDTO findByKey(@PathVariable("key") String key);
}

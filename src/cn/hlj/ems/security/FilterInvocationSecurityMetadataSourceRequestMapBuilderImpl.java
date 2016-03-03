package cn.hlj.ems.security;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.util.AntPathRequestMatcher;
import org.springframework.security.web.util.RequestMatcher;
import org.springframework.stereotype.Component;

import cn.hlj.ems.entities.Authority;
import cn.hlj.ems.entities.Resource;
import cn.hlj.ems.services.ResouceService;

@Component("filterInvocationSecurityMetadataSourceRequestMapBuilder")
public class FilterInvocationSecurityMetadataSourceRequestMapBuilderImpl
		implements FilterInvocationSecurityMetadataSourceRequestMapBuilder {

	@Autowired
	private ResouceService resouceService;

	@Override
	public LinkedHashMap<RequestMatcher, Collection<ConfigAttribute>> buildRequestMap() {

		LinkedHashMap<RequestMatcher, Collection<ConfigAttribute>> requestMap = new LinkedHashMap<>();

		AntPathRequestMatcher key = null;
		Collection<ConfigAttribute> val = null;

		for (Resource resource : resouceService.getAll()) {
			key = new AntPathRequestMatcher(resource.getUrl());

			val = new HashSet<>();

			for (Authority authority : resource.getAuthorities()) {
				val.add(new SecurityConfig(authority.getName()));
			}

			requestMap.put(key, val);
		}

		return requestMap;
	}

}

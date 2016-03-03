package cn.hlj.ems.security;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.security.web.access.intercept.DefaultFilterInvocationSecurityMetadataSource;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.stereotype.Component;

@Component
public class EmsSecurityBeanPostProcessor implements BeanPostProcessor {

	private FilterSecurityInterceptor filterSecurityInterceptor;

	private DefaultFilterInvocationSecurityMetadataSource metadataSource;

	private boolean isMetadataSourceSettedToFilterSecurityInterceptor = false;

	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {

		if (bean instanceof FilterSecurityInterceptor) {
			this.filterSecurityInterceptor = (FilterSecurityInterceptor) bean;
		}

		if ("filterInvocationSecurityMetadataSource".equals(beanName)) {
			this.metadataSource = (DefaultFilterInvocationSecurityMetadataSource) bean;
		}

		if (!isMetadataSourceSettedToFilterSecurityInterceptor && this.filterSecurityInterceptor != null
				&& this.metadataSource != null) {
			this.filterSecurityInterceptor.setSecurityMetadataSource(metadataSource);

			this.isMetadataSourceSettedToFilterSecurityInterceptor = true;
		}

		return bean;
	}

	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		return bean;
	}

}

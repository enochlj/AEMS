package cn.hlj.ems.interceptors;

import java.io.File;
import java.util.Map;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.Interceptor;

public class FileCleanerInterceptor implements Interceptor {

	private static final long serialVersionUID = 1L;

	@Override
	public void destroy() {

	}

	@Override
	public void init() {

	}

	@SuppressWarnings("unchecked")
	@Override
	public String intercept(ActionInvocation invocation) throws Exception {
		String result = invocation.invoke();

		// 需要在拦截器调用了 invocation.invoke()方法之后进行删除临时文件.
		// 因为在 invocation.invoke(); 之前, 目标方法还未执行
		Map<String, Object> request = (Map<String, Object>) invocation.getInvocationContext().get("request");
		String tempFileName = (String) request.get("tempFileName");

		if (tempFileName != null) {
			/*Thread.sleep(5000);*/

			File file = new File(tempFileName);
			if (file.exists()) {
				file.delete();
			}
		}

		return result;
	}

}

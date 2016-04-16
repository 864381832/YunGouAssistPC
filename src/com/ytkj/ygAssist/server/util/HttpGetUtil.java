package com.ytkj.ygAssist.server.util;

import java.awt.Desktop;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.util.Date;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.ytkj.ygAssist.tools.Config;

public class HttpGetUtil {
	private static CloseableHttpClient HttpClient = null;
	// private static PoolingHttpClientConnectionManager cm = null;

	public static String getHttpData(String url, String refererUrl, CloseableHttpClient closeableHttpClient) {
		String content = null;
		try {
			HttpGet httpGet = new HttpGet(url);
			httpGet.addHeader("Referer", refererUrl);
			HttpResponse httpResponse = closeableHttpClient.execute(httpGet);
			HttpEntity httpEntity = httpResponse.getEntity();
			if (httpEntity != null) {
				content = EntityUtils.toString(httpEntity);
			}
			httpGet.abort();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return content;
	}

	public static String getHttpData(String url, String refererUrl) {
		// if(cm == null){
		// cm = new PoolingHttpClientConnectionManager();
		// cm.setMaxTotal(500000);//整个连接池最大连接数
		// cm.setDefaultMaxPerRoute(500);//每路由最大连接数，默认值是2
		// }
		// CloseableHttpClient HttpClient =
		// HttpClients.custom().setConnectionManager(cm).build();
		if (HttpClient == null) {
			HttpClient = HttpClients.createDefault();
		}
		String content = null;
		try {
			// 创建 httpUriRequest 实例
			HttpGet httpGet = new HttpGet(url);
			// System.out.println("uri=" + httpGet.getURI());
			httpGet.addHeader("Referer", refererUrl);
			// 执行 get 请求
			HttpResponse httpResponse = HttpClient.execute(httpGet);
			// 获取响应实体
			HttpEntity httpEntity = httpResponse.getEntity();
			// 打印响应状态
			// System.out.println(httpResponse.getStatusLine());

			if (httpEntity != null) {
				// long length = httpEntity.getContentLength();// 响应内容的长度
				// 响应内容
				content = EntityUtils.toString(httpEntity);
				// System.out.println("Response content:" + content);
			}
			// 有些教程里没有下面这行
			httpGet.abort();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return content;
	}

	// 创建默认的 HttpClient 实例
	public static CloseableHttpClient createHttpClient() {
		return HttpClients.createDefault();
	}

	public static void CloseHttpClient() {
		try {
			HttpClient.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void CloseHttpClient(CloseableHttpClient closeableHttpClient) {
		try {
			closeableHttpClient.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void openBrowseURL(String url) {
		Desktop desktop = Desktop.getDesktop();
		try {
			desktop.browse(new URI(url));
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

	public static String getHttpUrl(String urlType) {
		Date time = new Date();
		String isLogin = Config.SystemAddressCode;
		String sig = Signature.getSignature(Config.userName, Config.userPassword, "" + time.getTime(), isLogin);
		StringBuffer getUrl = new StringBuffer();
		getUrl.append(Config.yunGouAssistUrl).append("/");
		getUrl.append(urlType);
		getUrl.append("?userid=").append(Config.userName);
		try {
			getUrl.append("&password=").append(URLEncoder.encode(Config.userPassword, "UTF-8"));
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		getUrl.append("&time=").append(time.getTime());
		getUrl.append("&isLogin=").append(isLogin);
		getUrl.append("&sig=").append(sig);
		return getUrl.toString();
	}

}

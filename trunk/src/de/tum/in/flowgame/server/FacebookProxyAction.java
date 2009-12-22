package de.tum.in.flowgame.server;

import java.util.Arrays;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.interceptor.ParameterAware;

import com.opensymphony.xwork2.ActionSupport;

/**
 * Proxies any request including all request parameters to the Facebook API
 * server, e.g. to make calls to the Facebook API from a sandboxed Java Applet.
 * <p>
 * Does currently support neither photo uploads nor HTTPS, i.e. calls to
 * "facebook.auth.getSession" and any request containing a
 * "generate_session_secret" parameter won't work.
 */
public class FacebookProxyAction extends ActionSupport implements ParameterAware {

	private final static String FACEBOOK_API_URL = "http://api.facebook.com/restserver.php";

	private final static Log log = LogFactory.getLog(FacebookProxyAction.class);

	private final static HttpClient client = new HttpClient(new MultiThreadedHttpConnectionManager());

	private Map<String, String[]> params;

	private String response;

	@Override
	public String execute() throws Exception {
		final PostMethod post = new PostMethod(FACEBOOK_API_URL);

		// Facebook requires this content type for POSTs
		post.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");

		// TODO filter by API key to avoid other people using our server

		for (final Entry<String, String[]> param : params.entrySet()) {
			post.addParameter(param.getKey(), param.getValue()[0]);
		}

		try {
			if (client.executeMethod(post) == HttpStatus.SC_OK) {
				if (log.isDebugEnabled()) {
					log.debug(post.getStatusLine() + ": " + Arrays.toString(post.getParameters()));
				}
			} else {
				if (log.isWarnEnabled()) {
					log.warn(post.getStatusLine() + ": " + Arrays.toString(post.getParameters()));
				}
			}

			this.response = IOUtils.toString(post.getResponseBodyAsStream(), "UTF-8");
		} finally {
			post.releaseConnection();
		}

		return SUCCESS;
	}

	public void setParameters(final Map<String, String[]> params) {
		this.params = params;
	}

	public String getResponse() {
		return response;
	}
}

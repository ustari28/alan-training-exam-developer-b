/**
 * 
 */
package com.alan.training.servlets;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.appidentity.AppIdentityService;
import com.google.appengine.api.appidentity.AppIdentityService.SigningResult;
import com.google.appengine.api.appidentity.AppIdentityServiceFactory;
import com.google.appengine.api.urlfetch.HTTPMethod;
import com.google.appengine.api.urlfetch.HTTPRequest;
import com.google.appengine.api.urlfetch.HTTPResponse;
import com.google.appengine.api.urlfetch.URLFetchService;
import com.google.appengine.api.urlfetch.URLFetchServiceFactory;
import com.google.apphosting.api.ApiProxy;
import com.google.apphosting.api.ApiProxy.Environment;

/**
 * @author alangabriel
 * 
 */
public class DemoServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6150775745760607022L;
	/**
	 * Logger.
	 */
	private static final Logger LOG = Logger.getLogger(DemoServlet.class
	        .getSimpleName());

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
	        throws ServletException, IOException {
		LOG.info("inicio");
		List<String> scopes = new ArrayList<String>();
		scopes.add("https://alan-training-exam-developer.appspot.com/");
		AppIdentityService appIdentity = AppIdentityServiceFactory
		        .getAppIdentityService();
		String sFirma = "lola";
		SigningResult firma = appIdentity.signForApp(sFirma.getBytes());
		LOG.info("key name:" + firma.getKeyName());
		LOG.info("key:" + new String(firma.getSignature()));

		Environment env = ApiProxy.getCurrentEnvironment();
		Enumeration e = req.getHeaderNames();
		Enumeration atributos = req.getAttributeNames();
		LOG.info("headers");
		while (e.hasMoreElements()) {
			String headerName = String.valueOf(e.nextElement());
			LOG.info(headerName + ":" + req.getHeader(headerName));
		}
		LOG.info("attrs");
		while (atributos.hasMoreElements()) {
			String atributoName = String.valueOf(atributos.nextElement());
			LOG.info(atributoName + ":" + req.getAttribute(atributoName));
		}
		URLFetchService fetchService = URLFetchServiceFactory
		        .getURLFetchService();
		HTTPRequest request = new HTTPRequest(new URL(
		        "https://alan-training-exam-developer.appspot.com/testsign"),
		        HTTPMethod.POST);
		HTTPResponse response = fetchService.fetch(request);
		LOG.info("RESP:" + new String(response.getContent()));
		resp.setContentType("application/json");
		resp.getWriter()
		        .write("{\"id\":\""
		                + env.getAppId()
		                + ","
		                + env.getAttributes()
		                        .get("com.google.appengine.runtime.default_version_hostname")
		                + "\"}");
	}
}

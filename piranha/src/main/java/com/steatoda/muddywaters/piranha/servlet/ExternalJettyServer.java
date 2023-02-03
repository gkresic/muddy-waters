package com.steatoda.muddywaters.piranha.servlet;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ro.pippo.core.AbstractWebServer;
import ro.pippo.core.Application;
import ro.pippo.core.HttpConstants;
import ro.pippo.core.PippoRuntimeException;
import ro.pippo.core.PippoServletContextListener;
import ro.pippo.core.WebServerSettings;
import ro.pippo.jetty.JettyServer;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.DispatcherType;
import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.EnumSet;

// based on ro.pippo.jetty.JettyServer (from ro.pippo:pippo-jetty)
// https://github.com/pippo-java/pippo/blob/master/pippo-server-parent/pippo-jetty/src/main/java/ro/pippo/jetty/JettyServer.java
@Singleton
public class ExternalJettyServer extends AbstractWebServer<WebServerSettings> {

	@Inject
	public ExternalJettyServer(Server server, Application pippoApplication) {

		init(pippoApplication);

		setPippoFilterPath("/rest/*");

		Handler pippoHandler = createPippoHandler();
		server.setHandler(pippoHandler);

	}

	@Override
	public void start() {
		// ignore
	}

	@Override
	public void stop() {
		// ignore
	}

	@Override
	protected WebServerSettings createDefaultSettings() {
		return new WebServerSettings(getApplication().getPippoSettings());
	}

	protected ServletContextHandler createPippoHandler() {

		MultipartConfigElement multipartConfig = createMultipartConfigElement();
		ServletContextHandler handler = new PippoHandler(ServletContextHandler.SESSIONS, multipartConfig);
		handler.setContextPath(getSettings().getContextPath());

		// inject application as context attribute
		handler.setAttribute(PIPPO_APPLICATION, getApplication());

		// add pippo filter
		addPippoFilter(handler);

		// add initializers
		handler.addEventListener(new PippoServletContextListener());

		// all listeners
		listeners.forEach(listener -> {
			try {
				handler.addEventListener(listener.getDeclaredConstructor().newInstance());
			} catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
				throw new PippoRuntimeException(e);
			}
		});

		return handler;

	}

	private void addPippoFilter(ServletContextHandler handler) {

		if (pippoFilterPath == null)
			pippoFilterPath = "/*"; // default value

		EnumSet<DispatcherType> dispatches = EnumSet.of(DispatcherType.REQUEST, DispatcherType.ERROR);

		FilterHolder pippoFilterHolder = new FilterHolder(getPippoFilter());
		handler.addFilter(pippoFilterHolder, pippoFilterPath, dispatches);

		Log.debug("Using pippo filter for path '{}'", pippoFilterPath);

	}

	/**
	 * Inject a {@code MultipartConfig} in a filter.
	 */
	private static class PippoHandler extends ServletContextHandler {

		private PippoHandler(int options, MultipartConfigElement multipartConfig) {
			super(options);
			this.multipartConfig = multipartConfig;
		}

		@Override
		public void doHandle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

			if (isMultipartRequest(request))
				baseRequest.setAttribute(Request.__MULTIPART_CONFIG_ELEMENT, multipartConfig);

			super.doHandle(target, baseRequest, request, response);

		}

		private boolean isMultipartRequest(HttpServletRequest request) {
			//noinspection PointlessBooleanExpression
			return true
				&& HttpConstants.Method.POST.equalsIgnoreCase(request.getMethod())
				&& request.getContentType() != null
				&& request.getContentType().toLowerCase().startsWith(HttpConstants.ContentType.MULTIPART_FORM_DATA);
		}

		private final MultipartConfigElement multipartConfig;

	}

	private static final Logger Log = LoggerFactory.getLogger(JettyServer.class);

}

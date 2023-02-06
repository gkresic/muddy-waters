package com.steatoda.muddywaters.piranha.servlet.rest;

import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.webapp.WebAppContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ro.pippo.core.Application;
import ro.pippo.core.PippoFilter;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.DispatcherType;
import java.util.EnumSet;

/**
 * <p>Wraps Pippo {@link Application} and configures it into {@link WebAppContext}.</p>
 *
 * <p>NOTE: Initialized {@link PippoFilter} destroys {@link Application}. If this is not desires, override
 * {@link PippoFilter#destroy()} and manage {@link Application}'s lifecycle in this class</p>
 */
@Singleton
public class PippoService {

	@Inject
	public PippoService(
		WebAppContext webAppContext,
		Application pippoApplication
	) {

		PippoFilter pippoFilter = new PippoFilter();
		pippoFilter.setApplication(pippoApplication);

		webAppContext.addFilter(new FilterHolder(pippoFilter), "/rest/*", EnumSet.of(DispatcherType.REQUEST, DispatcherType.ERROR));

	}

	@SuppressWarnings("unused")
	private static final Logger Log = LoggerFactory.getLogger(PippoService.class);

}

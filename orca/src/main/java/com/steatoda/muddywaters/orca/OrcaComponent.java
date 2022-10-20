package com.steatoda.muddywaters.orca;

import com.steatoda.muddywaters.orca.spark.SparkModule;
import dagger.Component;

import javax.inject.Singleton;

@Singleton
@Component(modules = {
	OrcaModule.class,
	SparkModule.class,
})
public interface OrcaComponent {

	@Singleton
	Orca orca();

}

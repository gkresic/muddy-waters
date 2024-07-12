package com.steatoda.muddywaters.octopus.servlet;

import graphql.kickstart.servlet.GraphQLConfiguration;
import graphql.kickstart.servlet.GraphQLHttpServlet;
import graphql.schema.GraphQLSchema;
import graphql.schema.StaticDataFetcher;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class OctopusGraphQLHttpServlet extends GraphQLHttpServlet {

	public OctopusGraphQLHttpServlet() {

		String schemaStr;
		try (InputStream istream = Thread.currentThread().getContextClassLoader().getResourceAsStream("com/steatoda/muddywaters/octopus/schema.graphqls")) {
			if (istream == null)
				throw new FileNotFoundException("GFraphQL schema definition (schema.graphqls) not found on classpath");
			schemaStr = new String(istream.readAllBytes(), StandardCharsets.UTF_8);
		} catch (IOException e) {
			throw new RuntimeException("Could not load GraphQL schema", e);
		}

		SchemaParser schemaParser = new SchemaParser();
		TypeDefinitionRegistry typeDefinitionRegistry = schemaParser.parse(schemaStr);

		RuntimeWiring runtimeWiring = RuntimeWiring.newRuntimeWiring()
			.type("Query", builder -> builder
				.dataFetcher("hello", new StaticDataFetcher("world"))
				.dataFetcher("eat", new EatDataFetcher())
			)
			.build();

		SchemaGenerator schemaGenerator = new SchemaGenerator();
		GraphQLSchema schema = schemaGenerator.makeExecutableSchema(typeDefinitionRegistry, runtimeWiring);
		configuration = GraphQLConfiguration.with(schema).build();
	}

	@Override
	protected GraphQLConfiguration getConfiguration() {
		return configuration;
	}

	private final GraphQLConfiguration configuration;
}

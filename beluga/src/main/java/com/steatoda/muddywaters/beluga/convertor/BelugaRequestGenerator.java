package com.steatoda.muddywaters.beluga.convertor;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.steatoda.muddywaters.beluga.Payload;
import com.steatoda.muddywaters.beluga.proto.PayloadRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

public class BelugaRequestGenerator {

	public static void main(String[] args) {

		String[] baseNames = new String []{
			"payload-10",
			"payload-100"
		};

		for (String baseName : baseNames) {
			try {
				Path messagePath = convert(baseName);
				print(messagePath);
			} catch (IOException e) {
				Log.error("Error converting {}", baseName, e);
			}
		}

	}

	private static Path convert(String baseName) throws IOException {

		Log.info("Converting {}", baseName);

		Path jsonPath = Paths.get(baseName + ".json");

		if (!Files.exists(jsonPath))
			throw new IOException("JSON file " + jsonPath.toAbsolutePath() + " doesn't exist!");

		Path messagePath = Paths.get("beluga", baseName + ".proto.message");

		if (Files.exists(messagePath)) {
			Log.info("Proto message file {} already exists, deleting...", messagePath);
			try {
				Files.delete(messagePath);
			} catch (IOException e) {
				throw new IOException("Unable to delete proto message file " + messagePath, e);
			}
		}

		JsonMapper jsonMapper = JsonMapper.builder()
			.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true)
			.build();

		Payload[] payloads;
		try (JsonParser jsonParser = jsonMapper.createParser(jsonPath.toFile())) {
			payloads = jsonParser.readValueAs(Payload[].class);
		} catch (IOException e) {
			throw new IOException("Unable to parse input JSON from " + jsonPath, e);
		}

		PayloadRequest request = PayloadRequest.newBuilder()
			.addAllPayloads(Arrays.stream(payloads)
				.map(belugaPayload -> com.steatoda.muddywaters.beluga.proto.Payload.newBuilder()
					.setText(belugaPayload.text)
					.setNumber(belugaPayload.number)
					.build()
				).toList()
			)
			.build();
		try (OutputStream ostream = Files.newOutputStream(messagePath)) {
			request.writeTo(ostream);
		} catch (IOException e) {
			throw new IOException("Unable to write output message to " + messagePath, e);
		}

		Log.info("Converted {} -> {} ({} items)", jsonPath, messagePath, payloads.length);

		return messagePath;

	}

	private static void print(Path messagePath) throws IOException {

		Log.info("Printing content of {}", messagePath.toAbsolutePath());

		if (!Files.exists(messagePath))
			throw new IOException("Proto message file " + messagePath.toAbsolutePath() + " doesn't exist!");

		PayloadRequest request;
		try (InputStream istream = Files.newInputStream(messagePath)) {
			request = PayloadRequest.parseFrom(istream);
		} catch (IOException e) {
			throw new IOException("Unable to read input message from " + messagePath, e);
		}

		Log.info("Message contains {} payloads", request.getPayloadsCount());
		for (int index = 0; index < request.getPayloadsCount(); ++index)
			Log.info("text={}; number={}", request.getPayloads(index).getText(), request.getPayloads(index).getNumber());

	}

	private static final Logger Log = LoggerFactory.getLogger(BelugaRequestGenerator.class);

}

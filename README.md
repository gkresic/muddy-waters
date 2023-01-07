# Muddy waters

Java REST benchmarks.

Minimum requirements: Java 17 on path (or set `JAVA_HOME`)

Build: `./gradlew build`

Test max RSS with: `/usr/bin/time -v <app-binary>`

## Shark

Optimized for speed, built using [Rapidoid](https://github.com/rapidoid/rapidoid) + [DSL-JSON](https://github.com/ngs-doo/dsl-json).

Run: `shark/build/install/shark/bin/shark`

Test & benchmark:

```
curl -v -H "Accept: application/json" -H "Content-Type: application/json" --data @payload-10.json "http://localhost:16001/eat"
wrk -t4 -c400 -d10s -s payload-10.lua http://localhost:16001/eat
wrk -t4 -c400 -d10s -s payload-100.lua http://localhost:16001/eat
```

You think you have something faster? *"Talk is cheap, show me the code."* - Linus Torvalds.

#### FatJar version

Build: `./gradlew shark:fatJar`

Run: `$JAVA_HOME/bin/java -jar shark/build/libs/shark-bundle-1.0.0.jar`

#### jlink version

Build: `./gradlew :shark:runtime`

Run: `shark/build/image/bin/shark`

## Whale

Hand-crafted, spring-boot-style REST server with all the bells and whistles (dependency injection, routing, convertors, ...).
Build using [Guice](https://github.com/google/guice) + [Jetty](https://github.com/eclipse/jetty.project) +
[RESTEasy](https://github.com/resteasy/Resteasy) + [Jackson](https://github.com/FasterXML/jackson).

Run:

```
cd whale/build/install/whale/
bin/whale
```

Test & benchmark:

```
curl -v -H "Accept: application/json" -H "Content-Type: application/json" --data @payload-10.json "http://localhost:16002/api/eat"
wrk -t4 -c400 -d10s -s payload-10.lua http://localhost:16002/api/eat
wrk -t4 -c400 -d10s -s payload-100.lua http://localhost:16002/api/eat
```

## Megalodon

Vanilla [Spring Boot](https://spring.io/projects/spring-boot) implementation.

Run:

```
cd megalodon/build/install/megalodon/
bin/megalodon
```

Test & benchmark:

```
curl -v -H "Accept: application/json" -H "Content-Type: application/json" --data @payload-10.json "http://localhost:16003/eat"
wrk -t4 -c400 -d10s -s payload-10.lua http://localhost:16003/eat
wrk -t4 -c400 -d10s -s payload-100.lua http://localhost:16003/eat
```

#### jlink version

Build: `./gradlew :megalodon:runtime`

Run: `megalodon/build/image/bin/megalodon`

#### Native version

Native version requires [GraalVM](https://www.graalvm.org/).
Detailed guide on how to build native image is available [here](https://docs.spring.io/spring-boot/docs/current/reference/html/native-image.html).

Build: `./gradlew :megalodon:nativeCompile`

Run: `megalodon/build/native/nativeCompile/megalodon`

## Swordfish

Vanilla [Quarkus](https://quarkus.io/) implementation.

Run:

```
cd swordfish/build/quarkus-app/
java -jar quarkus-run.jar
```

Test & benchmark:

```
curl -v -H "Accept: application/json" -H "Content-Type: application/json" --data @payload-10.json "http://localhost:16004/eat"
wrk -t4 -c400 -d10s -s payload-10.lua http://localhost:16004/eat
wrk -t4 -c400 -d10s -s payload-100.lua http://localhost:16004/eat
```

#### jlink version

Build: `./gradlew :swordfish:build :swordfish:runtime`

Run: `swordfish/build/jre/bin/java -jar swordfish/build/quarkus-app/quarkus-run.jar`

#### Native version

Native version requires [GraalVM](https://www.graalvm.org/) or [Mandrel](https://github.com/graalvm/mandrel).
Detailed guide on how to build native image is available [here](https://quarkus.io/guides/building-native-image).

Build: `./gradlew :swordfish:build -Dquarkus.package.type=native`

Run: `swordfish/build/swordfish-1.0.0-runner`

## Sailfish

Vanilla [Micronaut](https://micronaut.io/) implementation.

Run:

```
cd sailfish/build/install/sailfish/
bin/sailfish
```

Test & benchmark:

```
curl -v -H "Accept: application/json" -H "Content-Type: application/json" --data @payload-10.json "http://localhost:16005/eat"
wrk -t4 -c400 -d10s -s payload-10.lua http://localhost:16005/eat
wrk -t4 -c400 -d10s -s payload-100.lua http://localhost:16005/eat
```

#### jlink version

Build: `./gradlew :sailfish:runtime`

Run: `sailfish/build/image/bin/sailfish`

#### Native version

Native version requires [GraalVM](https://www.graalvm.org/).
Detailed guide on how to build native image is available [here](https://guides.micronaut.io/latest/micronaut-creating-first-graal-app-gradle-java.html).

Build: `./gradlew :sailfish:nativeCompile`

Run: `sailfish/build/native/nativeCompile/sailfish`

## Dolphin

Hand-crafted REST server with most of the bells and whistles.
Build using [Dagger](https://dagger.dev/) + [Vert.x](https://vertx.io/) + [Jackson](https://github.com/FasterXML/jackson).

Run: `dolphin/build/install/dolphin/bin/dolphin`

Test & benchmark:

```
curl -v -H "Accept: application/json" -H "Content-Type: application/json" --data @payload-10.json "http://localhost:16006/eat"
wrk -t4 -c400 -d10s -s payload-10.lua http://localhost:16006/eat
wrk -t4 -c400 -d10s -s payload-100.lua http://localhost:16006/eat
```

#### jlink version

Build: `./gradlew :dolphin:runtime`

Run: `dolphin/build/image/bin/dolphin`

## Orca

Hand-crafted REST server with most of the bells and whistles.
Build using [Dagger](https://dagger.dev/) + [Spark](https://sparkjava.com/) + [Jackson](https://github.com/FasterXML/jackson).

Run:

```
cd orca/build/install/orca/
bin/orca
```

Test & benchmark:

```
curl -v -H "Accept: application/json" -H "Content-Type: application/json" --data @payload-10.json "http://localhost:16007/eat"
wrk -t4 -c400 -d10s -s payload-10.lua http://localhost:16007/eat
wrk -t4 -c400 -d10s -s payload-100.lua http://localhost:16007/eat
```

## Beluga

Hand-crafted REST server with most of the bells and whistles.
Build using [Dagger](https://dagger.dev/) + [Javalin](https://sparkjava.com/) + [Jackson](https://github.com/FasterXML/jackson).

Run: `beluga/build/install/beluga/bin/beluga`

Test & benchmark:

```
curl -v -H "Accept: application/json" -H "Content-Type: application/json" --data @payload-10.json "http://localhost:16008/eat"
wrk -t4 -c400 -d10s -s payload-10.lua http://localhost:16008/eat
wrk -t4 -c400 -d10s -s payload-100.lua http://localhost:16008/eat
```

#### jlink version

Build: `./gradlew :beluga:runtime`

Run: `beluga/build/image/bin/beluga`

Protobuf payload:

```
curl -v -H "Accept: application/json" -H "Content-Type: application/protobuf" --data-binary @beluga/payload-10.proto.message "http://localhost:16008/eatProtobuf"
wrk -t4 -c400 -d10s -s beluga/payload-10.lua http://localhost:16008/eatProtobuf
wrk -t4 -c400 -d10s -s beluga/payload-100.lua http://localhost:16008/eatProtobuf
```

## Kaluga

Hand-crafted REST server with most of the bells and whistles.
Build using [Dagger](https://dagger.dev/) + [Helidon](https://helidon.io/) + [Jackson](https://github.com/FasterXML/jackson).

Run:

```
cd kaluga/build/install/kaluga/
bin/kaluga
```

Test & benchmark:

```
curl -v -H "Accept: application/json" -H "Content-Type: application/json" --data @payload-10.json "http://localhost:16009/eat"
wrk -t4 -c400 -d10s -s payload-10.lua http://localhost:16009/eat
wrk -t4 -c400 -d10s -s payload-100.lua http://localhost:16009/eat
```

# Out of competition

## Plankton

gRPC server.
Build using [gRPC](https://grpc.io/) + [Protocol buffers](https://developers.google.com/protocol-buffers/).

Run:

```
cd plankton/build/install/plankton/
bin/plankton
```

Test & benchmark:

```
./gradlew :plankton:benchmark
```

Using [ghz](https://ghz.sh/):

```
ghz --insecure --proto=plankton/src/main/proto/ping.proto --call=muddywaters.plankton.PingService/Ping --duration=10s --duration-stop=wait localhost:17001
ghz --insecure --proto=plankton/src/main/proto/payload.proto --call=muddywaters.plankton.EatService/EatOne --duration=10s --duration-stop=wait --data='{"text":"foo","number":42}' localhost:17001
ghz --insecure --proto=plankton/src/main/proto/payload.proto --call=muddywaters.plankton.EatService/EatStream --duration=10s --duration-stop=wait --data-file=payload-10.json localhost:17001
```

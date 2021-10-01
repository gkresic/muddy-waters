# Muddy waters

Java REST benchmarks.

Minimum requirements: Java 11 on path (or set `JAVA_HOME`)

Build: `./gradlew build`

## Shark

Optimized for speed, built using [Rapidoid](https://github.com/rapidoid/rapidoid) + [DSL-JSON](https://github.com/ngs-doo/dsl-json).

Run:

```
cd shark/build/install/shark/
bin/shark
```

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

## Swordfish

Vanilla [Quarkus](https://quarkus.io/) implementation.

Run:

```
cd swordfish/build/quarkus-app/
$JAVA_HOME/bin/java -jar quarkus-run.jar
```

Test & benchmark:

```
curl -v -H "Accept: application/json" -H "Content-Type: application/json" --data @payload-10.json "http://localhost:16004/eat"
wrk -t4 -c400 -d10s -s payload-10.lua http://localhost:16004/eat
wrk -t4 -c400 -d10s -s payload-100.lua http://localhost:16004/eat
```

#### Native version

Native version requires [GraalVM](https://www.graalvm.org/) or [Mandrel](https://github.com/graalvm/mandrel).

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

#### Native version

Native version requires [GraalVM](https://www.graalvm.org/).

Build: `./gradlew :sailfish:nativeImage`

Run: `sailfish/build/native-image/application`


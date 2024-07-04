## Authentication Service

Tiny demo service for authenticating multiple microservices

## Build and run

The app can be built in three variants: regular kotlin app, native runtime image and docker image (based on regular and native run time)

- Java build and run
```
./gradlew clean build

./gradlew run
```

- Native build and run
```
./gradlew nativeCompile

./gradlew nativeRun
```

- Docker build
```
./gradlew dockerBuild 
```

- Docker build native
```
./gradlew dockerBuildNative
```

- Docker jib build
```
./gradlew jib
```
Set image name and docker registry credentials in gradle.properties. Make sure that build arch matches docker base image arch

- Run a docker image

```
`docker run --env-file ./dev.env -d --name todo  -p 8080:8080 abondar/authserver:1.0
```
Note: update dev.env file with your mysql host and credentials

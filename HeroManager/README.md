# HeroManager

Tiny API to Manage superheroes.

## Build test and run

### Build

- Build the app using gradle wrapper
```
./gradlew clean build
```
- Build docker image
```
./gradlew jibDockerBuild
```
- Build jib image and push to registry

```
./gradlew jib
```
Note: requires credentials for registry set via env variables + tag requires registry id

### Test
- Run unit tests
```
./gradlew clean test
```
- Run integration tests
```
./gradlew clean integrationTest
```
### Run
- Run the app using gradlew

```
./gradlew bootRun -PprofileName
```
- Run the app via jar

```
java -jar -Dspring.profiles.active=profileName build/libs/HeroManager-1.0-SNAPSHOT.jar
```
- Run a docker image

```
`docker run --env-file ./dev.env -d --name todo  -p 8080:8080 abondar/heromanager:1.0-SNAPSHOT.jar
```
Note: update dev.env file with your mysql host and credentials

- Docker compose run
```
docker-compose up
```

## Assumptions
- We limit size of powers,weapons and associations to 10 due to performance concerns
- Update for properties means adding of new properties. If property needs to be changed it should be deleted and new value should be added.
- Authentication and authorization were not added due to not clear requirements and assumption that there is a separate service responsible for it. 
This microservice can send incoming jwt token in authorization service
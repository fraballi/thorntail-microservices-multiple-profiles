# THORNTAIL / UNDERTOW MICROSERVICES

## A demo project on Thorntail microservices using multiple profiles with Docker

### _Profiles_: **dev, prod**

---

## Configuration files (_stages_)

- **project-defaults.yml** (base)
- **project-dev.yml**  (dev profile config)
- **project-prod.yml** (prod profile config)

```ssh
    # Can switch among profiles setting:
    # Stage = profile, e.g 'dev' or 'prod'
    java -Dthorntail.project.stage=<stage> ... -jar <JAR_NAME.jar>

    #So would be:
    java -Dthorntail.project.stage=prod ... -jar <JAR_NAME.jar>

    #Or stage file can be referenced by an URL:
    java -Dthorntail.project.stage.file=<URL> ... -jar <JAR_NAME.jar>
```

## **Docker Resources**

### _docker-compose.yml_

```ym
    # Can pass ENV variables to the container to change default behaviour:
    environment:
      - THORNTAIL_PROJECT_STAGE=prod #If you want to set stage from ENV var, then a file called: 'project-prod.yml' should exist in /src/main/resources for after-package phase build

      - SWARM_HTTP_PORT=19090
      - THORNTAIL_BIND_ADDRESS=127.0.0.1
```

### _io.fabric8:docker-maven-plugin_ requires: **assembly.xml**

```sh
    target/assembly.xml
```

#### Docker build

```sh
    #Build with Dockerfile, e.g located at '.' directory:
    docker build -t thorntail_micsrv:1.0 .  \
    && docker run -p 19090:19090 --name thorntail_micsrv:1.0

    #Build with docker-compose.yml:
    docker-compose up
    #e.g If Dockerfile were in a location other than '.', add '-f <path-to-Dockerfile>' like:
    docker-compose -f /path/to/Dockerfile up

    # Builds with Maven:
    mvn clean install -Pdocker # e.g Image name = ${project.artifactId}:${project.version}
```

#### Maven build

```xml
<!-- Check children: -->
<!-- //////////////////////////// -->
<!--  <thorntail.bind.address>    -->
<!--  <java.net.preferIPv4Stack>  -->
<!-- //////////////////////////// -->

<build>
  <plugins>
    <plugin>
      <groupId>io.thorntail</groupId>
      <artifactId>thorntail-maven-plugin</artifactId>
      <version>2.2.1.Final</version>
      <configuration>
        <properties>
          <thorntail.bind.address>127.0.0.1</thorntail.bind.address>
          <java.net.preferIPv4Stack>true</java.net.preferIPv4Stack>
        </properties>
      </configuration>
    </plugin>
  </plugins>
</build>
```

#### Run

```sh
    #Starts application (interactively):
    mvn thorntail:run

    #Starts from a different port, e.g '19090':
    java -jar -Dswarm.http.port=19090 <JAR_NAME.jar>

    #Starts from a different address, e.g '127.0.0.1':
    java -jar -Dthorntail.bind.address=127.0.0.1 <JAR_NAME.jar>
```

#### Debug

```sh
   java -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=9090 \
   -jar target/<JAR_NAME.jar>
```

#### Dependencies

| **Name**                         | **Version**                |
| -------------------------------- | -------------------------- |
| Docker _Image:Tag_               | _openjdk:**8-jre-alpine**_ |
| io.thorntail                     | 2.6.0.Final                |
| io.thorntail:jaxrs               | 2.6.0.Final                |
| io.thorntail:cdi                 | 2.6.0.Final                |
| io.thorntail:ejb                 | 2.6.0.Final                |
| io.thorntail:microprofile-health | 2.6.0.Final                |
| snakeyaml                        | 1.26                       |
| log4j                            | 2.13.1                     |
| lombok                           | 1.18.12                    |

#### Maven plugins

| **Name**                             | **Version** |
| ------------------------------------ | ----------- |
| io.thorntail :thorntail-maven-plugin | 2.6.0.Final |
| maven-war-plugin                     | 3.2.3       |
| io.fabric8:docker-maven-plugin       | 0.33.0      |

#### Endpoints

| **Profile**         | **URL**                                  |
| ------------------- | ---------------------------------------- |
| PROD                | http://localhost:19090/message           |
| DEV                 | http://localhost:19091/message           |
| MICROPROFILE/HEALTH | **(prod)** http://localhost:19090/health |
|                     | **(dev)** http://localhost:19091/health  |
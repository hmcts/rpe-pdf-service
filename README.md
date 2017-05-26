# CMC PDF service

Allows to generate PDFs based on given template and placeholder data

## Getting started

### Prerequisites

- [JDK 8](https://www.oracle.com/java)
- [Docker](https://www.docker.com)

### Building

Project uses [Gradle](https://gradle.org) as a build tool but you don't have install it locally since project comes with `./gradlew` wrapper script.   

To build project please execute following command:

```bash
    ./gradlew build
```

### Running

First you need to create distribution by executing following command:

```bash
    ./gradlew installDist
```

When distribution has been created in `build/install/pdf-service` directory, you can run application by execute following command:

```bash
    docker-compose up
```

As a result the following container(s) will get created and started:
 - long living container for API application exposing port `5500`

### API documentation

API documentation is provided in form of following Swagger endpoints:

 - `http://localhost:5500/v2/api-docs` - resource definitions in JSON format
 - `http://localhost:5500/swagger-ui.html` - UI to interact with the API resources

NOTE: Swagger scans classes in the `uk.gov.hmcts.reform.cmc.pdfservice.controllers` package.  

## Developing

### Unit tests

To run all unit tests please execute following command:

```bash
    ./gradlew test
```

### Coding style tests

To run all checks (including unit tests) please execute following command:

```bash
    ./gradlew check
```

## Versioning

We use [SemVer](http://semver.org/) for versioning.
For the versions available, see the tags on this repository.

## License

This project is licensed under the MIT License - see the [LICENSE.txt](LICENSE.txt) file for details.



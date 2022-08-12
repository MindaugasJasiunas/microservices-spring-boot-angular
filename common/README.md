# common - Maven project

### It will be used to contain repeating POJO's used in microservices.

`<packaging>jar</packaging>`

## Usage

### Compile & copy to local maven repository
Run `mvn clean install -DskipTests`

### Use in other services by adding dependency:
```
<dependency>
    <groupId>org.example</groupId>
    <artifactId>common</artifactId>
    <version>5.0-SNAPSHOT</version>
</dependency>
```

### Change in downstream services
- delete event, command POJO's & reimport
# GameAPI

An API for Nimelia's game

#### How to add it to your projects
- Download the jar from the github actions
- Make a folder called `libs` and put the jar in it.
- Add those lines to your build manager.

Using maven:
```xml
<dependency>
    <groupId>fr.nimelia</groupId>
    <artifactId>gameapi</artifactId>
    <version>1.0</version>
    <scope>system</scope>
    <systemPath>${project.basedir}/libs/GameAPI.jar</systemPath>
</dependency>
```

Using gradle:
```groovy
implementation files('libs/GameAPI.jar')
```

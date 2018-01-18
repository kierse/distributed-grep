# README #

Attention: for Windows users, replace `gradlew` with `gradlew.bat`

##### Delete artifacts #####

```
 > ./gradlew clean 
```

##### Build archive (jar) #####
Note: this put the jar in build/libs

```
 > ./gradlew jar
```

#### Run Client ####

```
 > java -cp build/libs/distributed-grep-1.0-SNAPSHOT.jar eece513.client.GrepClient
```

#### Run Server ####
```
 > java -cp build/libs/distributed-grep-1.0-SNAPSHOT.jar eece513.server.GrepServer
```

## Upgrade from Java7 to Java8 ##
...
 > sudo yum install java-1.8.0
 > sudo yum remove java-1.7.0-openjdk
...
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

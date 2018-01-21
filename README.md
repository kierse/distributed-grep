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

### Upgrade from Java7 to Java8 ###
```
 > sudo yum install java-1.8.0
 > sudo yum remove java-1.7.0-openjdk
```

### Creating a nNw EC2 Instances ###
```
On the EC2 Dashboard, select "Launch Instance"
Create a instance of type "Amazon Linux AMI 2017.09.1 (HVM), SSD Volume Type"
Leave the defaults as is by selecting "Review and Launch" > "Launch" at the bottom right of the screen
In the following popup, select "Create a new key pair" from the first dropdown
Give it a name and select "Download" to get your private key
Finally, select "Launch Instance"
```

### Creating a Similar EC2 Instance###
```
Under INSTANCES > instances on the left sidebar, select the instance you want to clone
Select "Actions > Launch More Like This"
Note: for ease of use, use the same security group
```

### Setting Up Security Groups for EC2 Instances ###
```
Select the security group associated with your instances e.g. launch-wizard-1
In the split window on the bottom, select the Inbound tab
Select "Edit"
Then on the popup window, ensure you have 2 rules:
> Type: All traffic, Destination: 0.0.0.0/0
> Type: SSH, Destination: 0.0.0.0/0
If one rule is missing, select "Add Rule" to generate a new line
```
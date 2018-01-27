# README #

Attention: for Windows users, replace `gradlew` with `gradlew.bat`

## How to Use ##
1. Set up EC2 instance and pull from master
2. Update instances to Java 8 if required
3. Create a "Server.txt" file in the project root with Public DNS addresses of each EC2 instance
    * Note: this implementation currently only accommodates up to 6 instances
4. Run the following command to build, deploy, and run distributed tests
    ```
     > ./gradlew bootstrapAws distributedTest -Ppem=/Users/kierse/.ssh/eece513_aws.pem
    ```
5. In any EC2 instance home directory, use the _startClient_ command as you would call a _grep_ command
```
> ./startClient [OPTION]
```

## Auxiliary Commands ##

### Delete Artifacts ###
```
 > ./gradlew clean 
```

### Build Archive (.jar) ###

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

#### Runs Distributed Tests ####
```
 > ./gradlew distributedTest
```

#### Runs Unit Tests ####
```
 > ./gradlew test
```

#### Upgrade from Java7 to Java8 ####
```
 > sudo yum install java-1.8.0
 > sudo yum remove java-1.7.0-openjdk
```

#### Creating EC2 Instances ####
1. On the EC2 Dashboard, select **Launch Instance**
2. Create a instance of type **Amazon Linux AMI 2017.09.1 (HVM), SSD Volume Type**
3. Leave the defaults as is by selecting **Review and Launch > Launch** at the bottom right of the screen
4. In the following popup, select **Create a new key pair** from the first dropdown
5. Give it a name and select **Download** to get your private key
6. Finally, select **Launch Instance**

#### Creating a Similar EC2 Instance ####
1. Under **INSTANCES > instances** on the left sidebar, select the instance you want to clone
2. Select **Actions > Launch More Like This** 
    * _Note: for ease of use, use the same security group_

#### Setting Up Security Groups for EC2 Instances ####
1. Select the security group associated with your instances e.g. launch-wizard-1
2. In the split window on the bottom, select the **Inbound** tab
3. Select **Edit**
4. Then on the popup window, ensure you have 2 rules:
    ```
    > Type: All traffic, Destination: 0.0.0.0/0
    > Type: SSH, Destination: 0.0.0.0/0
    ```
    * If one rule is missing, select **Add Rule** to generate a new line
5. Repeat for the **Outbound** tab
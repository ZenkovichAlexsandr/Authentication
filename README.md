# Authentication API

RESTful API which authenticates a customer based on his username and password.
The Authentication API exposed via TLS (8084) using mutual authentication.

There are 2 endpoints:
1. one endpoint for creating an online banking account. (A customer can create an online account only if his bank account exists in the Accounts API)
2. one endpoint for authentication. 

### Constraints
if a customer introduces a wrong password for 3 consecutive times, then his account will be blocked for 24 hours.

### Pre-requisites

Following tools and packages should be installed in first place:

| Tool              | How to install           | Version  |
|-------------------|--------------------------|----------|
| JDK               | http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html | >= 1.8.0_121 |
| Maven             | https://maven.apache.org/                                                           | >= 3.6.0     |
| Docker            | https://www.docker.com/                                                             | >= 18        |
| Docker Compose    | https://www.docker.com/                                                             | >= 3.2       |

### Run
To run app, go to the folder with project and type in console:
```
./run.sh
```

### Documentation
For more documentation refer to:
```
./implementation/docs/docs-out/index.html
```
# ReactAndJavaServlets
A basic React fronted communicating with a Java backend through REST endpoints. 

## Backend

The backend is a Spring boot based Java8 application that uses WebServlets to serve the frontend.
The project can be built with maven using the *mvn clean install* command.
The application can be started with the *java -jar <jar-name>* command, which by default will run on localhost:8080 and uses Spring's embedded tomcat.

An h2 in-memory database is used (that will be wiped out when the application is restarted). Hibernate is used as an ORM.


## Frontend

The frontend was created with React (this is actually my first React frontend :D )
Build it with: *npm install*
Start it with: *npm start*

It will be run on localhost:3000 by default.

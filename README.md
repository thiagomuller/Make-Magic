# make-magic
Make magic api!
It creates, updates, deletes and finds characters from Harry Potter series (Which are awesome)

This project is made with Spring Boot and PostgresSQL.
All documentation for the API can be found at http://localhost:8080/swagger.html
There's a postman collection on this repository for reference as well.

Please beware that the following environment variables need to be set in order to get this running:
1 - dbUrl: Database jdbc url, I'm using postgres, but you only need to give a proper jdbc url for whatever db you want to use
2 - dbUsername: User name for your db
3 - dbPassword: Password for the provider db user
4 - potterApiHouseUrl: This is the url used to fetch all valid houseIds at Potter api, the correct url is: 
5 - potterApiKey: You must register to the Potter api webpage in order to get this key
6 - numberOfRetries: Number of retries this API calls Potter api in case of errors

Forgetting to set these up will result in a related exception at the bootstraping of the the application

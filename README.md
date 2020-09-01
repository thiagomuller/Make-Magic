# MakeMagic

Welcome to the Harry Potter api\
Here you will be able to create and store Harry Potter characters.\
This Api is integrated with Potter api, so, make sure to create an account there and get your key, so you can start using this :)\
Potter api: https://www.potterapi.com/

## Documentation
You can find full documentation at [Swagger](https://swagger.io/) interface, which can be accessed at:\
http:localhost:8080/swagger-ui.html\
There's also a [Postman](https://www.postman.com/) collection in this repository that you can import in your postman to see some examples

## Running the API
To run the api, please clone this repo, then run the following commands 

```bash
mvn clean package 
docker-compose build
potterApiKey=$your_api_key docker-compose up

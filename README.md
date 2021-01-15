# Name Application interface

API that returns names from database. Build with Java Spring framework, JDK 11. See the deployed version of the interface from [here](https://secret-plains-56145.herokuapp.com/). Heroku might take some time to serve the application so be patient :)

## Description
API is located at `api/v1/` and has four different endpoints.

- `api/v1/names` - return all the names and their amounts, sorted by popularity
- `api/v1/names/amount` - return total amount of all names in the database
- `api/v1/names/alphabetical` - return all the names sorted alphabetically
- `api/v1/names/{name}` - return amount of the name that is give as a path variable

All the logs from the running application and test are generated to the logs file in the root of the project.

## Installation locally
Copy the repository and open the project with the IDE of your choice. You can build and run the tests before launching it with the command:
```
mvn clean install
```

To run the project locally at localhost:8080, run the command:`
```
mvn spring-boot:run
```
By default, the application reads the json file located at `src/main/resources/json/names.json` and saves the results from that to the database. However, user can define the file to be read from command line arguments - as long as the file is located at resources directory. For example a file `names2.json` in the same location could be called with:
```
mvn spring-boot:run -Dspring-boot.run.arguments=--file.path="/json/names2.json"
```
When the program is run locally, it will create a simple database (.db) file in the `data` directory in the root. If you want to start program again and ìnitiate a new fresh database from a json file, you can remove the previous database files at the root of the project repository  with:
```
rm -rf data/
```
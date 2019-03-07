# Facilist - web shopping list
Group project built during the "Introduction to web programming" at the University of Trento

## Description
The project consists in a webapp that allows users to create and share a shopping list.
A shopping list can contain products created by system admins or normal users.


## Tecnologies

The backend is written in Kotlin using the Spring framework and the frontend is built with Angular 6.
The project uses the Push Notification API to notify users about collaborator actions and web sockets to provide a chat between the users.


## How to run

You can try the project in two ways:
- Use the deployed version on Heroku [here](https://provolosi-web.herokuapp.com/). Please note that:
    * The application can take up to 2 minutes to start
    * Emails are disabled
- Clone the repository and run it locally

Once the app is running you can login:

- as a normal user with email "user@facilist.it" and password "password"
- as another normal user with email "user2@facilist.it" and password "password"
- as an admin user with email "admin@facilist.it" and password "password"

To run it locally you need to start the server and build the webapp.

To start the server on Linux or macOS run
```bash
./gradlew bootRun
```
On Windows
```bash
./gradlew.bat bootRun
```   

To start a  development server for the webapp use:
```bash
cd client
npm i
ng serve --open
```

You can configure different parameters in `src/main/resources/application.properties`

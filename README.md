<div align="center">
  <img src="https://i.ibb.co/RSbYNhr/logo.png" alt="logo" width="200" height="auto" />
  <h1>My Movies List 1.0</h1>
</div>

## 🎬 About

REST API used in [My Movies List](https://nicolas-palacio.github.io/my-movies-list/) . Includes the CRUD for User and Movies, JWT filter to every Http request, security config to handle the endpoints access.

## 👾 Tech Stack

- Framework: Spring Boot (includes Spring Security 6.0).
- Language: Java 17.
- Database : PostgreSQL 15.2v.
- Hosting platform: Heroku.

## Swagger documentation 📃

[Check the doc for details.](https://my-movies-list.herokuapp.com/swagger-ui/index.html#/)

## ✅ Features

- User registration and login.
- Email confirmation to enable the account.
- Handle exception for every request.
- JWT implementation for session management.
- User can add watched movies to a list.
- User can add a profile picture.
- User can update their credentials:
  - Username.
  - Password.
  - Profile picture.
  - Country.

## 🖥 Demo Frontend

- [Web page](https://nicolas-palacio.github.io/my-movies-list/).

## 🔑 Demo Local

To run this project locally, you will need to add the following environment variables to your env.properties file.

- `DATABASE_URL`
- `PGUSER`: user for the database.
- `PGPASSWORD`: password for the database.
- `SERVER_PORT`
- `SPRING_MAIL_HOST`
- `SPRING_MAIL_PORT`
- `SPRING_MAIL_USERNAME`
- `SPRING_MAIL_PASSWORD`
- `DB_DDL_AUTO`: create, create-drop,validate or update.
- `IMAGES_SIZE`
- `SECRET_KEY`: key to encode the tokens.

# Spring Boot, PostgreSQL, Spring Security, JWT, JPA, Rest API

Build Restful CRUD API for a movie blog using Spring Boot, PostgreSQL, JPA and Hibernate.

## Steps to Setup

**1. Clone the application**

```bash
git clone https://github.com/khylko98/moviedb.git
```

**2. Create postgresql database**

```bash
create database moviedb
```

- run `moviedb/data/moviedb.pgsql`

**3. Change postgresql username and password as your postgresql account**

+ open `src/main/resources/application.properties`
+ change `spring.datasource.username` and `spring.datasource.password` as per your postgresql installation

**4.ver1. Run the app using maven**

```bash
mvn spring-boot:run
```

The app will start running at <http://localhost:8080>

**4.ver2. Run the app using docker**

```bash
docker-compose up
```

The app will start running at <http://localhost:8080>

## Explore Rest Api

The app defines following CRUD APIs.

### Auth

| Method | Url                  | Decription | Sample Valid Request Body | 
|--------|----------------------|------------|---------------------------|
| POST   | /moviedb/auth/signup | Sign up    | [JSON](#signup)           |
| POST   | /moviedb/auth/signin | Sign in    | [JSON](#signin)           |

### Users

| Method | Url                     | Description                    | Sample Valid Request Body |
|--------|-------------------------|--------------------------------|---------------------------|
| GET    | /moviedb/user/info      | Get user info                  |                           |
| PATCH  | /moviedb/user/update    | Update user                    | [JSON](#userupdate)       |
| DELETE | /moviedb/user/delete    | Delete user                    |                           |
| POST   | /moviedb/user/addMovie  | Add movie to 'watched' list    | [JSON](#useraddmovie)     |
| GET    | /moviedb/user/getMovies | Get movies from 'watched' list |                           |

### Admin

| Method | Url                            | Description                    | Sample Valid Request Body |
|--------|--------------------------------|--------------------------------|---------------------------|
| GET    | /moviedb/admin/user/{username} | Get full user info by admin    |                           |
| GET    | /moviedb/admin/movie/{title}   | Get movie info by admin        |                           |
| POST   | /moviedb/admin/movie/addMovie  | Add movie to database by admin | [JSON](#adminaddmovie)    |

Test them using postman.

## Valid JSON Request Body's Example

##### <a id="signup">Sign Up -> /moviedb/auth/signup</a>
```json
{
  "username": "Strider",
  "password": "kolobrod",
  "firstName": "John",
  "lastName": "Johnson",
  "age": 32,
  "email": "johnson32@gmail.com",
  "phoneNumber": "+380 99 200 25 30",
  "address": {
    "street": "Sobornyja",
    "city": "Dnipro",
    "country": "Ukraine",
    "zipCode": "49900"
  }
}
```

##### <a id="signin">Sign In -> /moviedb/auth/signin</a>
```json
{
  "username": "Strider",
  "password": "kolobrod"
}
```

##### <a id="userupdate">Update -> /moviedb/user/update</a>
```json
{
  "username": "Strider",
  "password": "kolobrod",
  "firstName": "John",
  "lastName": "Johnson",
  "age": 45,
  "email": "johnson45@gmail.com",
  "phoneNumber": "+380 97 567 52 03",
  "address": {
    "street": "Industrialnyja",
    "city": "Dnipro",
    "country": "Ukraine",
    "zipCode": "49999"
  }
}
```

##### <a id="useraddmovie">User add movie -> /moviedb/user/addMovie</a>
```json
{
  "title": "Seven",
  "releaseDate": "20/04/1999"
}
```

##### <a id="adminaddmovie">Admin add movie -> /moviedb/admin/movie/addMovie</a>
```json
{
  "title": "Shrek",
  "releaseDate": "22/07/2000",
  "director": {
    "firstName": "Andrew",
    "lastName": "Adamson",
    "age": 39
  },
  "actors": [
    {
      "firstName": "Mike",
      "lastName": "Myers",
      "age": 43
    },
    {
      "firstName": "Cameron",
      "lastName": "Diaz",
      "age": 28
    }
  ]
}
```
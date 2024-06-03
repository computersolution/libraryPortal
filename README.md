
# libraryportal

The Library Portal Application is a web-service designed to manage books, borrowers, and borrowing operations for a library.

## Features

- Book Management: Register, Get books in the library inventory.
- Borrower Management: Register new borrowers and manage borrower information and get Borrowers details.
- Borrowing Operations: Allow borrowers to borrow and return books



## Tech Stack

**Server:** Spring Boot, Spring Security, Spring Data JPA, Swagger, & h2 database

## Run Locally

Clone the project

```bash
  git clone https://github.com/computersolution/libraryportal.git
```

Go to the project directory

```bash
  cd libraryportal
```

Build the application

```bash
  ./mvn clean package
```

Run the application

```bash
  java -jar target/libraryportal-0.0.1-SNAPSHOT.jar
```
Access the application

```bash
  http://localhost:8080/libraryportal/login
```


API Documentation
- The API documentation for the Library Portal Application is available using Swagger UI. After running the application locally, you can access the API documentation at http://localhost:8080/libraryportal/swagger-ui/index.html

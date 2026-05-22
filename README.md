# Internship Management System

A full-stack web platform for managing student internships, built as a university project. The system connects students, universities, and companies through three dedicated web applications backed by a shared Spring Boot API.

## Features

- **AI-Powered Recommendations** — Integrates Google Gemini API to match students with internships based on CV skills and interests; returns a ranked list of up to 5 compatible internships with compatibility scores (0–1) and explanations
- **Europass CV Builder** — Students create structured CVs with education, work experience, skills and interests; companies can download CVs as PDF
- **Three-portal Architecture** — Separate applications for students, faculty staff, and companies
- **Internship Listings** — Full CRUD for companies; students can search, filter by technology/company, and apply
- **Work Journal** — Students fill in weekly activity logs; faculty and companies can review and grade
- **RSS Feed** — Public feed of available internship listings
- **Student Import** — Faculty can bulk-import student data from CSV files

## Tech Stack

| Component | Technology |
|-----------|-----------|
| Backend API | Java, Spring Boot, REST |
| Student App | Angular, TypeScript |
| Faculty App | Java JSP (MVC 2) |
| Company App | Java JSP |
| Database | PostgreSQL |
| AI Integration | Google Gemini API |

## Getting Started

### Prerequisites
- Java 17+
- Node.js 18+ & Angular CLI
- Maven
- MySQL 8+
- Apache Tomcat (for faculty and company apps)

### Setup & Run

**1. Database**
```bash
# Import the schema and test data
mysql -u root -p < db/prakse.sql
```

**2. Backend (Spring Boot)**
```bash
cd backend
# Set your Gemini API key in src/main/resources/application.properties
mvn spring-boot:run
```

**3. Faculty App**

Open `faculty-app/` in IntelliJ IDEA and run the Tomcat configuration.

**4. Company App**

Open `company-app/` in IntelliJ IDEA and run the Tomcat configuration.

**5. Student App (Angular)**
```bash
cd student-app
npm install
ng serve
```

### Application URLs

| App | URL |
|-----|-----|
| Backend API | http://localhost:8080 |
| Student App | http://localhost:4200 |
| Faculty App | http://localhost:8081 |
| Company App | http://localhost:8082 |

### Test Credentials

| Role | Username | Password |
|------|----------|----------|
| Faculty | faculty1 | password123 |
| Student | student1 | password123 |
| Company | company1 | password123 |

## Project Structure

```
├── backend/          # Spring Boot REST API + Gemini AI integration
├── student-app/      # Angular frontend for students
├── faculty-app/      # JSP application for faculty (MVC 2)
├── company-app/      # JSP application for companies
└── database/         # MySQL schema and test data (prakse.sql)
```

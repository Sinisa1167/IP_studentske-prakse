# Studentske prakse

## Pokretanje

1. Pokrenuti MySQL i importovati `prakse.sql`
2. Pokrenuti backend:
```
cd backend
mvn spring-boot:run
```
3. Otvoriti `faculty-app/` u IntelliJ i pokrenuti Tomcat konfiguraciju
4. Otvoriti `company-app/` u IntelliJ i pokrenuti Tomcat konfiguraciju
5. Pokrenuti studentsku aplikaciju:
```
cd student-app
npm install
ng serve
```

## Adrese
| Aplikacija | Adresa |
|------------|--------|
| Backend API | http://localhost:8080 |
| Studentska app | http://localhost:4200 |
| Fakultetska app | http://localhost:8081 |
| Kompanijska app | http://localhost:8082 |

## Pristupni podaci
| Uloga | Username | Password |
|-------|----------|----------|
| Fakultet | faculty1 | password123 |
| Student | student1 | password123 |
| Kompanija | company1 | password123 |

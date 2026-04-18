CREATE DATABASE IF NOT EXISTS studentske_prakse
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE studentske_prakse;

-- users
CREATE TABLE users (
    id       BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role     VARCHAR(20)  NOT NULL,
    active   BOOLEAN      NOT NULL DEFAULT TRUE
);

-- students
CREATE TABLE students (
    id            BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id       BIGINT       NOT NULL UNIQUE,
    first_name    VARCHAR(100) NOT NULL,
    last_name     VARCHAR(100) NOT NULL,
    email         VARCHAR(150) NOT NULL,
    phone         VARCHAR(30),
    date_of_birth DATE,
    address       VARCHAR(255),
    photo_path    VARCHAR(255),
    CONSTRAINT fk_students_user FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE INDEX idx_students_last_name ON students (last_name);

-- cv entries
CREATE TABLE cv_entries (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    student_id  BIGINT       NOT NULL,
    type        VARCHAR(20)  NOT NULL,
    title       VARCHAR(255) NOT NULL,
    description TEXT,
    start_date  DATE,
    end_date    DATE,
    extra       VARCHAR(100),
    CONSTRAINT fk_cv_entries_student FOREIGN KEY (student_id) REFERENCES students (id)
);

CREATE INDEX idx_cv_entries_student ON cv_entries (student_id);

-- companies
CREATE TABLE companies (
    id            BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id       BIGINT       NOT NULL UNIQUE,
    name          VARCHAR(255) NOT NULL,
    address       VARCHAR(255),
    contact_email VARCHAR(150),
    description   TEXT,
    active        BOOLEAN NOT NULL DEFAULT TRUE,
    CONSTRAINT fk_companies_user FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE INDEX idx_companies_name ON companies (name);

-- technologies
CREATE TABLE technologies (
    id   BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE
);

-- internships
CREATE TABLE internships (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    company_id  BIGINT       NOT NULL,
    title       VARCHAR(255) NOT NULL,
    description TEXT,
    start_date  DATE,
    end_date    DATE,
    conditions  TEXT,
    active      BOOLEAN NOT NULL DEFAULT TRUE,
    CONSTRAINT fk_internships_company FOREIGN KEY (company_id) REFERENCES companies (id)
);

CREATE INDEX idx_internships_title ON internships (title);

-- internship technologies
CREATE TABLE internship_technologies (
    internship_id  BIGINT NOT NULL,
    technology_id  BIGINT NOT NULL,
    PRIMARY KEY (internship_id, technology_id),
    CONSTRAINT fk_it_internship FOREIGN KEY (internship_id) REFERENCES internships (id),
    CONSTRAINT fk_it_technology FOREIGN KEY (technology_id) REFERENCES technologies (id)
);

-- applications
CREATE TABLE applications (
    id             BIGINT AUTO_INCREMENT PRIMARY KEY,
    student_id     BIGINT      NOT NULL,
    internship_id  BIGINT      NOT NULL,
    status         VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    applied_at     DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_applications_student FOREIGN KEY (student_id) REFERENCES students (id),
    CONSTRAINT fk_applications_internship FOREIGN KEY (internship_id) REFERENCES internships (id)
);

CREATE INDEX idx_applications_student ON applications (student_id);
CREATE INDEX idx_applications_internship ON applications (internship_id);

-- work diary
CREATE TABLE work_diary (
    id             BIGINT AUTO_INCREMENT PRIMARY KEY,
    student_id     BIGINT   NOT NULL,
    internship_id  BIGINT   NOT NULL,
    week_number    INT      NOT NULL,
    activities     TEXT,
    created_at     DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_work_diary_student FOREIGN KEY (student_id) REFERENCES students (id),
    CONSTRAINT fk_work_diary_internship FOREIGN KEY (internship_id) REFERENCES internships (id)
);

-- evaluations
CREATE TABLE evaluations (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    application_id  BIGINT      NOT NULL,
    evaluator_role  VARCHAR(20) NOT NULL,
    grade           INT,
    comment         TEXT,
    created_at      DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_evaluations_application FOREIGN KEY (application_id) REFERENCES applications (id)
);

-- ai recommendations
CREATE TABLE ai_recommendations (
    id             BIGINT AUTO_INCREMENT PRIMARY KEY,
    student_id     BIGINT         NOT NULL,
    internship_id  BIGINT         NOT NULL,
    score          DECIMAL(4, 3)  NOT NULL,
    explanation    TEXT,
    created_at     DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_ai_student FOREIGN KEY (student_id) REFERENCES students (id),
    CONSTRAINT fk_ai_internship FOREIGN KEY (internship_id) REFERENCES internships (id)
);

-- test data

-- users
INSERT INTO users (username, password, role, active) VALUES
('faculty1',  '$2a$12$6Mxb1HKjvwbJpHThfUY7ZedJkPlBnpcwGcH8YmQx4lA3CfE8w7KTS', 'FACULTY', TRUE),
('student1',  '$2a$12$6Mxb1HKjvwbJpHThfUY7ZedJkPlBnpcwGcH8YmQx4lA3CfE8w7KTS', 'STUDENT', TRUE),
('student2',  '$2a$12$6Mxb1HKjvwbJpHThfUY7ZedJkPlBnpcwGcH8YmQx4lA3CfE8w7KTS', 'STUDENT', TRUE),
('student3',  '$2a$12$6Mxb1HKjvwbJpHThfUY7ZedJkPlBnpcwGcH8YmQx4lA3CfE8w7KTS', 'STUDENT', TRUE),
('student4',  '$2a$12$6Mxb1HKjvwbJpHThfUY7ZedJkPlBnpcwGcH8YmQx4lA3CfE8w7KTS', 'STUDENT', TRUE),
('student5',  '$2a$12$6Mxb1HKjvwbJpHThfUY7ZedJkPlBnpcwGcH8YmQx4lA3CfE8w7KTS', 'STUDENT', TRUE),
('student6',  '$2a$12$6Mxb1HKjvwbJpHThfUY7ZedJkPlBnpcwGcH8YmQx4lA3CfE8w7KTS', 'STUDENT', TRUE),
('student7',  '$2a$12$6Mxb1HKjvwbJpHThfUY7ZedJkPlBnpcwGcH8YmQx4lA3CfE8w7KTS', 'STUDENT', TRUE),
('student8',  '$2a$12$6Mxb1HKjvwbJpHThfUY7ZedJkPlBnpcwGcH8YmQx4lA3CfE8w7KTS', 'STUDENT', TRUE),
('student9',  '$2a$12$6Mxb1HKjvwbJpHThfUY7ZedJkPlBnpcwGcH8YmQx4lA3CfE8w7KTS', 'STUDENT', TRUE),
('student10', '$2a$12$6Mxb1HKjvwbJpHThfUY7ZedJkPlBnpcwGcH8YmQx4lA3CfE8w7KTS', 'STUDENT', TRUE),
('student11', '$2a$12$6Mxb1HKjvwbJpHThfUY7ZedJkPlBnpcwGcH8YmQx4lA3CfE8w7KTS', 'STUDENT', TRUE),
('student12', '$2a$12$6Mxb1HKjvwbJpHThfUY7ZedJkPlBnpcwGcH8YmQx4lA3CfE8w7KTS', 'STUDENT', TRUE),
('company1',  '$2a$12$6Mxb1HKjvwbJpHThfUY7ZedJkPlBnpcwGcH8YmQx4lA3CfE8w7KTS', 'COMPANY', TRUE),
('company2',  '$2a$12$6Mxb1HKjvwbJpHThfUY7ZedJkPlBnpcwGcH8YmQx4lA3CfE8w7KTS', 'COMPANY', TRUE),
('company3',  '$2a$12$6Mxb1HKjvwbJpHThfUY7ZedJkPlBnpcwGcH8YmQx4lA3CfE8w7KTS', 'COMPANY', TRUE),
('company4',  '$2a$12$6Mxb1HKjvwbJpHThfUY7ZedJkPlBnpcwGcH8YmQx4lA3CfE8w7KTS', 'COMPANY', TRUE),
('company5',  '$2a$12$6Mxb1HKjvwbJpHThfUY7ZedJkPlBnpcwGcH8YmQx4lA3CfE8w7KTS', 'COMPANY', FALSE);

-- students
INSERT INTO students (user_id, first_name, last_name, email, phone, date_of_birth, address) VALUES
(2,'Marko','Marković','marko@etf.unibl.org','065-111-222','2002-05-15','Banja Luka'),
(3,'Ana','Anić','ana@etf.unibl.org','065-333-444','2001-09-20','Banja Luka'),
(4,'Petar','Petrović','petar@etf.unibl.org','065-555-666','2002-03-10','Sarajevo'),
(5,'Maja','Majić','maja@etf.unibl.org','065-777-888','2001-11-25','Mostar'),
(6,'Ivan','Ivanović','ivan@etf.unibl.org','065-999-000','2003-01-05','Banja Luka'),
(7,'Sara','Sarić','sara@etf.unibl.org','066-111-222','2002-07-18','Tuzla'),
(8,'Nikola','Nikolić','nikola@etf.unibl.org','066-333-444','2001-04-22','Banja Luka'),
(9,'Jelena','Jelenić','jelena@etf.unibl.org','066-555-666','2002-09-30','Sarajevo'),
(10,'Stefan','Stefanović','stefan@etf.unibl.org','066-777-888','2003-02-14','Banja Luka'),
(11,'Milica','Milićević','milica@etf.unibl.org','066-999-000','2001-06-08','Prijedor'),
(12,'Luka','Lukić','luka@etf.unibl.org','067-111-222','2002-12-01','Banja Luka'),
(13,'Teodora','Teodorović','teodora@etf.unibl.org','067-333-444','2001-08-17','Doboj');

-- cv entries
INSERT INTO cv_entries (student_id,type,title,description,start_date,end_date,extra) VALUES
(1,'EDUCATION','ETF','Smjer RI','2021-10-01',NULL,NULL);

-- companies
INSERT INTO companies (user_id,name,address,contact_email,description,active) VALUES
(14,'TechCorp d.o.o.','Banja Luka','info@techcorp.ba','Software',TRUE),
(15,'DataSoft d.o.o.','Banja Luka','info@datasoft.ba','Data',TRUE),
(16,'WebStudio d.o.o.','Sarajevo','info@webstudio.ba','Web',TRUE),
(17,'CloudSys d.o.o.','Banja Luka','info@cloudsys.ba','Cloud',TRUE),
(18,'DevTeam d.o.o.','Mostar','info@devteam.ba','Mobile',FALSE);

-- technologies
INSERT INTO technologies (name) VALUES
('Java'),('Spring Boot'),('Angular'),('React'),('Node.js'),
('Python'),('MySQL'),('PostgreSQL'),('Docker'),('Git'),
('TypeScript'),('Kubernetes'),('AWS'),('MongoDB'),('Vue.js');

-- internships
INSERT INTO internships (company_id, title, description, start_date, end_date, conditions, active) VALUES
(1, 'Java Backend Developer praksa',    'Rad na razvoju backend servisa u Java/Spring Boot okruženju.',    '2025-07-01', '2025-09-30', 'Poznavanje osnova Java programiranja',      TRUE),
(1, 'Angular Frontend praksa',          'Razvoj korisničkih interfejsa korištenjem Angular frameworka.',   '2025-07-01', '2025-09-30', 'Poznavanje HTML, CSS i JavaScript-a',       TRUE),
(1, 'DevOps Engineer praksa',           'Rad sa Docker i Kubernetes tehnologijama.',                        '2025-06-01', '2025-08-31', 'Osnove Linux-a i kontejnerizacije',         TRUE),
(2, 'Data Engineering praksa',          'Rad sa bazama podataka i ETL procesima.',                          '2025-06-01', '2025-08-31', 'Osnove SQL-a i Python-a',                   TRUE),
(2, 'Python Developer praksa',          'Razvoj Python aplikacija za obradu podataka.',                     '2025-07-01', '2025-09-30', 'Poznavanje Python programiranja',           TRUE),
(2, 'Database Administrator praksa',    'Administracija MySQL i PostgreSQL baza podataka.',                 '2025-06-01', '2025-08-31', 'Poznavanje SQL-a',                          TRUE),
(3, 'React Frontend Developer praksa',  'Razvoj modernih web aplikacija u React frameworku.',               '2025-07-01', '2025-09-30', 'Poznavanje JavaScript i React osnova',      TRUE),
(3, 'UI/UX Designer praksa',            'Dizajn korisničkih interfejsa i iskustava.',                        '2025-06-01', '2025-08-31', 'Poznavanje Figma alata',                    TRUE),
(3, 'Vue.js Developer praksa',          'Razvoj web aplikacija korištenjem Vue.js frameworka.',             '2025-07-01', '2025-09-30', 'Poznavanje JavaScript osnova',              TRUE),
(4, 'Cloud Engineer praksa',            'Rad sa AWS cloud servisima i infrastrukturom.',                     '2025-06-01', '2025-08-31', 'Osnove cloud computing-a',                  TRUE),
(4, 'Node.js Backend praksa',           'Razvoj REST API servisa korištenjem Node.js.',                     '2025-07-01', '2025-09-30', 'Poznavanje JavaScript i Node.js osnova',    TRUE),
(4, 'Full Stack Developer praksa',      'Rad na frontend i backend dijelu web aplikacije.',                  '2025-06-01', '2025-09-30', 'Poznavanje React i Node.js osnova',         TRUE);

-- internship technologies
INSERT INTO internship_technologies (internship_id, technology_id) VALUES
(1, 1), (1, 2), (1, 7),
(2, 3), (2, 7),
(3, 9), (3, 12),
(4, 6), (4, 7), (4, 8),
(5, 6), (5, 14),
(6, 7), (6, 8),
(7, 4), (7, 11),
(8, 11),
(9, 15), (9, 11),
(10, 13), (10, 9),
(11, 5), (11, 10),
(12, 4), (12, 5), (12, 11);

-- applications
INSERT INTO applications (student_id, internship_id, status, applied_at) VALUES
(1, 1,  'ACCEPTED',  '2025-04-10 10:00:00'),
(1, 2,  'PENDING',   '2025-04-11 11:00:00'),
(1, 3,  'REJECTED',  '2025-04-12 09:00:00'),
(2, 7,  'ACCEPTED',  '2025-04-09 09:00:00'),
(2, 9,  'PENDING',   '2025-04-10 10:00:00'),
(3, 4,  'ACCEPTED',  '2025-04-08 08:00:00'),
(3, 5,  'PENDING',   '2025-04-09 09:00:00'),
(4, 7,  'PENDING',   '2025-04-10 10:00:00'),
(4, 12, 'PENDING',   '2025-04-11 11:00:00'),
(5, 1,  'PENDING',   '2025-04-10 10:00:00'),
(5, 11, 'ACCEPTED',  '2025-04-09 09:00:00'),
(6, 4,  'PENDING',   '2025-04-11 11:00:00'),
(6, 6,  'ACCEPTED',  '2025-04-10 10:00:00'),
(7, 2,  'PENDING',   '2025-04-12 12:00:00'),
(7, 8,  'ACCEPTED',  '2025-04-11 11:00:00'),
(8, 10, 'ACCEPTED',  '2025-04-09 09:00:00'),
(8, 3,  'PENDING',   '2025-04-10 10:00:00'),
(9, 5,  'REJECTED',  '2025-04-08 08:00:00'),
(9, 12, 'ACCEPTED',  '2025-04-09 09:00:00'),
(10, 1, 'PENDING',   '2025-04-10 10:00:00'),
(10, 4, 'PENDING',   '2025-04-11 11:00:00'),
(11, 7, 'ACCEPTED',  '2025-04-09 09:00:00'),
(11, 9, 'PENDING',   '2025-04-10 10:00:00'),
(12, 6, 'PENDING',   '2025-04-11 11:00:00'),
(12, 11,'ACCEPTED',  '2025-04-10 10:00:00');

-- work diary
INSERT INTO work_diary (student_id, internship_id, week_number, activities, created_at) VALUES
(1, 1, 1, 'Upoznavanje sa projektom i postavka razvojnog okruženja.',         '2025-07-07 18:00:00'),
(1, 1, 2, 'Implementacija REST endpointa za upravljanje korisnicima.',         '2025-07-14 18:00:00'),
(1, 1, 3, 'Rad na autentifikaciji korištenjem JWT tokena.',                    '2025-07-21 18:00:00'),
(2, 7, 1, 'Upoznavanje sa React projektom i instalacija zavisnosti.',          '2025-07-07 18:00:00'),
(2, 7, 2, 'Implementacija komponenti za prikaz liste proizvoda.',              '2025-07-14 18:00:00'),
(3, 4, 1, 'Uvod u ETL procese i pregled postojeće arhitekture.',              '2025-06-07 18:00:00'),
(3, 4, 2, 'Implementacija pipeline-a za učitavanje podataka.',                '2025-06-14 18:00:00'),
(3, 4, 3, 'Optimizacija SQL upita i indeksiranje tabela.',                    '2025-06-21 18:00:00'),
(5, 11, 1, 'Upoznavanje sa Node.js projektom i arhitekturom.',                '2025-07-07 18:00:00'),
(5, 11, 2, 'Implementacija API endpointa za autentifikaciju.',                '2025-07-14 18:00:00'),
(6, 6, 1, 'Upoznavanje sa bazom podataka i šemom.',                          '2025-06-07 18:00:00'),
(6, 6, 2, 'Pisanje složenih SQL upita i optimizacija.',                       '2025-06-14 18:00:00'),
(7, 8, 1, 'Upoznavanje sa Figma alatom i dizajn sistemom.',                  '2025-06-07 18:00:00'),
(7, 8, 2, 'Izrada wireframe-ova za mobilnu aplikaciju.',                      '2025-06-14 18:00:00'),
(8, 10, 1, 'Uvod u AWS servise i konfiguracija okruženja.',                   '2025-06-07 18:00:00'),
(8, 10, 2, 'Postavljanje EC2 instanci i konfiguracija sigurnosnih grupa.',    '2025-06-14 18:00:00'),
(9, 12, 1, 'Upoznavanje sa Full Stack projektom.',                            '2025-06-07 18:00:00'),
(9, 12, 2, 'Implementacija login stranice.',                                  '2025-06-14 18:00:00'),
(11, 7, 1, 'Upoznavanje sa React projektom.',                                 '2025-07-07 18:00:00'),
(12, 11, 1, 'Upoznavanje sa Node.js okruženjem.',                             '2025-07-07 18:00:00');

-- evaluations (neke sa ocjenama, neke bez)
INSERT INTO evaluations (application_id, evaluator_role, grade, comment, created_at) VALUES
(1,  'COMPANY', 9,  'Student pokazuje odličan napredak i inicijativu.',        '2025-09-30 12:00:00'),
(1,  'FACULTY', 8,  'Dnevnik rada uredno vođen, zadaci ispunjeni.',            '2025-10-05 10:00:00'),
(4,  'COMPANY', 10, 'Izvanredan rad, preporučujemo za stalno zaposlenje.',     '2025-09-30 12:00:00'),
(4,  'FACULTY', 9,  'Odličan student, sve obaveze ispunjene na vrijeme.',      '2025-10-05 10:00:00'),
(6,  'COMPANY', 8,  'Solidan rad, dobro poznavanje SQL-a.',                    '2025-08-31 12:00:00'),
(11, 'COMPANY', 9,  'Student brzo uči i prilagođava se novim tehnologijama.', '2025-09-30 12:00:00'),
(15, 'COMPANY', 7,  'Dobar rad, potrebno poboljšati komunikaciju.',           '2025-08-31 12:00:00'),
(16, 'COMPANY', 10, 'Izvanredan dizajnerski talenat.',                        '2025-08-31 12:00:00'),
(18, 'COMPANY', 8,  'Dobro poznavanje AWS servisa.',                          '2025-08-31 12:00:00'),
(22, 'COMPANY', 9,  'Odličan rad na React komponentama.',                     '2025-09-30 12:00:00'),
(25, 'COMPANY', 8,  'Solidan rad na Node.js projektu.',                       '2025-09-30 12:00:00');

-- ai recommendations
INSERT INTO ai_recommendations (student_id, internship_id, score, explanation, created_at) VALUES
(1, 1, 0.920, 'Student posjeduje Java i Spring Boot vještine koje direktno odgovaraju zahtjevima prakse.', NOW()),
(1, 2, 0.650, 'Student ima osnove Angular-a ali nedostaje iskustvo sa frontend projektima.',               NOW()),
(1, 3, 0.340, 'Nedostaje iskustvo sa Docker-om i Kubernetes-om koje praksa zahtijeva.',                    NOW()),
(1, 4, 0.280, 'Nedostaje iskustvo sa Python-om i bazama podataka koje praksa zahtijeva.',                  NOW()),
(1, 11, 0.450, 'Student poznaje osnove ali nema direktnog iskustva sa Node.js.',                           NOW());
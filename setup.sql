DROP DATABASE IF EXISTS university;
CREATE DATABASE IF NOT EXISTS university;
USE university;

CREATE TABLE IF NOT EXISTS students (
    roll_no VARCHAR(50) PRIMARY KEY,
    name VARCHAR(255),
    password VARCHAR(255),
    department VARCHAR(100),
    semester INT,
    minor_stream VARCHAR(100) DEFAULT 'None'
);

CREATE TABLE IF NOT EXISTS faculty (
    id INT PRIMARY KEY,
    name VARCHAR(255),
    password VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS courses (
    id INT PRIMARY KEY,
    name VARCHAR(255),
    semester INT,
    credits INT,
    capacity INT DEFAULT 50,
    schedule VARCHAR(100),
    faculty_id INT,
    prerequisite_id INT NULL,
    FOREIGN KEY (faculty_id) REFERENCES faculty(id) ON DELETE SET NULL,
    FOREIGN KEY (prerequisite_id) REFERENCES courses(id) ON DELETE SET NULL
);

CREATE TABLE IF NOT EXISTS registrations (
    id INT AUTO_INCREMENT PRIMARY KEY,
    roll_no VARCHAR(50),
    course_id INT,
    status VARCHAR(20) DEFAULT 'ENROLLED',
    FOREIGN KEY (roll_no) REFERENCES students(roll_no) ON DELETE CASCADE,
    FOREIGN KEY (course_id) REFERENCES courses(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS minor_streams (
    id INT PRIMARY KEY,
    name VARCHAR(100)
);

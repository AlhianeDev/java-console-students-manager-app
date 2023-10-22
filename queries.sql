/*

CREATE TABLE IF NOT EXISTS Students (

	id INT NOT NULL PRIMARY KEY AUTO_INCREMENT, 

	first_name VARCHAR(255) NOT NULL,

	last_name VARCHAR(255) NOT NULL,

	gender_id INT NOT NULL,

	country_id INT NOT NULL

);

*/

# ALTER TABLE Students ADD user_id INT NOT NULL;

# TRUNCATE TABLE Students;

/*

CREATE TABLE IF NOT EXISTS Genders (

	id INT NOT NULL PRIMARY KEY AUTO_INCREMENT, 

	gender_type VARCHAR(255) NOT NULL

);

*/

/*

CREATE TABLE IF NOT EXISTS Countries (

	id INT NOT NULL PRIMARY KEY AUTO_INCREMENT, 

	country_name VARCHAR(255) NOT NULL

);

*/

# TRUNCATE TABLE Genders;

# INSERT INTO Genders (gender_type) VALUES ("MALE"), ("FEMALE");

# INSERT INTO Countries (country_name) VALUES ("Morocco"), ("Egypt"), ("Syria"), ("Algeria"), ("Saudi Arabia");

/*

INSERT INTO Students (first_name, last_name, gender_id, country_id, user_id) VALUES 

("Lahcen", "Alhiane", 1, 1, 1),

("Aicha", "Alhiane", 2, 2, 1),

("Hassna", "Ali", 2, 3, 2),

("Hafid", "Iben Daoud", 1, 4, 3),

("Medo", "Alhassani", 1, 5, 2),

("Alia", "Alia", 2, 3, 3);

*/

/*

SELECT Students.id, first_name, last_name, gender_type, country_name from Students 

INNER JOIN Genders ON Genders.id = Students.gender_id

INNER JOIN Countries ON Countries.id = Students.country_id

WHERE Students.user_id = 3

ORDER BY Students.id ASC;

*/

/*

CREATE TABLE IF NOT EXISTS Users (

	id INT NOT NULL PRIMARY KEY AUTO_INCREMENT, 

	first_name VARCHAR(255) NOT NULL,

	last_name VARCHAR(255) NOT NULL,

	user_name VARCHAR(255) NOT NULL,

	password VARCHAR(255) NOT NULL

);

*/

# TRUNCATE TABLE Users;

/*

INSERT INTO Users (first_name, last_name, user_name, password) VALUES 

("Lahcen", "Alhiane", "lalhiane", "test1234"),

("Aicha", "Alhiane", "aicha99", "aicha1234"),

("Hassna", "Ali", "hassna99", "hassna1234");

*/

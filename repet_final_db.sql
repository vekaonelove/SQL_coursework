-- Create the Customer table
CREATE TABLE IF NOT EXISTS Customer (
    ID SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    surname VARCHAR(100) NOT NULL,
    phoneNumber VARCHAR(15),
    email VARCHAR(100)
);

-- Create the Country table
CREATE TABLE IF NOT EXISTS Country (
    ID SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL
);

-- Create the City table
CREATE TABLE IF NOT EXISTS City (
    ID SERIAL PRIMARY KEY,
    countryID INT REFERENCES Country(ID),
    name VARCHAR(100) NOT NULL
);

-- Create the ExperienceLevel table
CREATE TABLE IF NOT EXISTS ExperienceLevel (
    ID SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL
);

-- Create the Subject table
CREATE TABLE IF NOT EXISTS Subject (
    ID SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL
);

-- Create the Performer table
CREATE TABLE IF NOT EXISTS Performer (
    ID SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    surname VARCHAR(100) NOT NULL,
    experienceID INT REFERENCES ExperienceLevel(ID),
    subject VARCHAR(100) NOT NULL,
	phoneNumber VARCHAR(15),
    email VARCHAR(100)
);

-- Create the Exam table
CREATE TABLE IF NOT EXISTS  Exam (
    ID SERIAL PRIMARY KEY,
    subjectID INT REFERENCES Subject(ID),
    countryID INT REFERENCES Country(ID),
    name VARCHAR(100) NOT NULL
);

-- Create the Order table
CREATE TABLE IF NOT EXISTS "Order" (
    ID SERIAL PRIMARY KEY,
    subjectID INT REFERENCES Subject(ID),
    performerID INT REFERENCES Performer(ID),
    examID INT REFERENCES Exam(ID),
    cityID INT REFERENCES City(ID),
	customerID INT REFERENCES Customer(ID)
);

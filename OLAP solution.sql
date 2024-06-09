-- Drop tables if they exist to avoid conflicts
DROP TABLE IF EXISTS dim_customer CASCADE;
DROP TABLE IF EXISTS dim_country CASCADE;
DROP TABLE IF EXISTS dim_city CASCADE;
DROP TABLE IF EXISTS dim_experience_level CASCADE;
DROP TABLE IF EXISTS dim_subject CASCADE;
DROP TABLE IF EXISTS dim_performer CASCADE;
DROP TABLE IF EXISTS dim_exam CASCADE;
DROP TABLE IF EXISTS fact_order CASCADE;

-- Create the Country dimension table
CREATE TABLE dim_country (
    country_key SERIAL PRIMARY KEY,
    country_id SERIAL UNIQUE,
    name VARCHAR(100) NOT NULL UNIQUE
);

-- Create the City dimension table
CREATE TABLE dim_city (
    city_key SERIAL PRIMARY KEY,
    city_id SERIAL UNIQUE,
    country_id INT REFERENCES dim_country(country_id),
    name VARCHAR(100) NOT NULL
);

-- Create the ExperienceLevel dimension table
CREATE TABLE dim_experience_level (
    experience_key SERIAL PRIMARY KEY,
    experience_id SERIAL UNIQUE,
    name VARCHAR(100) NOT NULL UNIQUE,
    valid_from TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    valid_to TIMESTAMP,
    current_flag BOOLEAN DEFAULT TRUE
);

-- Create the Subject dimension table
CREATE TABLE dim_subject (
    subject_key SERIAL PRIMARY KEY,
    subject_id SERIAL UNIQUE,
    name VARCHAR(100) NOT NULL UNIQUE
);

-- Create the Performer dimension table
CREATE TABLE dim_performer (
    performer_key SERIAL PRIMARY KEY,
    performer_id SERIAL UNIQUE,
    name VARCHAR(100) NOT NULL,
    surname VARCHAR(100) NOT NULL,
    experience_id INT REFERENCES dim_experience_level(experience_id),
    subject_id INT REFERENCES dim_subject(subject_id),
    phone_number VARCHAR(255),
    email VARCHAR(100)
);

-- Create the Exam dimension table
CREATE TABLE dim_exam (
    exam_key SERIAL PRIMARY KEY,
    exam_id SERIAL UNIQUE,
    subject_id INT REFERENCES dim_subject(subject_id),
    country_id INT REFERENCES dim_country(country_id),
    name VARCHAR(100) NOT NULL UNIQUE
);

-- Create the Customer dimension table
CREATE TABLE dim_customer (
    customer_key SERIAL PRIMARY KEY,
    customer_id SERIAL UNIQUE,
    name VARCHAR(100) NOT NULL,
    surname VARCHAR(100) NOT NULL,
    phone_number VARCHAR(255),
    email VARCHAR(100)
);

-- Create the Order fact table
CREATE TABLE fact_order (
    order_id SERIAL PRIMARY KEY,
    subject_id INT REFERENCES dim_subject(subject_id),
    performer_id INT REFERENCES dim_performer(performer_id),
    exam_id INT REFERENCES dim_exam(exam_id),
    city_id INT REFERENCES dim_city(city_id),
    customer_id INT REFERENCES dim_customer(customer_id)
);

-- Function to handle SCD Type 2 updates for dim_customer
CREATE OR REPLACE FUNCTION update_customer_dimension(
    IN p_customer_id INT,
    IN p_name VARCHAR(100),
    IN p_surname VARCHAR(100),
    IN p_phone_number VARCHAR(255),
    IN p_email VARCHAR(100)
)
RETURNS VOID AS $$
DECLARE
    v_current_key INT;
BEGIN
    -- Find the current row for the customer
    SELECT customer_key INTO v_current_key
    FROM dim_customer
    WHERE customer_id = p_customer_id
    AND current_flag = TRUE;

    -- Check if there's any change in customer details
    IF v_current_key IS NOT NULL AND (p_name, p_surname, p_phone_number, p_email) <> (
        SELECT name, surname, phone_number, email
        FROM dim_customer
        WHERE customer_key = v_current_key
    ) THEN
        -- Expire the current row
        UPDATE dim_customer
        SET valid_to = CURRENT_TIMESTAMP,
            current_flag = FALSE
        WHERE customer_key = v_current_key;

        -- Insert a new row with updated details
        INSERT INTO dim_customer (customer_id, name, surname, phone_number, email)
        VALUES (p_customer_id, p_name, p_surname, p_phone_number, p_email);
    ELSE
        -- If no change, update the current row
        UPDATE dim_customer
        SET name = p_name,
            surname = p_surname,
            phone_number = p_phone_number,
            email = p_email
        WHERE customer_key = v_current_key;
    END IF;
END;
$$ LANGUAGE plpgsql;

package com.example.sqlspringbatch.ETL1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

@Service
public class EtlService {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    private ArrayList<Long> subjectIds = new ArrayList<>();
    @Transactional
    public void processFile1(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean isHeader = true;

            while ((line = br.readLine()) != null) {
                if (isHeader) {
                    isHeader = false;
                    continue;
                }

                String[] fields = line.split(",");
                String subjectName = fields[9];
                long subjectId = getOrInsertSubject(subjectName);
                subjectIds.add(subjectId);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Transactional
    public void processFile2(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean isHeader = true;
            int i = 0;
            while ((line = br.readLine()) != null) {
                if (isHeader) {
                    isHeader = false;
                    continue;
                }
                String[] fields = line.split(",");
                String countryName = fields[0];
                String cityName = fields[1];
                String examName = fields[2];
                long countryId = getOrInsertCountry(countryName);
                getOrInsertExam(examName, countryId, subjectIds.get(i));
                i++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private long getOrInsertCountry(String name) {
        try {
            String select = "SELECT id FROM Country WHERE name = ?";
            Long id = jdbcTemplate.queryForObject(select, new Object[]{name}, Long.class);

            if (id != null) {
                return id;
            } else {
                String insert = "INSERT INTO Country (name) VALUES (?)";
                jdbcTemplate.update(insert, name);
                return jdbcTemplate.queryForObject(select, new Object[]{name}, Long.class);
            }
        } catch (EmptyResultDataAccessException e) {
            throw new RuntimeException("Country not found: " + name, e);
        } catch (DataAccessException e) {
            throw new RuntimeException("Error inserting or retrieving country", e);
        }
    }

    private long getOrInsertCity(String name, long countryId) {
        String select = null;
        try {
            select = "SELECT id FROM City WHERE name = ? AND countryID = ?";
            return jdbcTemplate.queryForObject(select, new Object[]{name, countryId}, Long.class);
        } catch (EmptyResultDataAccessException e) {
            String insert = "INSERT INTO City (name, countryID) VALUES (?, ?)";
            jdbcTemplate.update(insert, name, countryId);
            return jdbcTemplate.queryForObject(select, new Object[]{name, countryId}, Long.class);
        } catch (DataAccessException e) {
            throw new RuntimeException("Error inserting or retrieving city", e);
        }
    }
    private long getOrInsertExperienceLevel(String name) {
        String select = null;
        try {
            select = "SELECT id FROM ExperienceLevel WHERE name = ?";
            return jdbcTemplate.queryForObject(select, new Object[]{name}, Long.class);
        } catch (EmptyResultDataAccessException e) {
            String insert = "INSERT INTO ExperienceLevel (name) VALUES (?)";
            jdbcTemplate.update(insert, name);
            return jdbcTemplate.queryForObject(select, new Object[]{name}, Long.class);
        } catch (DataAccessException e) {
            throw new RuntimeException("Error inserting or retrieving experience level", e);
        }
    }



    private long getOrInsertSubject(String name) {
        try {
            String select = "SELECT id FROM Subject WHERE name = ?";
            Long id = jdbcTemplate.queryForObject(select, new Object[]{name}, Long.class);

            if (id != null) {
                return id;
            } else {
                String insert = "INSERT INTO Subject (name) VALUES (?)";
                jdbcTemplate.update(insert, name);
                return jdbcTemplate.queryForObject(select, new Object[]{name}, Long.class);
            }
        } catch (EmptyResultDataAccessException e) {
            throw new RuntimeException("Subject not found: " + name, e);
        } catch (DataAccessException e) {
            throw new RuntimeException("Error inserting or retrieving subject", e);
        }
    }

    private long getOrInsertCustomer(String name, String surname, String phone, String email) {
        String select = "SELECT id FROM Customer WHERE name = ? AND surname = ? AND phoneNumber = ? AND email = ?";
        try {
            Long id = jdbcTemplate.queryForObject(select, new Object[]{name, surname, phone, email}, Long.class);

            if (id != null) {
                return id;
            } else {
                String insert = "INSERT INTO Customer (name, surname, phoneNumber, email) VALUES (?, ?, ?, ?)";
                jdbcTemplate.update(insert, name, surname, phone, email);
                return jdbcTemplate.queryForObject(select, new Object[]{name, surname, phone, email}, Long.class);
            }
        } catch (EmptyResultDataAccessException e) {
            String insert = "INSERT INTO Customer (name, surname, phoneNumber, email) VALUES (?, ?, ?, ?)";
            jdbcTemplate.update(insert, name, surname, phone, email);
            return jdbcTemplate.queryForObject(select, new Object[]{name, surname, phone, email}, Long.class);
        } catch (DataAccessException e) {
            throw new RuntimeException("Error inserting or retrieving customer", e);
        }
    }


    private long getOrInsertExam(String name, long countryId, long subjectId) {
        try {
            String select = "SELECT id FROM Exam WHERE name = ?";
            Long id = jdbcTemplate.queryForObject(select, new Object[]{name}, Long.class);

            if (id != null) {
                return id;
            } else {
                String insert = "INSERT INTO Exam (name, countryID, subjectID) VALUES (?, ?, ?)";
                jdbcTemplate.update(insert, name, countryId, subjectId);
                return jdbcTemplate.queryForObject(select, new Object[]{name}, Long.class);
            }
        } catch (EmptyResultDataAccessException e) {
            throw new RuntimeException("Exam not found: " + name, e);
        } catch (DataAccessException e) {
            throw new RuntimeException("Error inserting or retrieving exam", e);
        }
    }

    private long getOrInsertPerformer(String name, String surname, long experienceId, long subjectId, String phoneNumber, String email) {
        try {
            String insert = "INSERT INTO Performer (name, surname, experienceID, subjectID, phoneNumber, email) " +
                    "VALUES (?, ?, ?, ?, ?, ?) ";
            jdbcTemplate.update(insert, name, surname, experienceId, subjectId, phoneNumber, email);
            String select = "SELECT id FROM Performer WHERE name = ? AND surname = ? " +
                    "AND experienceID = ? AND subjectID = ? AND phoneNumber = ? AND email = ?";
            return jdbcTemplate.queryForObject(select, new Object[]{name, surname, experienceId, subjectId, phoneNumber, email}, Long.class);
        } catch (DataAccessException e) {
            throw new RuntimeException("Error inserting performer", e);
        }
    }


    @Transactional
    public void processOrderFile(String filePath1, String filePath2) {
        try (BufferedReader br1 = new BufferedReader(new FileReader(filePath1));
             BufferedReader br2 = new BufferedReader(new FileReader(filePath2))) {
            String line1, line2;
            boolean isHeader = true;

            while ((line1 = br1.readLine()) != null && (line2 = br2.readLine()) != null) {
                if (isHeader) {
                    isHeader = false;
                    continue;
                }

                String[] fields1 = line1.split(",");
                String[] fields2 = line2.split(",");

                String countryName = fields2[0];
                String cityName = fields2[1];
                String examName = fields2[2];

                long countryId = getOrInsertCountry(countryName);
                long cityId = getOrInsertCity(cityName, countryId);

                String subjectName = fields1[9];
                long subjectId = getOrInsertSubject(subjectName);
                long examId = getOrInsertExam(examName, countryId, subjectId);

                long experienceId = getOrInsertExperienceLevel(fields1[8]);
                long performerId = getOrInsertPerformer(fields1[4], fields1[5], experienceId, subjectId, fields1[6], fields1[7]);

                long customerId = getOrInsertCustomer(fields1[0], fields1[1], fields1[2], fields1[3]);

                getOrInsertOrder(subjectId, performerId, examId, cityId, customerId);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private Long getOrInsertOrder(long subjectId, long performerId, long examId, long cityId, long customerId) {
        String selectOrder = "SELECT id FROM \"Order\" WHERE subjectID = ? AND performerID = ? AND examID = ? AND cityID = ? AND customerID = ?";
        try {
            // Try to find the existing order
            Long foundOrderId = jdbcTemplate.queryForObject(selectOrder, Long.class, subjectId, performerId, examId, cityId, customerId);
            return foundOrderId; // Return the found order ID if it exists
        } catch (EmptyResultDataAccessException e) {
            // Order doesn't exist, insert it
            String insertOrder = "INSERT INTO \"Order\" (subjectID, performerID, examID, cityID, customerID) VALUES (?, ?, ?, ?, ?) RETURNING id";
            try {
                // Execute the insert and return the generated order ID
                return jdbcTemplate.queryForObject(insertOrder, Long.class, subjectId, performerId, examId, cityId, customerId);
            } catch (DataAccessException ex) {
                throw new RuntimeException("Error inserting order", ex);
            }
        } catch (DataAccessException e) {
            throw new RuntimeException("Error retrieving order", e);
        }
    }


}
# SQL coursework
_My EHU/ESDE coursework for SQL (Relational Databases + SQL and Data Processing)._

## **Tools and technologies used**:
- PosgreSQL
- PowerBI
- Mockaroo
- Java + Spring Batch

## **Workflow**:
1) Developing [ER diagram](https://github.com/vekaonelove/SQL_coursework/blob/main/ER%20diagram.jpg)
2) Creating DB using PosgreSQL - [OLTP solution](https://github.com/vekaonelove/SQL_coursework/blob/main/OLTP%20solution)
3) Creating datasets: 2 .csv files containing data according to DB entities: [file1](https://github.com/vekaonelove/SQL_coursework/blob/main/file1.csv) and [file2](https://github.com/vekaonelove/SQL_coursework/blob/main/file2.csv)
4) Writing a _**rerunable**_ ETL script to load data from .csv files to our DB using [Spring Batch](https://github.com/vekaonelove/SQL_coursework/tree/main/ETL1)
5) Building a snowflake DWH based on our initial DB [(OLAP solution)](https://github.com/vekaonelove/SQL_coursework/blob/main/OLAP%20solution)
6) Writing a second ETL script to [move data from OLTP DB to the OLAP one](https://github.com/vekaonelove/SQL_coursework/blob/main/ETL2/DataMigration.java) using Spring Batch
7) Creating a PowerBI report with [visualisation](https://github.com/vekaonelove/SQL_coursework/blob/main/PowerBI.png)
8) Writing detailed [report](https://github.com/vekaonelove/SQL_coursework/blob/main/coursework%20report.docx) on the project and steps performed

## **Project description**
_As a product owner, I'm eager to provide my projects with sustainable solutions, so the topic I've chosen refers to my educational project - [@repet.hub](https://apple-booklet-260.notion.site/repet-hub-677bfcdbbe8e43c58f4860865c497915).
My application provides educational services (design, materials creating, video editing, curators' services) for teachers and online schools._

My DB refers to such entities as: 
- Customer
- Performer
- Experience Level
- Subject
- Exam
- Country
- City
- Order

And provides a business solution for the DB layer of my application.
It allows the customer to place an order, subscribe to our newsletter or simply receive a consultation from our customer support specialists.

Detailed documentation can be found [here]()

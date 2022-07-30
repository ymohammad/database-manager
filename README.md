## Database Manager

Application with collection of utilities to Manage Different types of Databases. Using this application, we can peform following useful operations. These are the independent utilities and can be launched and executed using Command Line using Java Runtime Enveronment.

- [**Export DDL**](https://github.com/ymohammad/database-manager/tree/main/export-db-objects/) : Many times we need to export Database DDL (Database Defination) information to external files. Exported DDL files can be committed to Repository and can be tracked for changes. To achieve the same, the [Database Export Utitlity](https://github.com/ymohammad/database-manager/tree/main/export-db-objects/) is used. It exports the database objects to individual files. Export is configurable, based on the provided Query, selected objects exported. It is use for different database types like Oracle, MySQL, PostgreSQL. Export objects includes TABLE, VIEW, PROCEDURE, FUNCTION, PACKAGE SPECIFICATION, PACKAGE BODY etc.  [Click Here](https://github.com/ymohammad/database-manager/tree/main/export-db-objects/) for more details.

- **Comparing two different databases**: Comparing two databases. It compares the Database tables, its data, Views configuration and PL/SQL code. It analysis the source and destination database and generates a comparision report which can be exported and shared across. It is useful if we want to compare the contents of two different database instances for debugging purpose.
- **Copying Data from Source to Destination database**: To copy data selectively from source database table to destination table, this utiltity can be used. We can map the source and destination columns before moving the data. 
- **Export Data to File** Export query result data to a file in the user specified file format. User can select the file format as CSV, XML or JSON. 
- **Import Data from File** Using this utiltity, we can import data from a file to the specified databases table. Supported import file format are CSV, XML or JSON. 
- **View Database Data**: We can view database content based on the user given query.
- **Database Performance Analysis**: It analysis the database and generate reports based on the standard queries and generate performance repot.

# Rabobank Customer Statement Processor
Rabobank receives monthly deliveries of customer statement records. This information is delivered in two formats, CSV and XML. These records need to be validated.

## Input
The format of the file is a simplified format of the MT940 format. The format is as follows:

Field  |Description
----|----
Transaction reference  | A numeric value
Account number   | An IBAN 
Account | IBAN 
Start Balance | The starting balance in Euros 
Mutation | Either and addition (+) or a deducation (-) 
Description | Free text 
End Balance | The end balance in Euros 

## Output
There are two validations:
     * all transaction references should be unique
	* the end balance needs to be validated
At the end of the processing, a report needs to be created which will display both the transaction reference and description of each of the failed records.

# Steps to run the application:
1.	Clone the Spring Boot project Rabo_Bnk_Cust_Stmt_Procs by using below web URL.

https://github.com/manickaselvi/Rabo_Bnk_Cust_Stmt_Procs.git

2.	Run maven command to install dependency.
3.	Run the project by using Spring Boot App with embedded tomcat.
4.	This Web service application have one active service to process Csv/Xml files. please find service url below,

http://localhost:8085/rabobank/statmentProcessor

5.	Upload input csv/xml file in the service using postman client.
6.	The input file will be validated based on two conduction mentioned in the problem statment.(validation condition mentioned in expected output section)
      *	Duplicate Transaction key check, 
      *	End balance calculation check. (endbalance = startbalance – mutation)
7.  Finally invalid records will be getting as webservice response with status code. 

### Screen Shots added in below mentioned github web URL
### Success scenario:

https://github.com/manickaselvi/Rabo_Bnk_Cust_Stmt_Procs/blob/master/documents/raboBnk_cust_stmt_process_success_csv_01.PNG

https://github.com/manickaselvi/Rabo_Bnk_Cust_Stmt_Procs/blob/master/documents/raboBnk_cust_stmt_process_success_xml_1.PNG


### Error scenario: 
1.	Handled the following error check in input file,
    *	Only Csv,Xml file can be Uploaded. Other file format will not be supported.
    *	Mandatory input check.
    *	Application runtime exception also handled using @ExceptionHandler.

https://github.com/manickaselvi/Rabo_Bnk_Cust_Stmt_Procs/blob/master/documents/raboBnk_cust_stmt_process_error_2.PNG

https://github.com/manickaselvi/Rabo_Bnk_Cust_Stmt_Procs/blob/master/documents/raboBnk_cust_stmt_process_error_1.PNG


### Test Cases:
Created test cases for all service classes in the application, (included in src/test/java/com/rabobank/RaboBnkCustStmtProcessorApplicationTests.java)
PFB the test result,

### Test Results:

https://github.com/manickaselvi/Rabo_Bnk_Cust_Stmt_Procs/blob/master/documents/raboBnk_cust_stmt_process_test_case.PNG


#### Developed by Manickaselvi M (https://github.com/manickaselvi)

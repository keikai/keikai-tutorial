![](images/keikai_logo.png) 

# Keikai Tutorial
[Keikai](https://keikai.io/) works as a spreadsheet component of your web application. This project is the source code of [Keikai tutorial](https://doc.keikai.io/tutorial), and it demonstrates how you can build a web application in Keikai with several examples.

Welcome to check related sites:

* [Demo](https://keikai.io/demo)
* [Documentation](https://doc.keikai.io)


# How to Run This Project
Clone the project and launch your command line interface in the keikai-tutorial folder. Execute the following commands based on your environment.

## Maven installed
`mvn jetty:run`

## No Maven installed yet
Run the Maven wrapper below which will download everything needed for you during starting up: 
* Linux / Mac

`./mvnw jetty:run`

* Windows

`mvnw.cmd jetty:run`


Then visit http://localhost:8080/tutorial


Those who know Gradle, please check `build.gradle`.

# Scenarios to Demonstrate

## Online Spreadsheet Editor
Keikai can render an xlsx file with a Excel-like web UI and a built-in toolbar. After importing the xlsx file end users can edit it in the browser with common features like changing cell content, font, color, format, formulas and so on.

## Work with a Database
There are 2 ways to interact with a database:
1. Import/Export an xlsx file from/to your database: <br/>
**Import**: please reference the previous section [Online Spreadsheet Editor](##Online Spreadsheet Editor) where you can import xlsx file via the UI or API. <br/>
**Export**: All information of a book model can be exported to an .xlsx file. You can store the file in a [BLOB](https://en.wikipedia.org/wiki/Binary_large_object) field of a table in a database.
2. Populate/Store cell data from/to your database: <br/>
**Populate data into spreadsheet**: When displaying data from the database, you can publish data into cells with `Range` setter methods into Keikai with predefined style.<br/>
**Store data into database**: Extract cell data or formulas you are interested with `Range` getter method and insert them into a corresponding database table. 

This project shows the 2nd way using `Range` API to save the cell data back to the database and publish a table's data to cells. The architecture is as follows:

![](images/database.png)

If you have any questions or have problem importing an Excel xlsx file, please send the file to [info@keikai.io](mailto:info@keikai.io) and request for support.


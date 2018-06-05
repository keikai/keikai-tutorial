# Keikai Tutorial
This project demonstrates [Keikai](https://keikai.io/) and how to build an application with its Java client API.

# Prerequisite
You need to download and start keikai server before running this project.
1. Please download keikai server at [https://keikai.io](https://keikai.io) according to your OS system
2. unzip the downloaded zip file
3. run the executable command inside the extracted folder:
* Mac/Linux

`./keikai`
* Windows

`keikai.bat`

After executing the command, you should see some messages like:
```
1:8888:2018-06-05 09:52:18.059549Z:keikai_dart_server:keikai_server:0
INFO: Keikai version: 1.0.0-alpha.0@jhsioate
...
INFO: Rikulo Stream Server 1.7.0 starting on 0.0.0.0:8888
...
```

Then keikai server starts up successfully at `localhost:8888`

## Options
### With different port and address
`keikai  —port=9999 —address=192.168.1.1`

### Help
For other options, you can check with the command:

`keikai --help`

# How to Run This Project
You can run this project with a gradle wrapper without installing anything in advanced. Just run the following command in your command line interface:
* Linux / Mac

`./gradlew appRun`

* Window

`gradlew appRun`

Then visit [http://localhost:8080/tutorial/editor](http://localhost:8080/tutorial/editor) with your browser.


# Supported Browser
IE 11+, Chrome, Firefox


# Supported File Format
Microsoft Excel Open XML Document (**.xlsx**) only


# Project Architecture
TODO a image


# Scenarios to Demonstrate
## Excel File Editor
You can upload an Excel file and edit it in a browser. Please visit [http://localhost:8080/tutorial/editor](http://localhost:8080/tutorial/editor) with you browser.

If you fail to import an xlsx file, please kindly send it to [info@zkoss.org](mailto:info@zkoss.org) 


## Working with a Database
It shows you how to import data from a database, and you can edit values in cells then save them back to the database. We just demonstrate one possible way, you can definitely load data from any other data repository by yourselves. 

Please visit [http://localhost:8080/tutorial/app](http://localhost:8080/tutorial/db) with you browser.

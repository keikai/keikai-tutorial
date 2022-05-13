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


When you see the following messages:
```
...
[INFO] Started Jetty Server
[INFO] Starting scanner at interval of 5 seconds.

```

Then visit http://localhost:8080/tutorial with your browser. After you have finished playing with Keikai, you can press `Ctrl+c` to stop the server.

# Demo case
1. copy 300 lines from xlsx file into keikai
2. all pasting values 

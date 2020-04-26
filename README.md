# G20 Bot - Stackelberg Leader Bot under Imperfect Information
## Compiling the Bot
The project is compiled and build using maven. To run from
intellij:
> Navigate to View | Tool Windows | Maven Projects <br> Navigate to
> Lifecycles to see the Available Maven Tasks

The following Tasks are Useful: 

> Clean: Wipes the target folder<br> Compile: Compiles code into classes
> <br> Install: Packages target code into Jar File

Double click each task to run it.  
## Running the Bot: Manual Setup
To run the bot, first set up the java rmi registry: <br><br>
`rmiregistry &`

Navigate to the g20Stackelberg Folder: <br>

`cd g20Stackelberg/`

Run the Platform GUI: <br>

`java -classpath poi-3.7-20101029.jar: 
-Djava.rmi.server.hostname=<HOST_IP> comp34120.ex2.Main &`

Navigate to the target folder: <br>

`cd target/`

Run our Leader Bot: <br>

`java -Djava.rmi.server.hostname=<HOST_IP> -cp g20Bot-1.0.jar
comp34120.ex2.Leader`


## Running the Bot: Automatic Setup

To run the bot, first set up the java rmi registry: <br><br>
`rmiregistry &`

Run the bash command: 

`sh run_bot.sh`



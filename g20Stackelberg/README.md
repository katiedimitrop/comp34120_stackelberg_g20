# G20 Bot - Stackelberg Leader Bot under Imperfect Information
## Compiling the Bot
The project is compiled and build using make: <br><br> `cd
g20Stackelberg` <br> `make`<br> <br>Make can also be used to run the
program run after build:<br> <br> `make run`
## Running the Bot
To run the bot, first set up the java rmi registry: <br><br>
`rmiregistry &`


Run the Platform GUI: <br>

`java -classpath poi-3.7-20101029.jar:
-Djava.rmi.server.hostname=<HOST_IP> comp34120.ex2.Main &`

Run our Leader Bot: <br>

`java -Djava.rmi.server.hostname=<HOST_IP> build/g20Bot.jar `




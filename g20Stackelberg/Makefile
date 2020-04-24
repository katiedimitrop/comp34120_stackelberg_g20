SOURCES = $(wildcard g20Bot/*.java src/*.java)
CLASSES = $(patsubst src/%.java,build/%.class,$(SOURCES))

.PHONY = ALL run

ALL: build/g20Bot.jar

build:
	mkdir build

build/g20Bot.jar: $(CLASSES)
	cd build; jar cfe g20Bot.jar g20Bot.Leader *

$(CLASSES): build $(SOURCES)
	javac $(SOURCES) -d build

run: build/g20Bot.jar
	rmiregistry &
	java -classpath poi-3.7-20101029.jar: -Djava.rmi.server.hostname=127.0.0.1 comp34120.ex2.Main &
	sleep 6
	java -Djava.rmi.server.hostname=127.0.0.1 -jar build/g20Bot.jar  &
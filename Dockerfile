FROM alpine:3.2
MAINTAINER Michael Hoglan <michaelh@tune.com>

RUN apk --update add openjdk7-jre font-misc-misc fontconfig ttf-ubuntu-font-family

ADD ./target/nPuzzle-1.0-SNAPSHOT.jar /opt/tune/nPuzzle/nPuzzle-1.0-SNAPSHOT.jar

ADD ./src/main/resources/puzzleConfiguration.yaml /opt/tune/nPuzzle/conf/puzzleConfiguration.yaml

EXPOSE 8080 8081
CMD /usr/bin/java -jar /opt/tune/nPuzzle/nPuzzle-1.0-SNAPSHOT.jar server /opt/tune/nPuzzle/conf/puzzleConfiguration.yaml

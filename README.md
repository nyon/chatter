# Chatter

Chatter is a simple real time chat application written in spring reactive 
to which clients can subscribe to receive messages. If a client sends a message to the server, all connected clients 
receive the message in real time.

⚠️ Disclaimer: This project is not meant to be used in production. Use at your own risk.

Live demo: http://nyon.de:3000

## General

When connecting with a client, you are given a random name (e.g. Reliable_Elephant). You can use the special command `/me` to query your username. To send a private message to a specific user you can use `/msg Other_Username My secret message`.  

## Architecture overview

![](./overview.drawio.png)

Every incoming message is transformed into a `Command` object, on which several `InputProcessor`s can be applied.
In this project 3 example processors are used:

- `AddSenderNameProcessor`: Extracts the corresponding user name from the current session and adds it to the `Command` instance
- `MetricsProcessors`: Generates simple metrics dealing with the message's content (e.g. message length)
- `TransformMeProcessor`: Detects the special `/me` command and adds the user's name to the text.

`InputProcessor`s can easily be created by implementing the `IInputProcessor` interface. All beans that implement this interface are automatically applied for every incoming message.

After processing messages are converted from the mutable `Command` object to the immutable `Message` object. This POJO
can be stored into your favourite messaging backend. In this application a simple messaging backend is implemented via the `ConcurrentLinkedQueue` in the `MessageService` bean.

From there every connected user receives this message. For every user all `OutputProcessor`s are applied. Currently there is only
one output processor for filtering out messages that are not meant to be received by the current connected user (`DiscardForeignMessages`).

`OutputProcessors` are costly, since they are applied for every connected user and message. Prefer `InputProcessor`s.

## Technologies used

This project uses Spring Boot 2.x Reactive to deliver messages for every user.
Java 11 and Maven is required to run this project. 

## Creating deliverables

You can simply call `./build.sh` to build all necessary artifacts in this project. `./build.sh` will:

- create a standalone java client jar in `client/target/client*.jar`, which can be run with `client/run.sh`
- create a docker image that can run the messaging service. You can see the docker image create when running `docker images chatter_backend`

## Running

Run `docker-compose up`.
If you want to use a web client open `http://localhost:3000` in your browser.
If you want to use the CLI client run `client/run.sh` in your terminal.

### Connect your local CLI client to the live demo

Run `client/run.sh ws://nyon.de:8080/chat`

## Testing

The project uses unit, integration and JMeter tests to keep its code quality high. All tests are run via Maven.

## Measure deliverables

To see all metrics that are generated by the service, you can run `docker-compose up` and open
`http://localhost:9090` to see a simple Prometheus UI. The live demo also provides the same interface. 

## Sources

The following sources have been used directly or indirectly to create this project:

- Official documentation: WebFlux WebSocketHandler example in the official documentation. See [here](https://docs.spring.io/spring-framework/docs/current/reference/html/web-reactive.html#webflux-websocket).
- StackOverflow answer: Using an embedded tomcat to create a websocket client. See [here](https://stackoverflow.com/questions/26452903/javax-websocket-client-simple-example).
- JS function for md5 from [here](https://stackoverflow.com/a/60467595)
- Material icons: https://mui.com/material-ui/material-icons/
- Self hosted monitoring example: https://www.baeldung.com/spring-boot-self-hosted-monitoring
- Uses [jmeter-websocket-samplers](https://bitbucket.org/pjtr/jmeter-websocket-samplers/downloads/)

## The future of chatter

In the future, this application could be extended in several ways:

- Technical aspects:
    - Change the logger backend from console to a more sophisticated one (Splunk or Kibana)
    - The current messaging implementation is in-memory. As such the application cannot scale well. However, if we change
      the messaging backend from a simple `ConcurrentLinkedQueue` to a more sophisticated system (e.g. Kafka), we could allow more
      users to connect.
    - The JMeter test is rudimentary and could be extended quite a bit. Currently it is only doing a lot of connections and checks whether the application crashes. In addition to that we could also check for response times to be lower than a specified threshold.
    - Error handling and logging is implemented in a very basic way. There are no trace ids, session information, etc. to allow efficient debugging.
    - The web client is very basic. There is no asset pipeline and such. 
- Potential features:
    - It should be communicated to all connected users, when a new user connects or disconnects.
    - The amount of connected users should be somehow communicated to the user.
    - More state of the art features in the web client (emoji selector, simple file upload)
    - More chat rooms than one
    - ... and a lot more
 

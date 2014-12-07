#QuizGen

Automatic quiz generation system

This project, built by Sachit Mishra (@smishra2) and Jonathan Yu (@jonathan-howe-yu) in fulfillment of our degree requirements at the University of Florida, is a web API and chrome extension for quiz generation. Use QuizGen to automatically generate quizzes based on factual text content. Use the chrome extension on any web page to 

## Tools Used

* Stanford CoreNLP
* Alchemy Language REST API
* Flask Python Server

To build, please have Java (1.6 and up) and Gradle 2.0 installed. Navigate to the QuizGen directory and type `gradle build`.

To run, use either:

1) `gradle run`

OR

2) `java -jar build/libs/QuizGen-1.0.jar`

The packaged .jar created by Gradle is runnable.

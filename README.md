QuizGen
======

Automatic quiz generation system

This project, built by Sachit Mishra (@smishra2) and Jonathan Yu (@jonathan-howe-yu) in fulfillment of our degree requirements at the University of Florida, is a web API and chrome extension for quiz generation. Use QuizGen to automatically generate quizzes based on factual text content. Use the chrome extension on any web page to 

## Tools Used

* Stanford CoreNLP
* Alchemy Language REST API
* Flask Python Server
* AngularJS (for data binding and DOM manipulation in chrome extension)

## Server endpoints

* 'GET /jstest?=websiteURL'
  Returns a sample JSON response, ignores the websiteURL.
* 'GET /quizme?=websiteURL'
  This is the main API call. Returns JSON encoded questions array (see below).

  Sample Response:
  
  {
    "questions": [
      {
        "question": "What is the question?",
        "sentence": "This is the sentence from which this question originated."
      },
      {
        "question": "Was Amy a good student?",
        "sentence": "Amy was a good student."
      }
    ]
  }
  
## Building

To build, please have Java (1.6 and up) and Gradle 2.0 installed. Navigate to the QuizGen directory and type `gradle build`.

To run, use either:

1) `gradle run`

OR

2) `java -jar build/libs/QuizGen-1.0.jar`

The packaged .jar created by Gradle is runnable.

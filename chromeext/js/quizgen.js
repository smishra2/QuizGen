// // This script will pull up the quiz questions for the user by reading
// // the current URL and sending request. It then parses the response JSON
// // for the questions. Based on chrome extension tutorial.

// var quizGenerator = {
  // /**
   // * QuizGen URL that will give us of whatever we're looking for.
   // * @type {string}
   // * @private
   // */
  // quizGenURL: 'http://127.0.0.1:5000',
  
  // /**
   // * Sends an XHR GET request to grab quiz of lots and lots of kittens. The
   // * XHR's 'onload' event is hooks up to the 'showQuiz_' method.
   // *
   // * @public
   // */
  // requestQuiz: function(tablink) {
    // var req = new XMLHttpRequest();
    // req.open("GET", this.quizGenURL + 
                    // "/jstest?websiteURL=" + encodeURIComponent(tablink), true);
    // req.onload = this.showQuiz.bind(this);
    // req.send(null);
  // },

  // /**
   // * Handle the 'onload' event of our quiz XHR request, generated in
   // * 'requestQuiz'.
   // *
   // * @param {ProgressEvent} e The XHR ProgressEvent.
   // * @private
   // */
  // showQuiz: function (e) {
    // var questions = JSON.parse(e.target.responseText);
    // console.log(questions);
  // }
// }

  // // /**
   // // * Given a photo, construct a URL using the method outlined at
   // // * http://www.flickr.com/services/api/misc.urlKittenl
   // // *
   // // * @param {DOMElement} A kitten.
   // // * @return {string} The kitten's URL.
   // // * @private
   // // */
  // // constructKittenURL_: function (photo) {
    // // return "http://farm" + photo.getAttribute("farm") +
        // // ".static.flickr.com/" + photo.getAttribute("server") +
        // // "/" + photo.getAttribute("id") +
        // // "_" + photo.getAttribute("secret") +
        // // "_s.jpg";
  // // }
// };

// (function(){
  // var app = angular.module('quiz', []);
  
  // app.controller('QuizController', function(){
    // this.questions = quizquestions;
  // });
  
  // var quizquestions = [
    // {question: 'Who are you?'},
    // {question: 'What are you?'}
  // ];
// })();

// Run our quiz generation script as soon as the document's DOM is ready.
// document.addEventListener('DOMContentLoaded', function () {

  // (function(){
    // var app = angular.module('quiz', []);
    
    // app.controller('QuizController', function(){
      // this.questions = quizquestions;
    // });
    
    // var quizquestions = [
      // {question: 'Who are you?'},
      // {question: 'What are you?'}
    // ];
  // })();

  // var tablink;

  // //chrome.tabs.query({'active': true, 'lastFocusedWindow': true}, function (tabs) {
  // //  this.tablink = tabs[0].url;
    // //quizGenerator.requestQuiz(this.tablink);
  // //});

// });



// (function() {
  // var app = angular.module('quizapp', []);
  
  // window.quizquestions = [{'question': 'Loading...'}, 
                  // {'question': 'Still loading...'}];
                  
  // app.controller('QuizController', ['$scope', function($scope) {
    
    // var quizGenerator = {
    
      // self: this,
  
      // quizquestions: [{'question': '1'}],
        
      // getQuestions: function() {
          // return this.quizquestions;
      // },
      
      // getQuiz: function() {
        // this.getURL();
        //return [{question: 'Why are you?'}];
      // },
        
      // getURL: function() {
        
        // $scope.scquestions = [{'question': '1Loading...'}, 
                              // {'question': '2Still loading...'}];
        // chrome.tabs.query({'active': true, 'lastFocusedWindow': true}, 
                          // function (tabs) {
                            // quizGenerator.requestQuiz(tabs[0].url);
                          // });
      // },
        
      // /**
       // * QuizGen URL that will give us of whatever we're looking for.
       // * @type {string}
       // * @private
       // */
      // quizGenURL: 'http://127.0.0.1:5000',
      
      // /**
       // * Sends an XHR GET request to grab quiz. The
       // * XHR's 'onload' event is hooks up to the 'showQuiz_' method.
       // *
       // * @public
       // */
      // requestQuiz: function(tablink, scope) {
        // scope.scquestions = [{'question': '1Loading...'}, 
                              // {'question': '2Still loading...'}];
        // var req = new XMLHttpRequest();
        // req.open("GET", this.quizGenURL + 
                 // "/jstest?websiteURL=" + encodeURIComponent(tablink), true);
        // req.onload = this.showQuiz.bind(this);
        // req.send(null);
      // },
         
      // /**
       // * Handle the 'onload' event of our quiz XHR request, generated in
       // * 'requestQuiz'.
       // *
       // * @param {ProgressEvent} e The XHR ProgressEvent.
       // * @private
       // */
      // showQuiz: function (e) {
        //var questions = JSON.parse(e.target.responseText);
        //console.log(questions);
        // console.log(e.target.responseText);
        // console.log(JSON.parse(e.target.responseText));
        // quizquestions = JSON.parse(e.target.responseText);
        // this.quizquestions = [{'question': '1Loading...'}, 
                              // {'question': '2Still loading...'}];
        // self.quizquestions = [{'question': '1Loading...'}, 
                              // {'question': '2Still loading...'}];
        // $scope.scquestions = [{'question': '1Loading...'}, 
                            // {'question': '2Still loading...'}];
        // console.log(this);
        // console.log(this.getQuestions());
        // tomato.question = JSON.parse(e.target.responseText);
      // }
         
    // }
    
    // var tomato = {
        // question: 1
    // }

    // quizGenerator.getQuiz($scope);
    
    // this.questions = quizGenerator.getQuestions();
    
    // $scope.scquestions = tomato.question;
    
  // }]);
  

// })();

(function() {
  var self = this;

  var app = angular.module('quizgen', []);
  
  var quizGenURL = 'http://127.0.0.1:5000'
  
  var endpoint = '/jstest';
  
  var tabURL = '';
  
  app.controller('QuizController', function($scope, $http) {
    $scope.questions = [{'question': 'Quiz loading...'}];
    chrome.tabs.query({'active': true, 'lastFocusedWindow': true}, 
                      function (tabs) {
                        tabURL = tabs[0].url;
                        
                        $http.get(quizGenURL + endpoint + '?websiteURL=' + tabURL).
                            success(function(data, status, headers, config) {
                              console.log(JSON.stringify(data));
                              $scope.questions = data;
                              // TODO: Error code for non 200 status
                            }).
                            error(function(data, status, headers, config) {
                              $scope.questions = [{'Error:': 'There has been an error loading your questions'}];
                              $scope.$apply(); // TODO: Do we need this?
                            });
                      });
  });

})();

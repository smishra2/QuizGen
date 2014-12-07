(function() {
  
  var self = this;

  var app = angular.module('quizgen', []);
  
  var quizGenURL = 'http://127.0.0.1:5000'
  
  var endpoint = '/quizme';
  
  var tabURL = '';
  
  var myquestions = [{'string': "Loading..."}];
  
  app.controller('QuizController', function($scope, $http) {
    $scope.loading = "Loading...";
    $scope.questions = myquestions;
    chrome.runtime.onMessage.addListener(function(request, sender, sendResponse) {
      $scope.questions = [{'string': 'Loading'}];
      $http.get(quizGenURL + endpoint + '?websiteURL=' + request.websiteURL).
        success(function(data, status, headers, config) {
          console.log(data);
          $scope.results = data;
          $scope.loading = "";
        }).
        error(function(data, status, headers, config) {
          $scope.questions = [{'Error:': 'There has been an error.'}];
          $scope.loading = "Sorry, there was an error loading your quiz.";
        });
    });
  });

})();

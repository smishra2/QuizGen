#import urllib.request
#urllib.request.p
#print(urllib.request.urlopen("http://www.cnn.com/2014/11/05/politics/elections-wednesday/index.html?hpt=hp_t1").read())
#urllib.request.urlcleanup()
#import http.client
#conn = http.client.HTTPConnection("https://readability.com")
#conn.request("GET", "/api/content/v1/parser?url=http://www.cnn.com/2014/11/05/politics/elections-wednesday/index.html?hpt=hp_t1")
from TextExtractor import TextExtractor
from flask import Flask
from flask import request
app = Flask(__name__)

@app.route("/")
def hello():
    return "Welcome to QuizGen."

@app.route("/quizme", methods=['GET'])
def extract():
    print("Received quizme GET request, website URL = " +
          str(request.args.get('websiteURL', '')))
    extractor = TextExtractor(request.args.get('websiteURL', ''))
    extractor.extract()
    return "something"

@app.route("/test", methods=['GET'])
def test():
    print(request.args.get('testID'))
    return "something"

if __name__ == "__main__":
    app.run()
from TextExtractor import TextExtractor
from flask import Flask, request, jsonify
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
    
@app.route("/jstest", methods=['GET'])
def jstest():
    print("Received JSTest")
    print("WebsiteURL is " + str(request.args.get('websiteURL', '')))
    return ("{\n" +
           "    \"question\": [\n" +
           "        \"What are you?\",\n" +
           "        \"Who are you?\",\n" +
           "        \"Why are you?\"\n" +
           "    ]" +
           "}")

if __name__ == "__main__":
    app.run()
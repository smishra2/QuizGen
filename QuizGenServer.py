import subprocess
from TextExtractor import TextExtractor
from flask import Flask, request

app = Flask(__name__)

@app.route("/")
def hello():
    return "Welcome to QuizGen."

@app.route("/quizme", methods=['GET'])
def extract():
    #Extract text
    print("Received quizme GET request, website URL = " +
          str(request.args.get('websiteURL', '')))
    extractor = TextExtractor(request.args.get('websiteURL', ''))
    extractor.extract()

    #Peform java processing
    subprocess.check_call("gradle run", shell=True)

    # Return result from file
    f = open('res/Output', 'r')
    result = f.read()
    f.close()
    return result

@app.route("/test", methods=['GET'])
def test():
    print(request.args.get('testID'))
    return "something"
    
@app.route("/jstest", methods=['GET'])
def jstest():
    print("Received JSTest")
    print("WebsiteURL is " + str(request.args.get('websiteURL', '')))
    return ("{\n" +
            "    \"questions\": [\n"
            "        {\n"
            "            \"question\": \"Who are you?\",\n"
            "            \"sentence\": \"I am me.\"\n"
            "        },\n"
            "        {\n"
            "            \"question\": \"Are you a man?\",\n"
            "            \"sentence\": \"I am a man.\"\n"
            "        }\n"
            "    ]\n"
            "}")

if __name__ == "__main__":
    app.run()
__author__ = 'Sachit'

import urllib.parse
import urllib.request
import json

class TextExtractor:

    ALCHEMYURL = 'http://access.alchemyapi.com/calls/url'
    ALCHEMYENDPOINT = "/URLGetText"
    APIKEY = "d83d5968892fd5865f3cc74e5e6bfac124db9267"
    OUTPUTMODE = "json"  

    def __init__(self, website):
        self.website = website

    def extract(self):
        print("TextExtractor extracting " + self.website)

        # Get text from Alchemy
        data = {}
        data['url'] = self.website
        data['apikey'] = self.APIKEY
        data['outputMode'] = self.OUTPUTMODE
        url_values = urllib.parse.urlencode(data)
        full_url = self.ALCHEMYURL + self.ALCHEMYENDPOINT + '?' + url_values
        response = urllib.request.urlopen(full_url)
        try:
            json_response = response.read().decode('utf-8').strip()
            decoded_json = json.loads(json_response)
        except (ValueError, KeyError, TypeError) as err:
            print("JSON format error " + err.reason)

        # Put text in file
        f = open('res/Input', 'w')
        f.write(decoded_json['text'])
        f.close()

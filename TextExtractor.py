__author__ = 'Sachit'

import http.client
import urllib.parse

class TextExtractor:

    def __init__(self, website):
        self.website = website

    def extract(self):
        print("TextExtractor extracting " + self.website)
        #conn = http.client.HTTPConnection("http://boilerpipe-web.appspot.com")
        #params = urllib.parse.urlencode({'url': self.website,
        #                                 'output': 'text',
        #                                 'extractor': 'ArticleExtractor'})
        #conn.request("GET", "/extract", params)
        conn = http.client.HTTPConnection("127.0.0.1:5000")
        params = urllib.parse.urlencode({'testID': 3})
        conn.request("GET", "/test", params)
        response = conn.getresponse()
        print(response.status)
        conn.close()
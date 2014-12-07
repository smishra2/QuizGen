(function() {
    chrome.browserAction.onClicked.addListener(function(oldtab) {
      websiteurl = oldtab.url;
      chrome.tabs.create({'url': chrome.extension.getURL('../default.html')}, function(tab) {
        // Tab opened.
        chrome.tabs.sendMessage(tab.id, {websiteURL: websiteurl}, function(response) {
          console.log(response);
        });
      });
    });
})();
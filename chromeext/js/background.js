(function() {
    chrome.browserAction.onClicked.addListener(function(tab) {
      chrome.tabs.create({'url': chrome.extension.getURL('../default.html')}, function(tab) {
        // Tab opened.
      });
    });
})();
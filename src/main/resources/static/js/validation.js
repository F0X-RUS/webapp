var title  = document.getElementByID('title');
var content  = document.getElementByID('content');
var error1 = title;
var error2 = content;

function addEvent(element, event, callback) {
    var previousEventCallBack = element["on"+event];
    element["on"+event] = function (e) {
        var output = callback(e);
        if (output === false) return false;
        if (typeof previousEventCallBack === 'function') {
            output = previousEventCallBack(e);
            if(output === false) return false;
        }
    }
};

addEvent(title, "input", function () {
    var test = title.value.length === 5;
    if (test) {
        title.className = "valid";
        error1.innerHTML = "";
        error1.className = "error";
    } else {
        title.className = "invalid";
    }
});
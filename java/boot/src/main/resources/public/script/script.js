/**
 * Created by liurui on 15/7/1.
 */

function calljs(script, ui) {
    $.ajax("script.js?script=" + script + "&ui=" + ui, {
        async: false
    });

    //$.getScript("script.js?script=" + script + "&ui=" + ui,
    //    function () {
    //    });

    return eval("main" + script + "()");
}

function getValue(uuid, 名称) {
    return getSWF('swf')["getValue" + uuid](名称);
}

function setValue(uuid, 名称, value) {
    return getSWF('swf')["setValue" + uuid](名称, value);
}

function getSWF(movieName) {
    //return $('#swf')

    if (navigator.appName.indexOf("Microsoft") != -1) {
        return window[movieName];
    }
    else {
        return document[movieName];
    }
}
function post(path, params, method) {
    method = method || "post"; // Set method to post by default if not specified.

    let url = choosePath(path);
    let remainParams = location.search.replace("?", "");
    let parsedParams = parse_query_string(remainParams);

    let form = document.createElement("form");
    form.setAttribute("method", method);
    form.setAttribute("action", url);

    for(let key in params) {
        if(params.hasOwnProperty(key)) {
            let hiddenField = document.createElement("input");
            hiddenField.setAttribute("type", "hidden");
            hiddenField.setAttribute("name", key);
            hiddenField.setAttribute("value", params[key]);

            form.appendChild(hiddenField);
        }
    }
    for(let key in parsedParams) {
        if(parsedParams.hasOwnProperty(key)) {
            let hiddenField = document.createElement("input");
            hiddenField.setAttribute("type", "hidden");
            hiddenField.setAttribute("name", key);
            hiddenField.setAttribute("value", parsedParams[key]);

            form.appendChild(hiddenField);
        }
    }

    document.body.appendChild(form);
    form.submit();
}

function removeParams(sParam)
{
    var url = window.location.href.split('?')[0]+'?';
    var sPageURL = decodeURIComponent(window.location.search.substring(1)),
        sURLVariables = sPageURL.split('&'),
        sParameterName,
        i;

    for (i = 0; i < sURLVariables.length; i++) {
        sParameterName = sURLVariables[i].split('=');
        if (sParameterName[0] != sParam) {
            url = url + sParameterName[0] + '=' + sParameterName[1] + '&'
        }
    }
    return url.substring(0,url.length-1);
}

function choosePath(path) {
    let url;
    if(location.href.includes("/cinema")){
        url = location.origin + "/cinema/films" + path + window.location.search;
    } else {
        url = location.origin + "/films" + path + window.location.search;
    }
    return url;
}

function parse_query_string(query) {
    var vars = query.split("&");
    var query_string = {};
    for (var i = 0; i < vars.length; i++) {
        var pair = vars[i].split("=");
        var key = decodeURIComponent(pair[0]);
        var value = decodeURIComponent(pair[1]);
        // If first entry with this name
        if (typeof query_string[key] === "undefined") {
            query_string[key] = decodeURIComponent(value);
            // If second entry with this name
        } else if (typeof query_string[key] === "string") {
            var arr = [query_string[key], decodeURIComponent(value)];
            query_string[key] = arr;
            // If third or later entry with this name
        } else {
            query_string[key].push(decodeURIComponent(value));
        }
    }
    return query_string;
}

//let generalURL = window.location.toString();
function setGetParam(key, value)
{
    let generalURL = window.location.toString();
    let url = new URL(generalURL);
    url.searchParams.set(key,value);
    return url;
}

function renewPage(pagePath) {
    window.location.assign(pagePath);
    return false;
}

function deleteGetParam(key) {
    let generalURL = window.location.toString();
    let url = new URL(generalURL);
    url.searchParams.delete(key);
    return url;
}

function go_back(){
    window.location = document.referrer;
}





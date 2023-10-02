// JavaScript source code
function deleteRequest(suffix, ID) {
    return new Promise(function (resolve, reject) {
        var xhr = new XMLHttpRequest();
        proxyuri = "https://cors-anywhere.herokuapp.com/http://web.socem.plymouth.ac.uk/IntProj/PRCS252J/api/";
        xhr.open("DELETE", proxyuri + suffix + "/" + ID, true);
        xhr.setRequestHeader('Content-Type', 'application/json/');
        xhr.send();
        xhr.onreadystatechange = function () {
            if (this.readyState == 4) {
                if (this.status == 200) {
                    resolve(true);
                } else if (this.status > 300) {
                    reject(false);
                }
            }
        }
    });
}

function postRequest(suffix, data) {
    return new Promise(function (resolve, reject) {

        var xhr = new XMLHttpRequest();
        proxyuri = "https://cors-anywhere.herokuapp.com/http://web.socem.plymouth.ac.uk/IntProj/PRCS252J/api/";
        xhr.open("POST", proxyuri + suffix, true);
        xhr.setRequestHeader('Content-Type', 'application/json');
        xhr.send(JSON.stringify(data));
        xhr.onreadystatechange = function (storage) {
            if (this.readyState == 4) {
                if (this.status == 200 || this.status == 201) {
                    resolve(true);
                } else if (this.status > 300) {
                    reject(false);
                }
            };
        }
    });
};

function putRequest(suffix, data) {
    return new Promise(function (resolve, reject) {
        var xhr = new XMLHttpRequest();
        proxyuri = "https://cors-anywhere.herokuapp.com/http://web.socem.plymouth.ac.uk/IntProj/PRCS252J/api/";
        xhr.open("PUT", proxyuri + suffix, true);
        xhr.setRequestHeader('Content-Type', 'application/json');
        xhr.send(JSON.stringify(data));
        xhr.onreadystatechange = function (storage) {
            if (this.readyState == 4) {
                if (this.status == 200 || this.status == 204) {
                    resolve(true);
                } else if (this.status > 400) {
                    reject(false);
                }
            };
        }
    });
};

function getRequest(suffix) {
    //promise implies something will be returned(lets async work)
    return new Promise(function (resolve, reject) {

        var xhr = new XMLHttpRequest();

        xhr.open("GET", "http://web.socem.plymouth.ac.uk/IntProj/PRCS252J/api/" + suffix, true);
        xhr.send();

        xhr.onreadystatechange = function (storage) {
            if (this.readyState == 4) {
                if (this.status == 200) {
                    resolve(JSON.parse(this.responseText));
                } else if (this.status > 300) {
                    alert("something went wrong, try reloading the page");
                    reject();
                }
            };
        }
    })

}

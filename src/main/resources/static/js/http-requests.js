function sendXmlHttpRequest(method, url, data, token) {
    return new Promise(function (resolve, reject) {
        try {
            const req = new XMLHttpRequest();
            req.open(method, url, true);
            req.setRequestHeader('Content-type', 'application/json; charset=utf-8');
            if (token) {
                req.setRequestHeader(token.header, token.content);
            }
            req.onreadystatechange = function () {
                if (this.readyState === 4) {
                    const jsonResult = JSON.parse(this.response);
                    if (jsonResult.error) {
                        reject(jsonResult);
                    }
                    resolve(jsonResult);
                }
            };
            req.send(data);
        } catch (e) {
            reject(e);
        }
    });
}

function createJsonFromInputs(inputs) {
    const data = {};
    inputs.forEach(function (i) {
        data[i.name] = i.value;
    });
    return data;
}

export {sendXmlHttpRequest, createJsonFromInputs}
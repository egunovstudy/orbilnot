import axios from 'axios';


const _saveUser = (token, user) => {
    const headers = {
        Accept: "application/json",
        Authorization: "Bearer " + token
    };

    return axios.put("/api/users/info", user, { headers });
}

export const saveUser = (token, user) => {
    return _saveUser(token, user)
}


const _getUser = (token) => {
    const headers = {
        Accept: "application/json",
        Authorization: "Bearer " + token,
        "Cache-Control": "no-cache",
        Pragma: 'no-cache',
        Expires: 0
    }

    return axios.get("/api/users/info",{ headers });
}

export const getUser = (token) => {
    return _getUser(token)
}
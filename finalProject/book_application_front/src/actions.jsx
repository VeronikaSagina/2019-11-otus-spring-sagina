import {API_URL} from "./ApiPath";
import axios from "axios";

export const userFirstAuth = user => {
    return axios.post(`${API_URL}/auth/first-login`, user)
        .then(data => {
            localStorage.setItem("token", data.data.jwt);
            console.log("token " + localStorage.getItem("token"))
        })
};

const loginUser = userObj => ({
    type: 'LOGIN_USER',
    payload: userObj
});

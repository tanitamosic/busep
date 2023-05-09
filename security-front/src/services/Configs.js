import axios from 'axios'
import { getToken, getTokenWithNoQuotes } from './utils/AuthService';

export var getApiCall = () =>{
    const token = getTokenWithNoQuotes();
    axios.defaults.headers.common['Access-Control-Allow-Origin'] = 'http://localhost:8081';
    return axios.create({
        baseURL: "http://localhost:8081",
        headers:  {"Authorization" : `Bearer ` + token,
                    "Content-Type": "application/json"}
    });

    // headers:  {"Authorization" : `Bearer ${token}`,
    //                 "Content-Type": "application/json"}

} 

export var getApiCallUrlEncoded = () =>{
    const token = getTokenWithNoQuotes();
    return axios.create({
        baseURL: "http://localhost:8081",
        headers:  {"Authorization" : `Bearer ` + token,
                    'Content-Type': 'application/x-www-form-urlencoded'}
    });

    // headers:  {"Authorization" : `Bearer ${token}`,
    //                 "Content-Type": "application/json"}

} 





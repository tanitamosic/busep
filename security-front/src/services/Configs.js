import axios from 'axios'
import { getToken, getTokenWithNoQuotes, getCookie } from './utils/AuthService';

export var getApiCall = () =>{
    const token = getTokenWithNoQuotes();
    return axios.create({
        baseURL: "http://localhost:8081",
        headers:  {"Authorization" : `Bearer ` + token,
                    "Content-Type": "application/json",
                "Cookiee": getCookie()}
    });
} 

export var getApiCallUrlEncoded = () =>{
    const token = getTokenWithNoQuotes();
    return axios.create({
        baseURL: "http://localhost:8081",
        headers:  {"Authorization" : `Bearer ` + token,
                    'Content-Type': 'application/x-www-form-urlencoded'}
    });
} 

export var getApiCallUrlEncodedNoBearer = () =>{
    // const token = getTokenWithNoQuotes();
    return axios.create({
        baseURL: "http://localhost:8081",
        headers:  {
                    'Content-Type': 'application/x-www-form-urlencoded'
                }
    });
} 





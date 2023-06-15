import axios from 'axios'
import { getToken, getTokenWithNoQuotes } from './utils/AuthService';

export var getApiCall = () =>{
    const token = getTokenWithNoQuotes();
    return axios.create({
        baseURL: "http://localhost:8081",
        headers:  {"Authorization" : `Bearer ` + token,
                    "Content-Type": "application/json",
                "Cookiee": sessionStorage.getItem("cookie")}
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

export var getApiCallUrlEncodedNoBearer = () =>{
    // const token = getTokenWithNoQuotes();
    return axios.create({
        baseURL: "http://localhost:8081",
        headers:  {
                    'Content-Type': 'application/x-www-form-urlencoded'}
    });

    // headers:  {"Authorization" : `Bearer ${token}`,
    //                 "Content-Type": "application/json"}

} 





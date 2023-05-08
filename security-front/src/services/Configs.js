import axios from 'axios'
import { getToken, getTokenWithNoNavodnici } from './utils/AuthService';

export var getApiCall = () =>{
    const token = getTokenWithNoNavodnici();
    return axios.create({
        baseURL: "http://localhost:8081",
        headers:  {"Authorization" : `Bearer ` + token,
                    "Content-Type": "application/json"}
    });

    // headers:  {"Authorization" : `Bearer ${token}`,
    //                 "Content-Type": "application/json"}

} 

export var getApiCallUrlEncoded = () =>{
    const token = getTokenWithNoNavodnici();
    return axios.create({
        baseURL: "http://localhost:8081",
        headers:  {"Authorization" : `Bearer ` + token,
                    'Content-Type': 'application/x-www-form-urlencoded'}
    });

    // headers:  {"Authorization" : `Bearer ${token}`,
    //                 "Content-Type": "application/json"}

} 





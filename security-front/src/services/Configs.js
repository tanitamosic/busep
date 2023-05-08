import axios from 'axios'
import { getToken } from './utils/AuthService';

export var getApiCall = () =>{
    const token = getToken();
    return axios.create({
        baseURL: "http://localhost:8081",
        headers:  {"Authorization" : `Bearer ${token}`,
                    "Content-Type": "application/json"}
    });

    // headers:  {"Authorization" : `Bearer ${token}`,
    //                 "Content-Type": "application/json"}

} 

export var getApiCallUrlEncoded = () =>{
    const token = getToken();
    return axios.create({
        baseURL: "http://localhost:8081",
        headers:  {"Authorization" : `Bearer ${token}`,
                    'Content-Type': 'application/x-www-form-urlencoded'}
    });

    // headers:  {"Authorization" : `Bearer ${token}`,
    //                 "Content-Type": "application/json"}

} 





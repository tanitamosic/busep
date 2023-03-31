import axios from 'axios'
// import { getToken } from './AuthService/AuthService';


export var getApiCall = () =>{
    // const token = getToken();
    return axios.create({
        baseURL: "http://localhost:8000",
        headers:  {"Content-Type": "application/json"}
    });

    // headers:  {"Authorization" : `Bearer ${token}`,
    //     "Content-Type": "application/json"}
} 

//export var api = getApiCall();





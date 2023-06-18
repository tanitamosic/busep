import {getApiCall} from "../Configs.js"
import { getLoggedUserEmail, getRole } from "../utils/AuthService.js";
import { checkEmailInput, checkNumInput } from "../utils/InputValidation.js";

// TODO 
export async function getAllObjects(){
    try {
        const responseData = await getApiCall().get('/home/get-all-homes');
        return responseData;
    } catch (err) {
        console.log(err.message);
        return err.message
    }
  }

  export async function getObject(houseId){
    try {
        const responseData = await getApiCall().get('/home/get-home/' + houseId);
        return responseData;
    } catch (err) {
        console.log(err.message);
        return err.message
    }
  }

  // TODO 
  export async function getUserObjects(userEmail){
    try {
        const responseData = await getApiCall().get('/home/get-all-homes-by-user/' +  userEmail);
        return responseData;
    } catch (err) {
        console.log(err.message);
        return err.message
    }
  }

  // not working completely
  export async function removeObjectRequest(id){
    try {
        const responseData = await getApiCall().delete('/home/delete-home-' + id);
        return responseData;
    } catch (err) {
        console.log(err.message);
        return err.message
    }
  }

  // TODO 
  export async function sendObjectCreationRequest(objectJson){
    try {
        const responseData = await getApiCall().post('/home/create-home', objectJson);
        return responseData;
    } catch (err) {
        console.log(err.message);
        return err.message
    }
  }

  export async function sendObjectUpdateRequest(objectJson){
    try {
        const responseData = await getApiCall().post('/home/update-home', objectJson);
        return responseData;
    } catch (err) {
        console.log(err.message);
        return err.message
    }
  }

  export async function sendRemoveRenterRequest(houseId){
    try {
        const responseData = await getApiCall().post('/home/remove-renter/' + houseId);
        return responseData;
    } catch (err) {
        console.log(err.message);
        return err.message
    }
  }

  export async function sendDeviceCreationRequest(deviceJson){
    try {
        const responseData = await getApiCall().post('/home/add-device', deviceJson);
        return responseData;
    } catch (err) {
        console.log(err.message);
        return err.message
    }
  }

  export async function removeDeviceRequest(deviceId){
    try {
        const responseData = await getApiCall().post('/home/delete-device/' + deviceId);
        return responseData;
    } catch (err) {
        console.log(err.message);
        return err.message
    }
  }

  export async function getPreviosLogs(filterDto){
    try {
        let responseData;

        if (getRole() === "admin"){
            responseData = await getApiCall().post('/home/logs', filterDto);
        } else {
            responseData = await getApiCall().post('/home/logs/' + getLoggedUserEmail(), filterDto);
        }

        return responseData;
    } catch (err) {
        console.log(err.message);
        return err.message
    }
  }

  export async function postCreateAlarmRequest(alarmDto){
    try {
        const responseData = await getApiCall().post('/device/custom-alarm', alarmDto);
        return responseData;
    } catch (err) {
        console.log(err.message);
        return err.message
    }
  }

  export async function postReportRequest(reportDto, isAdmin){
    try {
        let responseData;

        if (isAdmin){
            if (reportDto.reportSource === ""){
                responseData = await getApiCall().post('/report/all-houses', reportDto);    
            } else if (reportDto.reportSource === "0"){
                responseData = await getApiCall().post('/report/all-devices', reportDto);    
            } else if (checkEmailInput(reportDto.reportSource)){
                responseData = await getApiCall().post('/report/user-houses', reportDto);    
            } else if (checkNumInput(reportDto.reportSource)){
                responseData = await getApiCall().post('/report/house-devices', reportDto);    
            }
        } else {
            if (reportDto.reportSource === "0"){ // reports for all houses
                responseData = await getApiCall().post('/report/user-houses' + getLoggedUserEmail(), reportDto);  
            } else {
                responseData = await getApiCall().post('/report/user-house/' + getLoggedUserEmail(), reportDto);  
            }
        }
        
        return responseData;
    } catch (err) {
        console.log(err.message);
        return err.message
    }
  }
import {getApiCall} from "../Configs.js"

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
        const responseData = await getApiCall().get('/home/get-all-homes-by-user-' +  userEmail);
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

  export async function sendDeviceCreationRequest(deviceJson){
    try {
        const responseData = await getApiCall().post('/home/add-device', deviceJson);
        return responseData;
    } catch (err) {
        console.log(err.message);
        return err.message
    }
  }
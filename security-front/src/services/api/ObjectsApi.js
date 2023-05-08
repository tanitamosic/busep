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
  export async function removeObject(id){
    try {
        let objectId = {id};
        const responseData = await getApiCall().post('/home/delete-home-', objectId);
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
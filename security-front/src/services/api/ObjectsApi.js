import {getApiCall} from "../Configs.js"

// TODO 
export async function getAllObjects(){
    try {
        const responseData = await getApiCall().get('/objects');
        return responseData;
    } catch (err) {
        console.log(err.message);
        return err.message
    }
  }

  // TODO 
  export async function getUserObjects(userEmail){
    try {
        const responseData = await getApiCall().get(`/objects/user/` +  userEmail);
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
        const responseData = await getApiCall().post(`/objects/remove`, objectId);
        return responseData;
    } catch (err) {
        console.log(err.message);
        return err.message
    }
  }

  // TODO 
  export async function sendObjectCreationRequest(objectJson){
    try {
        const responseData = await getApiCall().post(`/objects/createNew`, objectJson);
        return responseData;
    } catch (err) {
        console.log(err.message);
        return err.message
    }
  }
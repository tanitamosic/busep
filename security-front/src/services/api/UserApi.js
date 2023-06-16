import { getApiCall} from "../Configs.js";


export async function getUserByEmail(email){
    try {
        const responseData = await getApiCall().get(`/user/info/` + email);
        return responseData;
    } catch (err) {
        console.log(err.message);
        return err.message
    }
}

export async function getAllClients(){
    try {
        const responseData = await getApiCall().get('/user/all-clients');
        return responseData;
    } catch (err) {
        console.log(err.message);
        return err.message
    }
  }

export async function postClientFilterRequest(){
    try {
        const responseData = await getApiCall().get('/user/all-clients');
        return responseData;
    } catch (err) {
        console.log(err.message);
        return err.message
    }
  }

  export async function sendFilterClientsRequest(filterParams){
    try {
        const responseData = await getApiCall().post('/user/filter-clients', filterParams);
        return responseData;
    } catch (err) {
        console.log(err.message);
        return err.message
    }
  }
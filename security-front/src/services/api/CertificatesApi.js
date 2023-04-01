import {getApiCall} from "../Configs.js"

export async function getRequests(){
    try {
        // TODO fix url
        const responseData = await getApiCall().get(`/csr/requests`);
        return responseData;
    } catch (err) {
        console.log(err.message);
        return err.message
    }
  }

  export async function declineRequest(declineRequest){
    try {
        // TODO fix url and where email? in body or just url (now in body)
        const responseData = await getApiCall().post(`/csr/requests/decline/`, declineRequest);
        return responseData;
    } catch (err) {
        console.log(err.message);
        return err.message
    }
  }

  export async function acceptRequest(acceptRequest){
    try {
        // TODO fix url
        const responseData = await getApiCall().post(`/csr/requests/accept/`, acceptRequest);
        return responseData;
    } catch (err) {
        console.log(err.message);
        return err.message
    }
  }
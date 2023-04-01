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


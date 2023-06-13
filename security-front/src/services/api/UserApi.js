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
import {getApiCall} from "../Configs.js"

export async function getRequests(id){
    try {
        const responseData = await getApiCall().get('/admin/get-csr-' + id);
        return responseData;
    } catch (err) {
        console.log(err.message);
        return err.message
    }
  }

  export async function declineRequest(declineRequest){
    try {
        const responseData = await getApiCall().post(`/admin/decline-request`, declineRequest);
        return responseData;
    } catch (err) {
        console.log(err.message);
        return err.message
    }
  }

  export async function acceptRequest(acceptRequest){
    try {
        const responseData = await getApiCall().post(`/admin/accept-request`, acceptRequest);
        return responseData;
    } catch (err) {
        console.log(err.message);
        return err.message
    }
  }

  export async function getCertificates(){
    try {
        const responseData = await getApiCall().get(`/admin/get-all-valid-certificates`);
        return responseData;
    } catch (err) {
        console.log(err.message);
        return err.message
    }
  }

  export async function isCertificateValid(id){
    try {
        const responseData = await getApiCall().get(`/admin/check-certificate-validity-` + id);
        return responseData;
    } catch (err) {
        console.log(err.message);
        return err.message
    }
  }

  export async function deactivateCertificate(id, reason){
    try {
        const responseData = await getApiCall().delete(`/admin/invalidate-certificate-`+ id+`/reason=`+reason);
        return responseData;
    } catch (err) {
        console.log(err.message);
        return err.message
    }
  }
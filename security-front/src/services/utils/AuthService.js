import jwtDecode from "jwt-decode";
import store from '../../secureStore/store';

export const getToken = () => {
  const state = store.getState();
  const token = state.auth.token;
   
   if (!!token){
    return token;
  } else {
    return "";
  }
}

export const getTokenWithNoQuotes = () => {
  const token = getToken();
  
  if (!!token){
    const splitedtoken = token.slice(1, -1);
   return splitedtoken;
 } else {
   return "";
 }
}

export const getLoggedUserEmail = () => {
  const decodedToken = getDecodedToken();
  const email = decodedToken.email;

  return email;
}

export const getRole = () => {
  const decodedToken = getDecodedToken();
  const role = decodedToken.role;

  if (role === "ROLE_ADMIN"){
    return "admin";
  } else if (role === "ROLE_OWNER" || role === "ROLE_RENTER"){
    return "client";
  } else {
    return "";
  }
}

export const getDecodedToken = () => {
  const token = getToken();

  if (!!token){
    return jwtDecode(token);
  } else {
    return {};
  }
}
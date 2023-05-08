import jwtDecode from "jwt-decode";

export const setToken = (token) => {
  if(token === null){
    sessionStorage.setItem("jwt", null);
  }
  else{
    sessionStorage.setItem("jwt", JSON.stringify(token));
  }
}

export const getToken = () => {
   return sessionStorage.getItem("jwt");
}

export const removeToken = () => {
  return sessionStorage.removeItem("jwt");
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


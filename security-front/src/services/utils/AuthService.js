export const setToken = (token) => {
  if(token === null){
    sessionStorage.setItem("jwt", null);
  }
  else{
    sessionStorage.setItem("jwt", JSON.stringify(token.accessToken));
  }
}
export const getToken = () => {
   return JSON.parse(sessionStorage.getItem("jwt"));
}


export const setRole = (role) => {
  sessionStorage.setItem("role", role);
}
export const getRole = () => {
   return sessionStorage.getItem("role");
}


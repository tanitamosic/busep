import jwtDecode from 'jwt-decode';

export function getLoggedUserEmail(){
    const token = 'your.jwt.token';
    const decodedToken = jwtDecode(token);

    return decodedToken.username;
}

export function getLoggedUserRole(){
    const token = 'your.jwt.token';
    const decodedToken = jwtDecode(token);

    return decodedToken.username;
}
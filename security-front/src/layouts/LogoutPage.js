import React, { useCallback, useEffect } from 'react';
import { useNavigate  } from "react-router-dom";    
import { removeLoggedUserData } from '../services/utils/AuthService';
import { sendLogoutRequest } from '../services/api/LoginApi';
import { useDispatch } from 'react-redux';
import { resetToken, resetCookie } from '../secureStore/authSlice';
import Cookies from 'js-cookie';

export default function LogoutPage(){

    const navigate = useNavigate ();
    const dispatch = useDispatch();

    useEffect(() => {
        sendLogoutRequest().then(
            (response) => {
                dispatch(resetToken());
                Cookies.remove('token', { path: '/' });
                dispatch(resetCookie());
                Cookies.remove('cookie', { path: '/' });
                window.dispatchEvent(new Event("userRoleUpdated"));
                navigate('/login');
                return response;
            }, (error) => {
              console.log(error);
            }
        );
    })

    return <>Loging out...</>
}


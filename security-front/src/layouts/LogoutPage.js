import React, { useCallback, useEffect } from 'react';
import { useNavigate  } from "react-router-dom";    

export default function LogoutPage(){

    const navigate = useNavigate ();

    useEffect(() => {
        sessionStorage.removeItem("userRole")
        window.dispatchEvent(new Event("userRoleUpdated"));
        navigate('/login');
    })

    return <>Loging out...</>
}


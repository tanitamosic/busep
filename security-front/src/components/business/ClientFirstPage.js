import React, { useCallback, useEffect } from 'react';
import '../../assets/styles/business.css';
import { Row, Col, Button } from 'react-bootstrap';
import { useNavigate  } from "react-router-dom";    

export default function ClientFirstPage(){

    const navigate = useNavigate ();

    const logoutButtonPressed = (e) => {
        navigate('/logout');
    }

    const userRole = sessionStorage.getItem("userRole");

    useEffect(() => {
        if(userRole !== "client"){
            navigate("/unavailable");
        }
    }, [navigate, userRole])

    return <>
            <Row className='mt-2'>
                <Col sm={4}/>
                <Col sm={4} align='center'>
                    <Button className='formButton' onClick={logoutButtonPressed}>
                        Logout
                    </Button>
                </Col>
                <Col sm={4}/>
            </Row> 
        </>
}


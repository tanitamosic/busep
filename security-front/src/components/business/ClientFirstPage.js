import React, { useCallback, useEffect } from 'react';
import '../../assets/styles/business.css';
import { Row, Col, Button } from 'react-bootstrap';
import { useNavigate  } from "react-router-dom";   
import { getRole } from '../../services/utils/AuthService'; 

export default function ClientFirstPage(){

    const navigate = useNavigate ();

    const logoutButtonPressed = (e) => {
        navigate('/logout');
    }

    const userRole = getRole();

    useEffect(() => {
        if(userRole !== "client"){
            navigate("/unavailable");
        }
    }, [navigate, userRole])

    const logsButtonPressed = (e) => {
        navigate('/logs');
    }

    const reportsButtonPressed = (e) => {
        navigate('/reports');
    }

    const myObjectsButtonPressed = (e) => {
        navigate('/client/my-objects');
    }

    return <>
            <Row className='mt-2'>
                <Col sm={4}/>
                <Col sm={4} align='center'>
                    <Button className='formButton' onClick={myObjectsButtonPressed}>
                        My objects
                    </Button>
                </Col>
                <Col sm={4}/>
            </Row> 
            <Row className='mt-2'>
                <Col sm={4}/>
                <Col sm={4} align='center'>
                    <Button className='formButton' onClick={logsButtonPressed}>
                        Logs
                    </Button>
                </Col>
                <Col sm={4}/>
            </Row> 
            <Row className='mt-2'>
                <Col sm={4}/>
                <Col sm={4} align='center'>
                    <Button className='formButton' onClick={reportsButtonPressed}>
                        Reports
                    </Button>
                </Col>
                <Col sm={4}/>
            </Row> 
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


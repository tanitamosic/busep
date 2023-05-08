import React, { useEffect } from 'react';
import '../../assets/styles/business.css';
import { Row, Col, Button } from 'react-bootstrap';
import { useNavigate  } from "react-router-dom";    

export default function AdminFirstPage(){

    const navigate = useNavigate ();

    const certificatesButtonPressed = (e) => {
        navigate('/admin/certificates');
    }

    const requestsButtonPressed = (e) => {
        navigate('/admin/requests');
    }

    const allObjectsButtonPressed = (e) => {
        navigate('/admin/objects');
    }

    const logoutButtonPressed = (e) => {
        navigate('/logout');
    }

    const userRole = sessionStorage.getItem("userRole");

    useEffect(() => {
        if(userRole !== "admin"){
            navigate("/unavailable");
        }
    }, [navigate, userRole])

    return <>
            <Row className='mt-2'>
                <Col sm={4}/>
                <Col sm={4} align='center'>
                    <Button className='formButton' onClick={certificatesButtonPressed}>
                        Certificates
                    </Button>
                </Col>
                <Col sm={4}/>
            </Row> 
            <Row className='mt-2'>
                <Col sm={4}/>
                <Col sm={4} align='center'>
                    <Button className='formButton' onClick={requestsButtonPressed}>
                        Requests
                    </Button>
                </Col>
                <Col sm={4}/>
            </Row> 
            <Row className='mt-2'>
                <Col sm={4}/>
                <Col sm={4} align='center'>
                    <Button className='formButton' onClick={allObjectsButtonPressed}>
                        All objects
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


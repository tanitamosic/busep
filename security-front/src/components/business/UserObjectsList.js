import React, { useCallback } from 'react';
import '../../assets/styles/business.css';
import {useEffect, useState} from 'react';
import { Row, Col } from 'react-bootstrap';
import { getCertificates } from '../../services/api/CertificatesApi';
import ListedRequest from './ListedRequest';
import ListedCertificate from './ListedCertificate';
import { useNavigate } from 'react-router';
import ListedObject from './ListedObject';
import { getUserObjects } from '../../services/api/ObjectsApi';
import { useParams } from 'react-router';
import { getRole } from '../../services/utils/AuthService';

export default function UserObjectsList(){

    const {email} = useParams();
    const [objects, setObjects] = useState([]);
    const [listedObjects, setListedObjects] = useState([]);

    // const userRole = getRole();
    // const navigate = useNavigate();

    // useEffect(() => {
    //     if(userRole !== "admin"){
    //         navigate("/unavailable");
    //     }
    // }, [navigate, userRole])

    useEffect(() => {
        getUserObjects(email).then(
            (response) => {
                setObjects(!!response ? response.data : []);
            }
        )
    }, [email])

    useEffect(() => {
        if (objects !== undefined){
            console.log(objects)
            let mappedObjects = objects.map((object) => <ListedObject object={object} key={object.id}/>)
            setListedObjects(mappedObjects);
        }
    }, [objects])

    return <>
            <center><h3>Objects</h3></center>
            <br/>

            <Row>
                <Col sm="2">
                    Id
                </Col>
                <Col sm="2">
                    Name
                </Col>
                <Col sm="2">
                    Address
                </Col>
                <Col sm="2">
                    {/* Role */}
                </Col>
                <Col sm="1">
                    {/* Valid */}
                </Col>
                <Col sm="2" />
                <Col sm="2">
                </Col>
            </Row>

            {listedObjects}
            {/* just gives nice space in the bottom */}
            <p className='mt-3'></p> 
        </>
}


import React, { useCallback } from 'react';
import '../../assets/styles/business.css';
import {useEffect, useState} from 'react';
import { Row, Col } from 'react-bootstrap';
import { getCertificates } from '../../services/api/CertificatesApi';
import ListedRequest from './ListedRequest';
import ListedCertificate from './ListedCertificate';
import { useNavigate } from 'react-router';
import ListedObject from './ListedObject';
import { getAllObjects } from '../../services/api/ObjectsApi';
import { getRole } from '../../services/utils/AuthService';

export default function ObjectsList({objects}){

    const [listedObjects, setListedObjects] = useState([]);

    useEffect(() => {
        if (!!objects){
            console.log(objects)
            let mappedObjects = objects.map((object) => <ListedObject object={object} key={object.id}/>)
            setListedObjects(mappedObjects);
            console.log(listedObjects);
        }
    }, [objects])

    return <>
            <center><h3>Objects</h3></center>
            <br/>

            <Row>
                <Col sm="1">
                    Id
                </Col>
                <Col sm="4">
                    Address
                </Col>
                <Col sm="3">
                    Owner
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


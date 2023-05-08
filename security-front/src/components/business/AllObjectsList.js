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

export default function AllObjectsList(){

    const [objects, setObjects] = useState([]);
    const [listedObjects, setListedObjects] = useState([]);

    const userRole = sessionStorage.getItem("userRole");
    const navigate = useNavigate();

    useEffect(() => {
        if(userRole !== "admin"){
            navigate("/unavailable");
        }
    }, [navigate, userRole])

    const dummyObjects = [
        {
            "id": "1",
          "name": "Green Haven",
          "address": "123 Main Street",
          "owner": "Juros"
        },
        {
            "id": "2",
          "name": "Cozy Cottage",
          "address": "456 Elm Avenue",
          "owner": "Lazar"
        },
        {
            "id": "3",
          "name": "Sunset Villa",
          "address": "789 Oak Lane",
          "owner": "Milan"
        },
        {
            "id": "4",
          "name": "Harmony House",
          "address": "321 Pine Street",
          "owner": "Marko"
        },
        {
            "id": "5",
          "name": "Serene Residence",
          "address": "987 Maple Avenue",
          "owner": "Drago"
        }
      ]

    useEffect(() => {
        setObjects(dummyObjects);
        getAllObjects().then(
            (response) => {
                setObjects(!!response ? response.data : []);
            }
        )
    }, [])

    useEffect(() => {
        if (objects !== undefined){
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


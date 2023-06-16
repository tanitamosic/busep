import React, { useCallback } from 'react';
import '../../assets/styles/business.css';
import {useEffect, useState} from 'react';
import { Row, Col } from 'react-bootstrap';
import { getCertificates } from '../../services/api/CertificatesApi';
import ListedRequest from './ListedRequest';
import ListedCertificate from './ListedCertificate';
import { useNavigate } from 'react-router';
import ListedObject from './ListedObject';
import { getAllClients } from '../../services/api/UserApi';
import { getRole } from '../../services/utils/AuthService';
import ListedClient from './ListedClient';

export default function ClientsList(){

    const [clients, setClients] = useState();
    const [listedClients, setListedClients] = useState([]);

    useEffect(() => {
        // TODO get all clients
        getAllClients().then(
            (response) => {
                setClients(!!response ? response.data : []);
            }
        )
    }, [])

    useEffect(() => {
        if (!!clients){
            console.log(clients)
            let mappedClients = clients.map((client) => <ListedClient client={client} key={client.email}/>)
            setListedClients(mappedClients);
        }
    }, [clients])

    return <>
            <center><h3>Clients</h3></center>
            <br/>

            {/* TODO search form */}

            <Row>
                <Col sm="3">
                    Name
                </Col>
                <Col sm="3">
                    Surname
                </Col>
                <Col sm="3">
                    Email
                </Col>
                <Col sm="1">
                    Role
                </Col>
                <Col sm="1" />
            </Row>

            {listedClients}
            {/* just gives nice space in the bottom */}
            <p className='mt-3'></p> 
        </>
}


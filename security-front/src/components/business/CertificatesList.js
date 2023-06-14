import React, { useCallback } from 'react';
import '../../assets/styles/business.css';
import {useEffect, useState} from 'react';
import { Row, Col } from 'react-bootstrap';
import { getCertificates } from '../../services/api/CertificatesApi';
import ListedRequest from './ListedRequest';
import ListedCertificate from './ListedCertificate';
import { useNavigate } from 'react-router';
import { getRole } from '../../services/utils/AuthService';

export default function CertificatesList(){

    const [certificates, setCertificates] = useState([]);
    const [listedCertificates, setListedCertificates] = useState([]);

    const userRole = getRole();
    const navigate = useNavigate();

    useEffect(() => {
        if(userRole !== "admin"){
            navigate("/unavailable");
        }
    }, [navigate, userRole])

    useEffect(() => {
        getCertificates().then(
            (response) => {
                console.log(response)
                setCertificates(!!response ? response.data : []);
            }
        )
    }, [])

    useEffect(() => {
        if (certificates !== undefined){
            console.log(certificates)
            let mappedCertificates = certificates.map((certificate) => <ListedCertificate certificate={certificate} key={certificate.email}/>)
            setListedCertificates(mappedCertificates);
        }
    }, [certificates])

    return <>
            <center><h3>Certificates</h3></center>
            <br/>

            <Row>
                <Col sm="2">
                    Id
                </Col>
                <Col sm="2">
                    Fullname
                </Col>
                <Col sm="2">
                    Email
                </Col>
                <Col sm="2">
                    End date
                </Col>
                <Col sm="2">
                    Valid
                </Col>
                <Col sm="2" />
            </Row>

            {listedCertificates}
            {/* just gives nice space in the bottom */}
            <p className='mt-3'></p> 
        </>
}


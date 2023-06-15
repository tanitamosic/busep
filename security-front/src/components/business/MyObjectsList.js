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
import { getLoggedUserEmail, getRole } from '../../services/utils/AuthService';
import ObjectsList from './ObjectsList';

export default function MyObjectsList(){

    const [objects, setObjects] = useState([]);

    // const userRole = getRole();
    // const navigate = useNavigate();

    // useEffect(() => {
    //     if(userRole !== "admin"){
    //         navigate("/unavailable");
    //     }
    // }, [navigate, userRole])

    useEffect(() => {
        getUserObjects(getLoggedUserEmail()).then(
            (response) => {
                setObjects(!!response ? response.data : []);
            }
        )
    }, [])

    return <>
            <ObjectsList objects={objects} />
        </>
}


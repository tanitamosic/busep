import React, { useCallback } from 'react';
import '../../assets/styles/business.css';
import {useEffect, useState} from 'react';
import { Row, Col } from 'react-bootstrap';
import { getRequests } from '../../services/api/CertificatesApi';
import ListedRequest from './ListedRequest';

export default function RequestsList(){

    const [requests, setRequests] = useState([]);
    const [listedRequests, setListedRequests] = useState([]);

    const dummyRequests = [
        {
          "givenName": "John",
          "surname": "Doe",
          "email": "johndoe@example.com",
          "password": "5tr0ngP@55w0rd!",
          "organization": "Acme Corporation",
          "orgUnit": "Sales",
          "country": "United States",
          "owner": true
        },
        {
          "givenName": "Jane",
          "surname": "Smith",
          "email": "janesmith@example.com",
          "password": "P@ssw0rd123",
          "organization": "Globex Corporation",
          "orgUnit": "Marketing",
          "country": "Canada",
          "owner": false
        },
        {
          "givenName": "Michael",
          "surname": "Johnson",
          "email": "michaeljohnson@example.com",
          "password": "SecureP@55",
          "organization": "Initech",
          "orgUnit": "IT",
          "country": "United Kingdom",
          "owner": false
        },
        {
          "givenName": "Samantha",
          "surname": "Lee",
          "email": "samanthalee@example.com",
          "password": "Pa$$word!",
          "organization": "Tech Solutions",
          "orgUnit": "Development",
          "country": "Australia",
          "owner": true
        },
        {
          "givenName": "David",
          "surname": "Brown",
          "email": "davidbrown@example.com",
          "password": "C0mpl3xP@55",
          "organization": "ABC Corporation",
          "orgUnit": "Finance",
          "country": "New Zealand",
          "owner": false
        }
      ]

    useEffect(() => {
        setRequests(dummyRequests);
        // getRequests().then(
        //     (response) => {
        //         setRequests(!!response ? response.data : []);
        //     }
        // )
    }, [])

    useEffect(() => {
        if (requests !== undefined){
            console.log(requests)
            let mappedRequests = requests.map((request) => <ListedRequest request={request} key={request.email}/>)
            setListedRequests(mappedRequests);
        }
    }, [requests])

    return <>
            <center><h3>Pending requests</h3></center>
            <br/>

            <Row>
                <Col sm="2">
                    Name
                </Col>
                <Col sm="2">
                    Lastname
                </Col>
                <Col sm="2">
                    Organization
                </Col>
                <Col sm="2">
                    Email
                </Col>
                <Col sm="4" />
            </Row>

            {listedRequests}
            {/* just gives nice space in the bottom */}
            <p className='mt-3'></p> 
        </>












}


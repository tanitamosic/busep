import React, { useCallback } from 'react';
import '../../assets/styles/business.css';
import { Row, Col, Button } from 'react-bootstrap';
import FixedWidthRegButton from '../buttons/FixedWidthRegButton';
import {useParams} from 'react-router-dom';
import {useEffect, useState} from 'react';
import { declineRequest, acceptRequest } from '../../services/api/CertificatesApi';
import LabeledTextarea from '../forms/LabeledTextarea';

export default function RequestPreview(){

    const {email} = useParams();
    const [request, setRequest] = useState(null);
    const [extensions, setExtensions] = useState([]);
    const [declineReason, setDeclineReason] = useState("");

    // TODO use this when apis available on backend
    //
    // useEffect(() => {
    //     async function fetchRequest(){
    //         const requestData = await getRequestByEmail(email);
    //         setRequest(!!requestData ? requestData.data : {});
    //         return requestData;
    //     }
    //     fetchRequest();
    // }, [email])

    useEffect(() => {
        for (let r of dummyRequests){
            if (r.email === email){
                setRequest(r);
            }
        }
        
    }, [email])

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

    const acceptButtonPressed = (e) => {
        // if (true) {
            postAcceptRequest(e);
        // } else {
            // alert("")
        // }
    }

    const postAcceptRequest = useCallback(
        (e) => {
            e.preventDefault();
            // TODO should not send and receive password
            const acceptJson = {
                "givenName": request.givenName,
                "surname": request.surname,
                "email": request.email,
                "password": request.password,
                "organization": request.organization,
                "orgUnit": request.orgUnit,
                "country": request.country,
                "owner": request.owner,
                "extensions": extensions
            }
            console.log(acceptJson)
            acceptRequest(acceptJson).then(
                (response) => {
                    console.log(response);
                    alert("Request has been accepted.");
                }, (error) => {
                    console.log(error);
                }
            );
        }, [request, extensions]
    )

    const declineButtonPressed = (e) => {
        if (declineReason.length > 0) {
            postDeclineRequest(e);
        } else {
            alert("You need to write decline reason")
        }
    }

    const postDeclineRequest = useCallback(
        (e) => {
            e.preventDefault();
            const declineJson = {email, declineReason}
            console.log(declineJson)
            declineRequest(declineJson).then(
                (response) => {
                    console.log(response);
                    alert("Request for " + email + " declined.");
                }, (error) => {
                console.log(error);
                }
            );
        }, [declineReason, email]
    )
    
    if(!!request){
    return <>
            <center><h3>Request for {email}</h3></center>
            <br/>
            <div className="borderedBlock mt-3 " align="">
                <Row>
                    <Col sm="4">
                        <Row>
                            Name: {request.givenName}
                        </Row>
                        <Row>
                            Last name: {request.surname}
                        </Row>
                        <Row>
                            Email: {request.email}
                        </Row>
                        
                    </Col>
                    <Col sm="4">
                        <Row>
                            Organization: {request.organization}
                        </Row>
                        <Row>
                            Organizational unit: {request.orgUnit}
                        </Row>
                        <Row>
                            Country: {request.country}
                        </Row>
                    </Col>
                    <Col sm="4">
                        <Row>
                            {request && <p>Is property owner: {request.owner.toString()}</p>}
                        </Row>
                    </Col>
                </Row>
            </div>
            <div className="borderedBlock mt-3 " align="">
                <Row className='mt-2'>
                        <Col sm={4}/>
                        <Col sm={4} align='center'>
                            <Button className='formButton' onClick={acceptButtonPressed}>
                                Accept
                            </Button>
                        </Col>
                        <Col sm={4}/>
                  </Row>
                <br/>
                <LabeledTextarea value={declineReason} label="Decline reason" inputName="declineReason" placeholder="Type reason for declining the request" required onChangeFunc={setDeclineReason}/>
                <Row className='mt-2'>
                        <Col sm={4}/>
                        <Col sm={4} align='center'>
                            <Button className='formButton' onClick={declineButtonPressed}>
                                Decline
                            </Button>
                        </Col>
                        <Col sm={4}/>
                  </Row> 
            </div>
        </>
    } else {
        return "No request for such email"
    }
}


import React, { useCallback } from 'react';
import '../../assets/styles/business.css';
import { Row, Col, Button, Form } from 'react-bootstrap';
import FixedWidthRegButton from '../buttons/FixedWidthRegButton';
import {useParams} from 'react-router-dom';
import {useEffect, useState} from 'react';
import { declineRequest, acceptRequest } from '../../services/api/CertificatesApi';
import LabeledTextarea from '../forms/LabeledTextarea';
import LabeledInput from '../forms/LabeledInput';
import { checkDateInput, checkNumInput} from '../../services/utils/InputValidation';

export default function RequestPreview(){

    const {email} = useParams();
    const [request, setRequest] = useState(null);
    const [extensions, setExtensions] = useState([]);
    const [declineReason, setDeclineReason] = useState("");
    const [chosenKeyUsage, setChosenKeyUsage] = useState([]);
    const [chosenExtendedKeyUsage, setChosenExtendedKeyUsage] = useState([]);
    const [startDate, setStartDate] = useState("");
    const [duration, setDuration] = useState(0);

    // key usage
    const [cbDigitalSignature, setCbDigitalSignature] = useState(false);
    const [cbKeyEncipherment, setCbKeyEncipherment] = useState(false);
    const [cbEnciphermentOnly, setCbEnciphermentOnly] = useState(false);
    const [cbNonRepudiation, setCbNonRepudiation] = useState(false);
    const [cbKeyAgreement, setCbKeyAgreement] = useState(false);

    // extended key usage
    const [cbOCSPSigning, setCbOCSPSigning] = useState(false);
    const [cbSmartcardLogon, setCbSmartcardLogon] = useState(false);
    const [cbTLSClient, setCbTLSClient] = useState(false);
    const [cbTLSServer, setCbTLSServer] = useState(false);
    const [cbDocumentSigning, setCbDocumentSigning] = useState(false);

    const SSLServerTemplate = [["Digital Signature", "Key Encipherment"], ["TLS Web Server Authentication"]]
    const SSLClientTemplate = [["Digital Signature", "Key Encipherment"], ["TLS Web Client Authentication"]]
    const CodeSigningTemplate = [["Digital Signature"], ["TLS Web Server Authentication"]]

    const availableKeyUsage = [
        "Digital Signature",
        "Key Encipherment",
        "Encipher Only",
        "Non Repudiation",
        "Key Agreement"
    ]

    const availableExtendedKeyUsage = [
        "OCSP Signing",
        "Smartcard Logon",
        "TLS Web Client Authentication",
        "TLS Web Server Authentication",
        "Document Signing"
    ]

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

    const addSSLServerExtensions = (e) => {
        decheckAllKeyOptions()
        setChosenKeyUsage(SSLServerTemplate[0]);
        setChosenExtendedKeyUsage(SSLServerTemplate[1]);

        setCbDigitalSignature(true);
        setCbKeyEncipherment(true);
        setCbTLSServer(true);
    }

    const addSSLClientExtensions = (e) => {
        decheckAllKeyOptions()
        setChosenKeyUsage(SSLClientTemplate[0]);
        setChosenExtendedKeyUsage(SSLClientTemplate[1]);
        
        setCbDigitalSignature(true);
        setCbKeyEncipherment(true);
        setCbTLSClient(true);
    }

    const addCodeSigningExtensions = (e) => {
        decheckAllKeyOptions()
        setChosenKeyUsage(CodeSigningTemplate[0]);
        setChosenExtendedKeyUsage(CodeSigningTemplate[1]);
        
        setCbDigitalSignature(true);
        setCbTLSServer(true);
    }

    const decheckAllKeyOptions = () => {
        // key usage
        setCbDigitalSignature(false);
        setCbKeyEncipherment(false);
        setCbEnciphermentOnly(false);
        setCbNonRepudiation(false);
        setCbKeyAgreement(false);

        // extended key usage
        setCbOCSPSigning(false);
        setCbSmartcardLogon(false);
        setCbTLSClient(false);
        setCbTLSServer(false);
        setCbDocumentSigning(false);
    }

    const addOrRemoveToKeyUsage = (keyToAddOrRemove) => {
        let updated = JSON.parse(JSON.stringify(chosenKeyUsage));

        const index = updated.indexOf(keyToAddOrRemove);
        if (index > -1) { // if key already exists then remove it 
            updated.splice(index, 1); // 2nd parameter means remove one item only
        } else {
            updated.push(keyToAddOrRemove); // otherwise add it
        }

        setChosenKeyUsage(updated)
    }

    const addOrRemoveToExtendedKeyUsage = (keyToAddOrRemove) => {
        let updated = JSON.parse(JSON.stringify(chosenExtendedKeyUsage));

        const index = updated.indexOf(keyToAddOrRemove);
        if (index > -1) { // if key already exists then remove it 
            updated.splice(index, 1); // 2nd parameter means remove one item only
        } else {
            updated.push(keyToAddOrRemove); // otherwise add it
        }

        setChosenExtendedKeyUsage(updated)
    }

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
            if (duration.length > 0 && checkNumInput(duration) &&
                 checkDateInput(startDate)){
                    const acceptJson = {
                        "givenName": request.givenName,
                        "surname": request.surname,
                        "email": request.email,
                        "password": request.password,
                        "organization": request.organization,
                        "orgUnit": request.orgUnit,
                        "country": request.country,
                        "owner": request.owner,
                        "extensions": {
                            "keyUsage" : chosenKeyUsage,
                            "extendedKeyUsage" : chosenExtendedKeyUsage
                        },
                        "startDate" : startDate,
                        "duration" : duration
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
                } else {
                    alert("Invalid input")
                }
            
        }, [request, chosenKeyUsage, chosenExtendedKeyUsage, startDate, duration]
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
                
                <Form>
                    <Row>
                        <Col sm={4} align='center'>
                            <b>Choose templates</b>
                            <Form.Check 
                                type="radio"
                                label="SSL Server"
                                onChange={(e) => addSSLServerExtensions()}
                                name="groupOptions"
                            />
                            <Form.Check 
                                type="radio"
                                label="SSL Client"
                                onChange={(e) => addSSLClientExtensions()}
                                name="groupOptions"
                            />
                            <Form.Check 
                                type="radio"
                                label="Code Signing"
                                onChange={(e) => addCodeSigningExtensions()}
                                name="groupOptions"
                            />
                        </Col>
                        <Col sm={4} align='center'>
                            <b>Key usages</b>
                            <Form.Check 
                                type="checkbox"
                                label="Digital Signature"
                                checked={cbDigitalSignature}
                                onChange={(e) => {setCbDigitalSignature(e.target.checked); addOrRemoveToKeyUsage("Digital Signature")}}
                            />
                            <Form.Check 
                                type="checkbox"
                                label="Key Encipherment"
                                checked={cbKeyEncipherment}
                                onChange={(e) => {setCbKeyEncipherment(e.target.checked); addOrRemoveToKeyUsage("Key Encipherment")}}
                            />
                            <Form.Check 
                                type="checkbox"
                                label="Encipherment Only"
                                checked={cbEnciphermentOnly}
                                onChange={(e) => {setCbEnciphermentOnly(e.target.checked); addOrRemoveToKeyUsage("Encipherment Only")}}
                            />
                            <Form.Check 
                                type="checkbox"
                                label="Non Repudiation"
                                checked={cbNonRepudiation}
                                onChange={(e) => {setCbNonRepudiation(e.target.checked); addOrRemoveToKeyUsage("Non Repudiation")}}
                            />
                            <Form.Check 
                                type="checkbox"
                                label="Key Agreement"
                                checked={cbKeyAgreement}
                                onChange={(e) => {setCbKeyAgreement(e.target.checked); addOrRemoveToKeyUsage("Key Agreement")}}
                            />
                        </Col>
                        <Col sm={4} align='center'>
                            <b>Extended key usages</b>
                            <Form.Check 
                                type="checkbox"
                                label="OCSP Signing"
                                checked={cbOCSPSigning}
                                onChange={(e) => {setCbOCSPSigning(e.target.checked); addOrRemoveToExtendedKeyUsage("OCSP Signing")}}
                            />
                            <Form.Check 
                                type="checkbox"
                                label="Smartcard Logon"
                                checked={cbSmartcardLogon}
                                onChange={(e) => {setCbSmartcardLogon(e.target.checked); addOrRemoveToExtendedKeyUsage("Smartcard Logon")}}
                            />
                            <Form.Check 
                                type="checkbox"
                                label="TLS Client"
                                checked={cbTLSClient}
                                onChange={(e) => {setCbTLSClient(e.target.checked); addOrRemoveToExtendedKeyUsage("TLS Client")}}
                            />
                            <Form.Check 
                                type="checkbox"
                                label="TLS Server"
                                checked={cbTLSServer}
                                onChange={(e) => {setCbTLSServer(e.target.checked); addOrRemoveToExtendedKeyUsage("TLS Server")}}
                            />
                            <Form.Check 
                                type="checkbox"
                                label="Document Signing"
                                checked={cbDocumentSigning}
                                onChange={(e) => {setCbDocumentSigning(e.target.checked); addOrRemoveToExtendedKeyUsage("Document Signing")}}
                            />
                        </Col>
                    </Row>
                    <Row>
                        <Col sm={6} align='center'>
                            <LabeledInput value={startDate} label="Start date" inputName="startDate" placeholder="YYYY-MM-dd" required onChangeFunc={setStartDate}/>
                        </Col>
                        <Col sm={6} align='center'>
                        <LabeledInput value={duration} label="Duration" inputName="duration" placeholder="Type duration" required onChangeFunc={setDuration}/>
                        </Col>
                    </Row>
                </Form>

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


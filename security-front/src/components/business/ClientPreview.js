import React, { useCallback } from 'react';
import '../../assets/styles/business.css';
import { Row, Col, Button } from 'react-bootstrap';
import FixedWidthRegButton from '../buttons/FixedWidthRegButton';
import {useParams} from 'react-router-dom';
import {useEffect, useState} from 'react';
import {isCertificateValid, deactivateCertificate } from '../../services/api/CertificatesApi';
import LabeledTextarea from '../forms/LabeledTextarea';
import { useNavigate  } from "react-router-dom";
import { getRole } from '../../services/utils/AuthService';
import { getUserByEmail } from '../../services/api/UserApi';
import ListedDevice from './ListedDevice';
import { NewDeviceForm } from '../forms/NewDeviceForm';
import { getObject } from '../../services/api/ObjectsApi';
import { removeObjectRequest } from '../../services/api/ObjectsApi';
import { UpdateObjectForm } from '../forms/UpdateObjectForm';
import { getUserObjects } from '../../services/api/ObjectsApi';

export default function ClientPreview(){

    const {email} = useParams();
    const [client, setClient] = useState(null);
    const [clientObjects, setClientObjects] = useState();
    // const [owner, setOwner] = useState();
    // const [renter, setRenter] = useState();
    // const [devices, setDevices] = useState();
    // const [listedDevices, setListedDevices] = useState();
    // const [isUpdateMode, setIsUpdateMode] = useState(false);
    // const [isNewDeviceMode, setIsNewDeviceMode] = useState(false);
    // const [isAdmin, setIsAdmin] = useState(false);
    const navigate = useNavigate();

    // useEffect(() => {
    //     if (getRole() === "admin"){
    //         setIsAdmin(true);
    //     }
    // }, [])

    useEffect(() => {
        if (!!email){
            getUserByEmail(email).then(
                (response) => {
                    console.log(response.data)
                    setClient(response.data);
                }, (error) => {
                    console.log(error);
                }
            );
            getUserObjects(email).then(
                (response) => {
                    setClientObjects(!!response ? response.data : []);
                }
            )
        }
    }, [email])

    // useEffect(() => {
    //     if (!!client){

    //     }
    // }, [client])

    // useEffect(() => {
    //     if (!!devices){
    //         let mappedDevices = devices.map((device) => <ListedDevice device={device} key={device.houseId + "_" + device.name}/>)
    //         setListedDevices(mappedDevices);
    //     }
    // }, [devices])

    // const removeObject = useCallback(
    //     (e) => {
    //         e.preventDefault();
    //         removeObjectRequest(id).then(
    //             (response) => {
    //                 console.log(response);
    //                 navigate("/admin/all-objects");
    //             }, (error) => {
    //                 console.log(error);
    //             }
    //         );
    //     }, []
    // )

    const removeClientButtonPressed = (e) => {
        // TODO
        // removeObject() 
    }

    const changeClientRoleButtonPressed = (e) => {
        // TODO
    }

    const clientObjectsButtonPressed = (e) => {
        navigate("/admin/objects/" + email);
    }

    if(!!client && !!clientObjects){
    return <>
            <center><h3>Client</h3></center>

            <br/>

            <div className="borderedBlock mt-3 " align="">
                <Row>
                <Col sm="3"/>
                    <Col sm="1">
                        <right><h5>Name:</h5></right>
                    </Col>
                    <Col sm="4">
                        <center><h5>{client.name}</h5></center>
                    </Col>
                    <Col sm="4"/>
                </Row>
                <Row>
                    <Col sm="3"/>
                    <Col sm="1">
                        <right><h5>Surname:</h5></right>
                    </Col>
                    <Col sm="4">
                        <center><h5>{client.surname}</h5></center>
                    </Col>
                    <Col sm="4"/>
                </Row>
                <Row>
                <Col sm="3"/>
                    <Col sm="1">
                        <right><h5>Email:</h5></right>
                    </Col>
                    <Col sm="4">
                        <center><h5>{client.email}</h5></center>
                    </Col>
                    <Col sm="4"/>
                </Row>
                <Row>
                    <Col sm="3"/>
                    <Col sm="1">
                        <right><h5>Role:</h5></right>
                    </Col>
                    <Col sm="4">
                        <center><h5>{client.role}</h5></center>
                    </Col>
                    <Col sm="4"/>
                </Row>
                <Row>
                    <Col sm="2"/>
                    <Col sm="2">
                        <right><h5>Number of objects:</h5></right>
                    </Col>
                    <Col sm="4">
                        <center><h5>{clientObjects.length}</h5></center>
                    </Col>
                    <Col sm="4"/>
                </Row>
                <Row className='mt-2'>
                <Col sm={4}/>
                <Col sm={4} align='center'>
                    <Button className='formButton' onClick={clientObjectsButtonPressed}>
                        See objects
                    </Button>
                </Col>
                <Col sm={4}/>
            </Row> 
            </div>
            <div className="borderedBlock mt-3 " align="">
                {clientObjects.length === 0 ? 
                    <Row className='mt-2'>
                        <Col sm={1}/>
                        <Col sm={4} align='center'>
                            <Button className='formButton' onClick={changeClientRoleButtonPressed}>
                                {client.role === "RENTER" ? "Change role to OWNER" : "Change role to RENTER"}
                            </Button>
                        </Col>
                        <Col sm={2}/>
                        <Col sm={4}> 
                            <Button className='formButton' onClick={removeClientButtonPressed}>
                                Remove client
                            </Button>
                        </Col>
                        <Col sm={1}/>
                  </Row>
                  : 
                  <center><h5>Can't make changes to client while he has any object</h5></center>
                } 
            </div>
        </>
    } else {
        return "No such client"
    }
}


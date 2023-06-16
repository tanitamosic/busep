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
import { removeObjectRequest, sendRemoveRenterRequest } from '../../services/api/ObjectsApi';
import { UpdateObjectForm } from '../forms/UpdateObjectForm';

export default function ObjectPreview(){

    const {id} = useParams();
    const [object, setObject] = useState(null);
    const [owner, setOwner] = useState();
    const [renter, setRenter] = useState();
    const [devices, setDevices] = useState();
    const [listedDevices, setListedDevices] = useState();
    const [isUpdateMode, setIsUpdateMode] = useState(false);
    const [isNewDeviceMode, setIsNewDeviceMode] = useState(false);
    const [isAdmin, setIsAdmin] = useState(false);
    const navigate = useNavigate();

    useEffect(() => {
        if (getRole() === "admin"){
            setIsAdmin(true);
        }
    }, [])

    useEffect(() => {
        getObject(id).then(
            (response) => {
                console.log(response.data)
                setObject(response.data);
                setDevices(response.data.devices);
            }
        )
    }, [id])

    useEffect(() => {
        if (!!object){
            if (!!object.ownerEmail && object.ownerEmail.length > 0){
                getUserByEmail(object.ownerEmail).then(
                    (response) => {
                        setOwner(response.data);
                    }, (error) => {
                      console.log(error);
                    }
                );
            }
            
            if (!!object.renterEmail && object.renterEmail.length > 0){
                getUserByEmail(object.renterEmail).then(
                    (response) => {
                        setRenter(response.data);
                    }, (error) => {
                      console.log(error);
                    }
                );
            }
        }
    }, [object])

    useEffect(() => {
        if (!!devices){
            let mappedDevices = devices.map((device) => <ListedDevice device={device} key={device.houseId + "_" + device.name}/>)
            setListedDevices(mappedDevices);
        }
    }, [devices])

    const removeObject = useCallback(
        (e) => {
            e.preventDefault();
            removeObjectRequest(id).then(
                (response) => {
                    console.log(response);
                    navigate("/admin/all-objects");
                }, (error) => {
                    console.log(error);
                }
            );
        }, []
    )

    const removeOBjectButtonPressed = (e) => {
        removeObject(e);
    }

    const updateModeButtonPressed = (e) => {
        setIsUpdateMode(!isUpdateMode);
    }

    const addNewDeviceButtonPressed = (e) => {
        setIsNewDeviceMode(!isNewDeviceMode);
    }
    
    if(!!object){
    return <>
            <center><h3>{object.name}</h3></center>
            <center><h5>{object.address}</h5></center>

            <br/>

            <div className="borderedBlock mt-3 " align="">
                {!!owner && <>
                    <center><h5>Owner</h5></center>
                    <Row>
                        <Col sm="4">
                            <center>{owner.name}</center>
                        </Col>
                        <Col sm="4">
                            <center>{owner.surname}</center>
                        </Col>
                        <Col sm="4">
                            <center>{owner.email}</center>
                        </Col>
                    </Row>
                </>}
                {!!renter && <>
                    <br/>
                    <center><h5>Renter</h5></center>
                    <Row>
                        <Col sm="4">
                            <center>{renter.name}</center>
                        </Col>
                        <Col sm="4">
                            <center>{renter.surname}</center>
                        </Col>
                        <Col sm="4">
                            <center>{renter.email}</center>
                        </Col>
                    </Row>
                </>}
                {isAdmin && <Row className='mt-2'>
                        <Col sm={4}/>
                        <Col sm={4}/>
                        <Col sm={4} align='center'>
                            <Button className='formButton' onClick={updateModeButtonPressed}>
                                {isUpdateMode ? "Cancel" : "Make changes"}
                            </Button>
                        </Col>
                  </Row>} 
                  {isAdmin && isUpdateMode && <>
                    <UpdateObjectForm id={object.id} />
                    <br/>
                    <Row className='mt-2'>
                        <Col sm={4}/>
                        <Col sm={4} align='center'>
                            <Button className='formButton' onClick={removeOBjectButtonPressed}>
                                Remove object
                            </Button>
                        </Col>
                        <Col sm={4}/>
                  </Row> 
                  </>}
            </div>

            <div className="borderedBlock mt-3 " align="">
                {(!!devices && devices.length > 0) ? <>
                        <br/>
                        <center><h5>Devices</h5></center>
                        <Row>
                            <Col sm="3">
                                <center>Name</center>
                            </Col>
                            <Col sm="3">
                                <center>Type</center>
                            </Col>
                            <Col sm="3">
                                <center>Read time</center>
                            </Col>
                            <Col sm="3" />
                        </Row>
                        {listedDevices}
                    </> : 
                    <>
                        <center><h5>No devices</h5></center>
                </>}
                {isAdmin && <Row className='mt-2'>
                        <Col sm={4}/>
                        <Col sm={4}/>
                        <Col sm={4} align='center'>
                            <Button className='formButton' onClick={addNewDeviceButtonPressed}>
                                {isNewDeviceMode ? "Cancel" : "Add new device"}
                            </Button>
                        </Col>
                  </Row> }
                {isAdmin && isNewDeviceMode && <>
                    <NewDeviceForm houseId={object.id} />
                  </>}
            </div>
        </>
    } else {
        return "No such object"
    }
}


import React from 'react';
import '../../assets/styles/business.css';
import { Row, Col } from 'react-bootstrap';
import FixedWidthRegButton from '../buttons/FixedWidthRegButton';
import { useCallback, useEffect, useState } from 'react';
import { removeDeviceRequest } from '../../services/api/ObjectsApi';
import { getRole } from '../../services/utils/AuthService';

export default function ListedDevice({device}){

    const [canRemove, setCanRemove] = useState(false);

    useEffect(() => {
        if (getRole() === "admin"){
            setCanRemove(true);
        }
    }, [device])

    const removeButtonPressed = (e) => {
        removeDevice(e);
    }

    const removeDevice = useCallback(
        (e) => {
            e.preventDefault();
            console.log("device id : " + device.id);
            removeDeviceRequest(device.id).then(
                (response) => {
                    console.log(response);
                    window.location.reload();
                }, (error) => {
                    console.log(error);
                }
            );
        }, []
    )

    return <div className="borderedBlock mt-3 " align="">
                <Row>
                    <Col sm="3">
                        <center>{device.name}</center>
                    </Col>
                    <Col sm="3">
                        <center>{device.type}</center>
                    </Col>
                    <Col sm="3">
                        <center>{device.readTime}</center>
                    </Col>
                    <Col sm="1" />
                    <Col sm="1">
                        {canRemove && <div className='mt-4'>
                            <FixedWidthRegButton text='Remove' onClickFunction={removeButtonPressed}/>
                        </div>}
                    </Col>
                    <Col sm="1" />
                </Row>
            </div>
}


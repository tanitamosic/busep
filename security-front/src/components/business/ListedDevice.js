import React from 'react';
import '../../assets/styles/business.css';
import { Row, Col } from 'react-bootstrap';
import FixedWidthRegButton from '../buttons/FixedWidthRegButton';
import { useCallback } from 'react';
import { removeObjectRequest } from '../../services/api/ObjectsApi';

export default function ListedDevice({device}){

    const removeButtonPressed = (e) => {
        removeDevice(e);
    }

    const removeDevice = useCallback(
        (e) => {
            e.preventDefault();
            //TODO

            // for the sake of easienes, just reload (navigate to the house preview again (you had id in the deleted device))

            // removeObjectRequest(object.id).then(
            //     (response) => {
            //         console.log(response);
            //         if (response.data === true){
            //             alert("Object removed.");
            //         }
            //     }, (error) => {
            //         console.log(error);
            //     }
            // );
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
                        <div className='mt-4'>
                            <FixedWidthRegButton text='Remove' onClickFunction={removeButtonPressed}/>
                        </div>
                    </Col>
                    <Col sm="1" />
                </Row>
            </div>
}


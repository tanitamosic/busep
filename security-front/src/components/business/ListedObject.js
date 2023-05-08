import React from 'react';
import '../../assets/styles/business.css';
import { Row, Col } from 'react-bootstrap';
import FixedWidthRegButton from '../buttons/FixedWidthRegButton';
import { useCallback } from 'react';
import { removeObject } from '../../services/api/ObjectsApi';

export default function ListedObject({object}){
    const devicesViewUrl = "/admin/objects/devices/" + object.id;

    const removeButtonPressed = (e) => {
        // removeObject(e);
    }

    // const removeObject = useCallback(
    //     (e) => {
    //         e.preventDefault();
    //         removeObject(object.id).then(
    //             (response) => {
    //                 console.log(response);
    //                 if (response.data === true){
    //                     alert("Object removed.");
    //                 }
    //             }, (error) => {
    //                 console.log(error);
    //             }
    //         );
    //     }, []
    // )

    return <div className="borderedBlock mt-3 " align="">
                <Row>
                    <Col sm="2">
                        {object.id}
                    </Col>
                    <Col sm="2">
                        {object.name} 
                    </Col>
                    <Col sm="2">
                        {object.address}
                    </Col>
                    <Col sm="2">
                        {/* {object.usersRole} owner / renter */}
                    </Col>
                    <Col sm="1">
                        {/* {object.valid.toString()} */}
                    </Col>
                    
                    <Col sm="1">
                        {/* <div className='mt-4'>
                            <FixedWidthRegButton href={devicesViewUrl} text='Change role' onClickFunction={''}/>
                        </div> */}
                        
                    </Col>
                    <Col sm="1">
                        <div className='mt-4'>
                            <FixedWidthRegButton text='Remove' onClickFunction={removeButtonPressed}/>
                        </div>
                        
                    </Col>
                </Row>
            </div>
}


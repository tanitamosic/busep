import React from 'react';
import '../../assets/styles/business.css';
import { Row, Col } from 'react-bootstrap';
import FixedWidthRegButton from '../buttons/FixedWidthRegButton';
import { useCallback } from 'react';
import { useNavigate } from 'react-router';
import { removeObjectRequest } from '../../services/api/ObjectsApi';
import { useState, useEffect } from 'react';
import { getRole } from '../../services/utils/AuthService';

export default function ListedObject({object}){
    // default is for client use
    const [canRemove, setCanRemove] = useState(false);
    const [previewUrl, setPreviewUrl] = useState("/client/my-objects/" + object.id);

    useEffect(() => {
        if (getRole() === "admin"){
            setCanRemove(true);
            setPreviewUrl("/admin/object/" + object.id);
        }
    }, [object])

    const removeButtonPressed = (e) => {
        removeObject(e);
    }

    const removeObject = useCallback(
        (e) => {
            e.preventDefault();
            removeObjectRequest(object.id).then(
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
                    <Col sm="1">
                        {object.id}
                    </Col>
                    <Col sm="4">
                        {object.address}
                    </Col>
                    <Col sm="3">
                        {object.ownerEmail}
                    </Col>
                    
                    
                    <Col sm="1">
                        <div className='mt-4'>
                            <FixedWidthRegButton href={previewUrl} text='Preview' onClickFunction={''}/>
                        </div>
                    </Col>

                    <Col sm="1" />

                    <Col sm="1">
                        {canRemove && <div className='mt-4'>
                            <FixedWidthRegButton text='Remove' onClickFunction={removeButtonPressed}/>
                        </div>}
                    </Col>
                </Row>
            </div>
}


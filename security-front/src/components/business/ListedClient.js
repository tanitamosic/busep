import React from 'react';
import '../../assets/styles/business.css';
import { Row, Col } from 'react-bootstrap';
import FixedWidthRegButton from '../buttons/FixedWidthRegButton';
import { useState, useEffect } from 'react';

export default function ListedClient({client}){
    const [previewUrl, setPreviewUrl] = useState("/admin/clients/" + client.email);

    return <div className="borderedBlock mt-3 " align="">
                <Row>
                    <Col sm="3">
                        {client.name}
                    </Col>
                    <Col sm="3">
                        {client.surname}
                    </Col>
                    <Col sm="3">
                        {client.email}
                    </Col>
                    <Col sm="1">
                        {client.role}
                    </Col>
                    <Col sm="1">
                        <div className='mt-4'>
                            <FixedWidthRegButton href={previewUrl} text='Preview' onClickFunction={''}/>
                        </div>
                    </Col>
                </Row>
            </div>
}


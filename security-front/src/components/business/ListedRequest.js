import React from 'react';
import '../../assets/styles/business.css';
import { Row, Col } from 'react-bootstrap';
import FixedWidthRegButton from '../buttons/FixedWidthRegButton';

export default function ListedRequest({request}){
    console.log(request.id)
    const detViewUrl = "/admin/requests/" + request.email;

    return <div className="borderedBlock mt-3 " align="">
                <Row>
                    <Col sm="2">
                        {request.givenName}
                    </Col>
                    <Col sm="2">
                        {request.surname}
                    </Col>
                    <Col sm="2">
                        {request.organization}
                    </Col>
                    <Col sm="2">
                        {request.email}
                    </Col>
                    <Col sm="2">
                    </Col>
                    <Col sm="2">
                        <div className='mt-4'>
                            <FixedWidthRegButton href={detViewUrl} text='Preview' onClickFunction={''}/>
                        </div>
                    </Col>
                </Row>
            </div>
}


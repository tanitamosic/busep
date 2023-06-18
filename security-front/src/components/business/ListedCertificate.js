import React from 'react';
import '../../assets/styles/business.css';
import { Row, Col } from 'react-bootstrap';
import FixedWidthRegButton from '../buttons/FixedWidthRegButton';

export default function ListedCertificate({certificate}){
    const detViewUrl = "/admin/certificates/" + certificate.id;
    const objectsViewUrl = "/admin/objects/" + certificate.id;

    return <div className="borderedBlock mt-3 " align="">
                <Row>
                    <Col sm="2">
                        {certificate.id}
                    </Col>
                    <Col sm="2">
                        {certificate.givenName} {certificate.surname} 
                    </Col>
                    <Col sm="2">
                        {certificate.email}
                    </Col>
                    <Col sm="2">
                        {certificate.endDate.slice(0, 10)}
                    </Col>
                    <Col sm="1">
                        {certificate.valid.toString()}
                    </Col>
                    
                    <Col sm="2">
                        <div className='mt-4'>
                            <FixedWidthRegButton href={detViewUrl} text='Preview' onClickFunction={''}/>
                        </div>
                        
                    </Col>
                </Row>
            </div>
}


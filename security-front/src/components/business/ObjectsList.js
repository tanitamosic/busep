// import React, { useCallback } from 'react';
// import '../../assets/styles/business.css';
// import {useEffect, useState} from 'react';
// import { Row, Col } from 'react-bootstrap';
// import { getCertificates } from '../../services/api/CertificatesApi';
// import ListedRequest from './ListedRequest';
// import ListedCertificate from './ListedCertificate';
// import { useNavigate } from 'react-router';

// export default function ObjectsList(){

//     const [objects, setObjects] = useState([]);
//     const [listedObjects, setListedObjects] = useState([]);

//     const userRole = sessionStorage.getItem("userRole");
//     const navigate = useNavigate();

//     useEffect(() => {
//         if(userRole !== "admin"){
//             navigate("/unavailable");
//         }
//     }, [navigate, userRole])

//     const dummyObjects = [
//         {
//           "givenName": "John",
//           "surname": "Doe",
//           "email": "johndoe@example.com",
//           "password": "5tr0ngP@55w0rd!",
//           "organization": "Acme Corporation",
//           "orgUnit": "Sales",
//           "country": "United States",
//           "owner": true,
//           "endDate": "2024/04/02",
//           "valid": true,
//           "id": 154
//         },
//         {
//           "givenName": "Jane",
//           "surname": "Smith",
//           "email": "janesmith@example.com",
//           "password": "P@ssw0rd123",
//           "organization": "Globex Corporation",
//           "orgUnit": "Marketing",
//           "country": "Canada",
//           "owner": false,
//           "endDate": "2024/08/02",
//           "valid": true,
//           "id": 18
//         },
//         {
//           "givenName": "Michael",
//           "surname": "Johnson",
//           "email": "michaeljohnson@example.com",
//           "password": "SecureP@55",
//           "organization": "Initech",
//           "orgUnit": "IT",
//           "country": "United Kingdom",
//           "owner": false,
//           "endDate": "2022/04/02",
//           "valid": false,
//           "id": 5
//         },
//         {
//           "givenName": "Samantha",
//           "surname": "Lee",
//           "email": "samanthalee@example.com",
//           "password": "Pa$$word!",
//           "organization": "Tech Solutions",
//           "orgUnit": "Development",
//           "country": "Australia",
//           "owner": true,
//           "endDate": "2023/02/02",
//           "valid": false,
//           "id": 7
//         },
//         {
//           "givenName": "David",
//           "surname": "Brown",
//           "email": "davidbrown@example.com",
//           "password": "C0mpl3xP@55",
//           "organization": "ABC Corporation",
//           "orgUnit": "Finance",
//           "country": "New Zealand",
//           "owner": false,
//           "endDate": "2026/06/02",
//           "valid": true,
//           "id": 37
//         }
//       ]

//     useEffect(() => {
//         setCertificates(dummyObjects);
//         // getCertificates().then(
//         //     (response) => {
//         //         setCertificates(!!response ? response.data : []);
//         //     }
//         // )
//     }, [])

//     useEffect(() => {
//         if (objects !== undefined){
//             console.log(objects)
//             let mappedCertificates = objects.map((certificate) => <ListedCertificate certificate={certificate} key={certificate.email}/>)
//             setListedObjects(mappedObjects);
//         }
//     }, [objects])

//     return <>
//             <center><h3>Objects</h3></center>
//             <br/>

//             <Row>
//                 <Col sm="2">
//                     Id
//                 </Col>
//                 <Col sm="2">
//                     Fullname
//                 </Col>
//                 <Col sm="2">
//                     Email
//                 </Col>
//                 <Col sm="2">
//                     End date
//                 </Col>
//                 <Col sm="2">
//                     Valid
//                 </Col>
//                 <Col sm="2" />
//             </Row>

//             {listedCertificates}
//             {/* just gives nice space in the bottom */}
//             <p className='mt-3'></p> 
//         </>
// }


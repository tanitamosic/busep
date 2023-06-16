import React, { useCallback } from 'react';
import '../../assets/styles/business.css';
import {useEffect, useState} from 'react';
import { Row, Col, Button } from 'react-bootstrap';
import { getCertificates } from '../../services/api/CertificatesApi';
import ListedRequest from './ListedRequest';
import ListedCertificate from './ListedCertificate';
import { useNavigate } from 'react-router';
import ListedObject from './ListedObject';
import { getAllClients, postClientFilterRequest, sendFilterClientsRequest } from '../../services/api/UserApi';
import { getRole } from '../../services/utils/AuthService';
import ListedClient from './ListedClient';
import LabeledInput from '../forms/LabeledInput';
import { checkLettersInput, checkEmailInput } from '../../services/utils/InputValidation';

export default function ClientsList(){

    const [clients, setClients] = useState();
    const [listedClients, setListedClients] = useState([]);
    const [filterName, setFilterName] = useState("");
    const [filterSurname, setFilterSurname] = useState("");
    const [filterEmail, setFilterEmail] = useState("");
    const [filterRole, setFilterRole] = useState("");

    useEffect(() => {
        const filterParams = JSON.parse(sessionStorage.getItem("filterParams"));

        if (!filterParams){
            getAllClients().then(
                (response) => {
                    setClients(!!response ? response.data : []);
                }
            )
        } else {
            setFilterName(filterParams.filterName);
            setFilterSurname(filterParams.filterSurname);
            setFilterEmail(filterParams.filterEmail);
            setFilterRole(filterParams.filterRole);
            
            sendFilterClientsRequest(filterParams).then(
                (response) => {
                    console.log(response);
                    setClients(!!response ? response.data : []);
                    
                }, (error) => {
                  console.log(error);
                }
            );
        }
    }, [])

    useEffect(() => {
        if (!!clients){
            console.log(clients)
            let mappedClients = clients.map((client) => <ListedClient client={client} key={client.email}/>)
            setListedClients(mappedClients);
        }
    }, [clients])

    
  
    const validateInput = () => {
        let valid = (checkLettersInput(filterName) || filterName.length === 0 ) && 
                    (checkLettersInput(filterSurname) || filterSurname.length === 0 ) && 
                    // (checkEmailInput(filterEmail) || filterEmail.length === 0 ) && 
                    (filterRole.length === 0 || filterRole.toLowerCase() === "renter" || filterRole.toLowerCase() === "owner" ) 
                    ;
  
        return valid;
    }

    const resetButtonPressed = (e) => {
        e.preventDefault();
        getAllClients().then(
            (response) => {
                setClients(!!response ? response.data : []);
            }
        );
        sessionStorage.removeItem("filterParams");
        setFilterName("");
        setFilterSurname("");
        setFilterEmail("");
        setFilterRole("");
    }

    const filterButtonPressed = (e) => {
        if (validateInput()) {
            postFilterClientsRequest(e);
        } else {
          console.log("Invalid input")
          alert("Invalid input")
        }
    }

    const postFilterClientsRequest = useCallback(
        (e) => {
            e.preventDefault();

            const filterParams = {filterName, filterSurname, filterEmail, filterRole}
            sessionStorage.setItem("filterParams", JSON.stringify(filterParams));

            sendFilterClientsRequest(filterParams).then(
                (response) => {
                    console.log(response);
                    setClients(!!response ? response.data : []);
                    
                }, (error) => {
                  console.log(error);
                }
            );
        }, [filterName, filterSurname, filterEmail, filterRole]
    )

    return <>
            <center><h3>Clients</h3></center>
            <br/>
            <div className="borderedBlock">
                {/* TODO search form */}
                <LabeledInput value={filterName} label="Name" inputName="name" placeholder="Type client name" required onChangeFunc={setFilterName}/>
                <LabeledInput value={filterSurname} label="Last name" inputName="lastname" placeholder="Type client lastname" required onChangeFunc={setFilterSurname}/>
                <LabeledInput value={filterEmail} label="Email" inputName="email" placeholder="Type client email" required onChangeFunc={setFilterEmail}/>
                <LabeledInput value={filterRole} label="Role" inputName="role" placeholder="Type client role" required onChangeFunc={setFilterRole}/>
                
                <Row className='mt-2'>
                        <Col sm={2}/>
                        <Col sm={4} align='center'>
                            <Button className='formButton' onClick={filterButtonPressed}>
                                Filter
                            </Button>
                        </Col>
                        <Col sm={4} align='center'>
                            <Button className='formButton' onClick={resetButtonPressed}>
                                Reset
                            </Button>
                        </Col>
                        <Col sm={2}/>
                  </Row> 
            </div>
            <br/>
            <Row>
                <Col sm="3">
                    Name
                </Col>
                <Col sm="3">
                    Surname
                </Col>
                <Col sm="3">
                    Email
                </Col>
                <Col sm="1">
                    Role
                </Col>
                <Col sm="1" />
            </Row>

            {listedClients}
            {/* just gives nice space in the bottom */}
            <p className='mt-3'></p> 
        </>
}


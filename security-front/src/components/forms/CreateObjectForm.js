import {useState, useEffect, useRef, useCallback} from 'react';
import {checkLettersInput, checkEmailInput, checkPasswordInput } from '../../services/utils/InputValidation';
import { Form, Button, Container, Col, Row} from 'react-bootstrap';
import { sendRegistrationRequest } from '../../services/api/LoginApi';
import LabeledInput from './LabeledInput';
import '../../assets/styles/buttons.css';
import { useNavigate  } from "react-router-dom";  
import { sendObjectCreationRequest } from '../../services/api/ObjectsApi';

export function CreateObjectForm() {
    const [ownerEmail, setOwnerEmail] = useState("");
    const [renterEmail, setRenterEmail] = useState("");
    const [address, setAddress] = useState("");
    const [devices, setDevices] = useState([]);

    const navigate = useNavigate();

    const createButtonPressed = (e) => {
      if (validateInput()) {
        postCreationObjectRequest(e);
      } else {
        console.log("Invalid input")
        alert("Invalid input")
      }
    }

    const validateInput = () => {
      let valid = (checkEmailInput(ownerEmail) && ownerEmail.length > 0 ) && 
                  (checkEmailInput(renterEmail) && renterEmail.length > 0 ) && 
                  address.length > 0
                  ;
        
        return valid;
    }

    const postCreationObjectRequest = useCallback(
        (e) => {
            e.preventDefault();

            const objectJson = {ownerEmail, renterEmail, address, devices}
            // console.log(userJson)
            sendObjectCreationRequest(objectJson).then(
                (response) => {
                    console.log(response);
                    alert("Object created");
                    navigate("/admin/objects/" + ownerEmail);
                }, (error) => {
                  console.log(error);
                }
            );
        }, [ownerEmail, renterEmail, address, devices, navigate]
    )

    return (<>
    <Row className='mt-5' >
        <Col sm={2} />
        <div className="borderedBlock">
            <Col sm={true} >
                <Form>
                  <Row className='mt-2'>
                        <Col sm={4}/>
                        <Col sm={4} align='center'>
                            Object Creation
                        </Col>
                        <Col sm={4}/>
                  </Row> 

                  <LabeledInput value={ownerEmail} label="Owner Email" inputName="ownerEmail" placeholder="Type owner email" required onChangeFunc={setOwnerEmail}/>
                  <LabeledInput value={renterEmail} label="Renter Email" inputName="renterEmail" placeholder="Type renter email" required onChangeFunc={setRenterEmail}/>
                  <LabeledInput value={address} label="Address" inputName="address" placeholder="Type object address" required onChangeFunc={setAddress}/>
                  

                  <Row className='mt-2'>
                        <Col sm={4}/>
                        <Col sm={4} align='center'>
                            <Button className='formButton' onClick={createButtonPressed}>
                                Create
                            </Button>
                        </Col>
                        <Col sm={4}/>
                  </Row> 
                </Form>
            </Col>
        </div>
        <Col sm={2} />
    </Row>
    </>
    );
}
import {useState, useEffect, useRef, useCallback} from 'react';
import {checkLettersInput, checkEmailInput, checkPasswordInput } from '../../services/utils/InputValidation';
import { Form, Button, Container, Col, Row} from 'react-bootstrap';
import { sendRegistrationRequest } from '../../services/api/LoginApi';
import LabeledInput from './LabeledInput';
import '../../assets/styles/buttons.css';
import { useNavigate  } from "react-router-dom";  
import { sendObjectUpdateRequest, sendRemoveRenterRequest} from '../../services/api/ObjectsApi';

export function UpdateObjectForm({id}) {
    const [ownerEmail, setOwnerEmail] = useState("");
    const [renterEmail, setRenterEmail] = useState("");
    const [address, setAddress] = useState("");

    const createButtonPressed = (e) => {
      if (validateInput()) {
        putUpdateObjectRequest(e);
      } else {
        console.log("Invalid input")
        alert("Invalid input")
      }
    }

    const validateInput = () => {
      let valid = (checkEmailInput(ownerEmail) || ownerEmail.length === 0 ) && 
                  (checkEmailInput(renterEmail) || renterEmail.length === 0 ) &&
                  !(renterEmail.length === 0 && ownerEmail.length === 0 && address.length === 0)
                  ;
        
        return valid;
    }

    const putUpdateObjectRequest = useCallback(
        (e) => {
            e.preventDefault();

            const objectJson = {id, ownerEmail, renterEmail, address}
            // console.log(userJson)
            sendObjectUpdateRequest(objectJson).then(
                (response) => {
                    console.log(response);
                    window.location.reload();
                }, (error) => {
                  console.log(error);
                }
            );
        }, [id, ownerEmail, renterEmail, address]
    )

    const postRemoveRenterRequest = useCallback(
        (e) => {
            e.preventDefault();

            sendRemoveRenterRequest(id).then(
                (response) => {
                    console.log(response);
                    window.location.reload();
                }, (error) => {
                  console.log(error);
                }
            );
        }, [id]
    )

    return (<>
            <Form>
                <LabeledInput value={ownerEmail} label="Owner Email" inputName="ownerEmail" placeholder="Type owner email" required onChangeFunc={setOwnerEmail}/>
                <LabeledInput value={renterEmail} label="Renter Email" inputName="renterEmail" placeholder="Type renter email" required onChangeFunc={setRenterEmail}/>
                <LabeledInput value={address} label="Address" inputName="address" placeholder="Type object address" required onChangeFunc={setAddress}/>
                

                <Row className='mt-2'>
                    <Col sm={4}/>
                    <Col sm={4} align='center'>
                        <Button className='formButton' onClick={createButtonPressed}>
                            Update
                        </Button>
                    </Col>
                    <Col sm={4}/>
                </Row> 
                <Row className='mt-2'>
                    <Col sm={4}/>
                    <Col sm={4} align='center'>
                        <Button className='formButton' onClick={postRemoveRenterRequest}>
                            Remove renter
                        </Button>
                    </Col>
                    <Col sm={4}/>
                </Row> 
            </Form>
        </>
    );
}
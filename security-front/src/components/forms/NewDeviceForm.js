import {useState, useEffect, useRef, useCallback} from 'react';
import {checkLettersInput, checkEmailInput, checkPasswordInput, checkNumInput } from '../../services/utils/InputValidation';
import { Form, Button, Container, Col, Row} from 'react-bootstrap';
import LabeledInput from './LabeledInput';
import '../../assets/styles/buttons.css';
import { useNavigate  } from "react-router-dom";  
import { sendDeviceCreationRequest } from '../../services/api/ObjectsApi';

export function NewDeviceForm({houseId}) {
    const [name, setName] = useState("");
    const [type, setType] = useState("SMART_CAM");
    const [readTime, setReadTime] = useState("");
    const [filterRegex, setRegex] = useState("");

    const possibleTypes = ["SMART_CAM", "SMART_LIGHT", "SMART_LOCK", "SMART_SMOKE", "SMART_TEMP"];

    // const navigate = useNavigate();

    const createButtonPressed = (e) => {
      if (validateInput()) {
        postDeviceCreationRequest(e);
      } else {
        console.log("Invalid input")
        alert("Invalid input")
      }
    }

    const validateInput = () => {
      let valid = (name.length > 0 ) && 
                  possibleTypes.includes(type) &&
                  checkNumInput(readTime) &&
                  filterRegex.length > 0
                  ;
        
        return valid;
    }

    const postDeviceCreationRequest = useCallback(
        (e) => {
            e.preventDefault();

            const deviceJson = {houseId, type, readTime, regex: filterRegex, name}
            sendDeviceCreationRequest(deviceJson).then(
                (response) => {
                    console.log(response);
                    window.location.reload();
                }, (error) => {
                  console.log(error);
                }
            );
        }, [houseId, type, readTime, filterRegex, name]
    )

    const handleTypeChange = (event) => {
        setType(event.target.value);
      };

    return (<>
        <Form>

            <LabeledInput value={name} label="Device name" inputName="deviceName" placeholder="Type device name" required onChangeFunc={setName}/>

            <Row className='mt-2'>
                <Col sm={4}/>
                <Col sm={4} align='center'>
                    <Form.Label>Select device type:</Form.Label>
                    <Form.Select onChange={handleTypeChange}>
                        {possibleTypes.map((option, index) => (
                            <option key={index} value={option}>{option}</option>
                        ))}
                    </Form.Select>
                </Col>
                <Col sm={4}/>
            </Row> 

            <LabeledInput value={readTime} label="Read time" inputName="readTime" placeholder="Type read time" required onChangeFunc={setReadTime}/>
            <LabeledInput value={filterRegex} label="Regex" inputName="regex" placeholder="Type device regex" required onChangeFunc={setRegex}/>
            
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
    </>
    );
}
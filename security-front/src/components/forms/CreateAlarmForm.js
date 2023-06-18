import React, { useCallback } from 'react';
import '../../assets/styles/business.css';
import {useEffect, useState} from 'react';
import { Row, Col, Button, Form } from 'react-bootstrap';
import LabeledInput from '../forms/LabeledInput';
import { checkLettersInput, checkEmailInput, checkNumInput } from '../../services/utils/InputValidation';
import { postCreateAlarmRequest } from '../../services/api/ObjectsApi';


export default function CreateAlarmForm(){

    const [deviceType, setDeviceType] = useState("SMART_CAM");
    const [logType, setMessageType] = useState("INFO");
    const [frequency, setFrequency] = useState("1")
    const [window, setWindow] = useState("1")

    const possibleDeviceTypes = ["SMART_CAM", "SMART_LIGHT", "SMART_LOCK", "SMART_SMOKE", "SMART_TEMP"];
    const possibleLogTypes = ["INFO", "WARN", "ERROR"];

    const validateInput = () => {
        let valid = (checkNumInput(frequency) && frequency > 0 && frequency < 101 ) && 
                    (checkNumInput(window) && window > 0 && window < 61 ) && 
                    (possibleDeviceTypes.includes(deviceType)) && 
                    (possibleLogTypes.includes(logType))
                    ;
  
        return valid;
    }

    const createAlarmButtonPressed = (e) => {
        if (validateInput()) {
            createAlarm(e);
        } else {
          console.log("Invalid input")
          alert("Invalid input")
        }
    }

    // when filter button pressed
    const createAlarm = useCallback(
        (e) => {
            e.preventDefault();

            const alarmDto = {deviceType, logType, frequency, window};

            postCreateAlarmRequest(alarmDto).then(
                (response) => {
                    console.log(response);
                    window.location.reload();
                }
            )
        }, []
    )

    const handleDeviceTypeChange = (event) => {
        setDeviceType(event.target.value);
    };

    const handleLogTypeChange = (event) => {
        setMessageType(event.target.value);
    };

    return <>
            <center><h3>Create alarm</h3></center>
            <br/>
            <div className="borderedBlock">
                <Row className='mt-2'>
                    <Col sm={4}/>
                    <Col sm={4} align='center'>
                        <Form.Label>Select device type:</Form.Label>
                        <Form.Select onChange={handleDeviceTypeChange}>
                            {possibleDeviceTypes.map((option, index) => (
                                <option key={index} value={option}>{option}</option>
                            ))}
                        </Form.Select>
                    </Col>
                    <Col sm={4}/>
                </Row> 

                <Row className='mt-2'>
                    <Col sm={4}/>
                    <Col sm={4} align='center'>
                        <Form.Label>Select log type:</Form.Label>
                        <Form.Select onChange={handleLogTypeChange}>
                            {possibleLogTypes.map((option, index) => (
                                <option key={index} value={option}>{option}</option>
                            ))}
                        </Form.Select>
                    </Col>
                    <Col sm={4}/>
                </Row> 

                <LabeledInput value={frequency} label="Frequency" inputName="frequency" placeholder="Type frequency" required onChangeFunc={setFrequency}/>
                <LabeledInput value={window} label="Window" inputName="window" placeholder="Type window" required onChangeFunc={setWindow}/>
                
                <Row className='mt-2'>
                        <Col sm={4}/>
                        <Col sm={4} align='center'>
                            <Button className='formButton' onClick={createAlarmButtonPressed}>
                                Create alarm
                            </Button>
                        </Col>
                        <Col sm={4}/>
                  </Row> 
            </div>

            {/* just gives nice space in the bottom */}
            <p className='mt-3'></p> 
        </>
}
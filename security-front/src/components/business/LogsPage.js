import React, { useCallback } from 'react';
import '../../assets/styles/business.css';
import {useEffect, useState} from 'react';
import { Row, Col, Button, Form } from 'react-bootstrap';
import { getCertificates } from '../../services/api/CertificatesApi';
import ListedRequest from './ListedRequest';
import ListedCertificate from './ListedCertificate';
import { useNavigate } from 'react-router';
import ListedObject from './ListedObject';
import { getAllClients, postClientFilterRequest, sendFilterClientsRequest } from '../../services/api/UserApi';
import { getRole } from '../../services/utils/AuthService';
import ListedClient from './ListedClient';
import LabeledInput from '../forms/LabeledInput';
import { checkLettersInput, checkEmailInput, checkNumInput } from '../../services/utils/InputValidation';
import LogsViewer from './LogsViewer';
import { getPreviosLogs } from '../../services/api/ObjectsApi';

export default function LogsPage(){
    //ListedLog

    const [houseId, setHouseId] = useState();
    const [deviceType, setDeviceType] = useState();
    const [logType, setMessageType] = useState();
    const [regex, setRegex ] = useState();

    const [logs, setLogs ] = useState();
    const [listedLogs, setListedLogs] = useState();

    const possibleDeviceTypes = ["ALL", "SMART_CAM", "SMART_LIGHT", "SMART_LOCK", "SMART_SMOKE", "SMART_TEMP"];
    const possibleLogTypes = ["ALL", "INFO", "WARN", "ERROR", "ALARM"];
    
    const dummyLogs = [
        {
            "id": "f012e5f5-ac1e-4798-bd02-0c2f8ba8a1fe",
            "timestamp": [2023, 6, 17, 18, 3, 28],
            "deviceId": 1,
            "deviceType": "SMART_CAM",
            "ipAddress": "0:0:0:0:0:0:0:1",
            "message": "Motion detected",
            "logType": "INFO",
            "houseId": 1
          },
          {
            "id": "8e5679ac-874b-43a7-a788-2d1c3f82c34b",
            "timestamp": [2023, 6, 17, 18, 5, 12],
            "deviceId": 2,
            "deviceType": "SMART_LIGHT",
            "ipAddress": "0:0:0:0:0:0:0:1",
            "message": "Light turned on",
            "logType": "INFO",
            "houseId": 2
          },
          {
            "id": "9bf0c610-62f0-41d6-8a27-8d4e583e77d8",
            "timestamp": [2023, 6, 17, 18, 7, 45],
            "deviceId": 3,
            "deviceType": "SMART_LOCK",
            "ipAddress": "0:0:0:0:0:0:0:1",
            "message": "Door locked",
            "logType": "INFO",
            "houseId": 1
          },
          {
            "id": "293d6e59-4b61-4866-87dd-95f4ac9721d2",
            "timestamp": [2023, 6, 17, 18, 9, 20],
            "deviceId": 4,
            "deviceType": "SMART_SMOKE",
            "ipAddress": "0:0:0:0:0:0:0:1",
            "message": "Smoke detected",
            "logType": "WARN",
            "houseId": 3
          },
          {
            "id": "db269f7a-9c52-4fd1-b02a-37e27ee9b8c9",
            "timestamp": [2023, 6, 17, 18, 11, 5],
            "deviceId": 5,
            "deviceType": "SMART_TEMP",
            "ipAddress": "0:0:0:0:0:0:0:1",
            "message": "Temperature above threshold",
            "logType": "ERROR",
            "houseId": 1
          },
          {
          "id": "f012e5f5-ac1e-4798-bd02-0c2f8ba8a1fe",
            "timestamp": [2023, 6, 17, 18, 3, 28],
            "deviceId": 1,
            "deviceType": "SMART_CAM",
            "ipAddress": "0:0:0:0:0:0:0:1",
            "message": "Motion detected",
            "logType": "INFO",
            "houseId": 1
          },
          {
            "id": "8e5679ac-874b-43a7-a788-2d1c3f82c34b",
            "timestamp": [2023, 6, 17, 18, 5, 12],
            "deviceId": 2,
            "deviceType": "SMART_LIGHT",
            "ipAddress": "0:0:0:0:0:0:0:1",
            "message": "Light turned on",
            "logType": "INFO",
            "houseId": 2
          },
          {
            "id": "9bf0c610-62f0-41d6-8a27-8d4e583e77d8",
            "timestamp": [2023, 6, 17, 18, 7, 45],
            "deviceId": 3,
            "deviceType": "SMART_LOCK",
            "ipAddress": "0:0:0:0:0:0:0:1",
            "message": "Door locked",
            "logType": "ALARM",
            "houseId": 1
          },
          {
            "id": "9bf0c610-62f0-41d6-8a27-8d4e583e77d8",
            "timestamp": [2023, 6, 17, 18, 7, 45],
            "deviceId": 3,
            "deviceType": "SMART_LOCK",
            "ipAddress": "0:0:0:0:0:0:0:1",
            "message": "Door locked",
            "logType": "INFO",
            "houseId": 1
          },
          {
            "id": "293d6e59-4b61-4866-87dd-95f4ac9721d2",
            "timestamp": [2023, 6, 17, 18, 9, 20],
            "deviceId": 4,
            "deviceType": "SMART_SMOKE",
            "ipAddress": "0:0:0:0:0:0:0:1",
            "message": "Smoke detected",
            "logType": "WARN",
            "houseId": 3
          },
          {
            "id": "db269f7a-9c52-4fd1-b02a-37e27ee9b8c9",
            "timestamp": [2023, 6, 17, 18, 11, 5],
            "deviceId": 5,
            "deviceType": "SMART_TEMP",
            "ipAddress": "0:0:0:0:0:0:0:1",
            "message": "Temperature above threshold",
            "logType": "ERROR",
            "houseId": 1
          },
          {
            "id": "f012e5f5-ac1e-4798-bd02-0c2f8ba8a1fe",
              "timestamp": [2023, 6, 17, 18, 3, 28],
              "deviceId": 1,
              "deviceType": "SMART_CAM",
              "ipAddress": "0:0:0:0:0:0:0:1",
              "message": "Motion detected",
              "logType": "INFO",
              "houseId": 1
            },
            {
              "id": "8e5679ac-874b-43a7-a788-2d1c3f82c34b",
              "timestamp": [2023, 6, 17, 18, 5, 12],
              "deviceId": 2,
              "deviceType": "SMART_LIGHT",
              "ipAddress": "0:0:0:0:0:0:0:1",
              "message": "Light turned on",
              "logType": "INFO",
              "houseId": 2
            },
            {
              "id": "9bf0c610-62f0-41d6-8a27-8d4e583e77d8",
              "timestamp": [2023, 6, 17, 18, 7, 45],
              "deviceId": 3,
              "deviceType": "SMART_LOCK",
              "ipAddress": "0:0:0:0:0:0:0:1",
              "message": "Door locked",
              "logType": "INFO",
              "houseId": 1
            },
            {
            "id": "f012e5f5-ac1e-4798-bd02-0c2f8ba8a1fe",
                "timestamp": [2023, 6, 17, 18, 3, 28],
                "deviceId": 1,
                "deviceType": "SMART_CAM",
                "ipAddress": "0:0:0:0:0:0:0:1",
                "message": "Motion detected",
                "logType": "INFO",
                "houseId": 1
            },
            {
                "id": "8e5679ac-874b-43a7-a788-2d1c3f82c34b",
                "timestamp": [2023, 6, 17, 18, 5, 12],
                "deviceId": 2,
                "deviceType": "SMART_LIGHT",
                "ipAddress": "0:0:0:0:0:0:0:1",
                "message": "Light turned on",
                "logType": "INFO",
                "houseId": 2
            },
            {
                "id": "9bf0c610-62f0-41d6-8a27-8d4e583e77d8",
                "timestamp": [2023, 6, 17, 18, 7, 45],
                "deviceId": 3,
                "deviceType": "SMART_LOCK",
                "ipAddress": "0:0:0:0:0:0:0:1",
                "message": "Door locked",
                "logType": "INFO",
                "houseId": 1
            }
    ]

    useEffect(() => {
        setLogs(dummyLogs);
    }, [])

    useEffect(() => {
        const filterParams = JSON.parse(sessionStorage.getItem("logsFilterParams"));

        if (!filterParams){
            //  TODO 
            getPreviosLogs({}).then(
                (response) => {
                    if (!!response && response.data) {
                        setLogs(response.data);
                    } else {
                    setLogs(dummyLogs);
                    }
                }
                // TODO connect to websocket
            )
        } else {
            setHouseId(filterParams.houseId);
            setDeviceType(filterParams.deviceType);
            setMessageType(filterParams.messageType);
            setRegex(filterParams.regex);
            
            getPreviosLogs(filterParams).then(
                (response) => {
                    if (!!response && response.data) {
                        setLogs(response.data);
                    } else {
                    setLogs(dummyLogs);
                    }
                }
                // TODO connect to websocket
            )
        }
    }, [])
  
    const validateInput = () => {
        let valid = (checkNumInput(houseId) || houseId.length === 0 ) && 
                    (checkLettersInput(deviceType) || deviceType.length === 0 ) && 
                    (checkLettersInput(logType) || logType.length === 0 )
                    // for regex no need to check
                    ;
  
        return valid;
    }

    const resetButtonPressed = (e) => {
        e.preventDefault();

        getPreviosLogs({}).then(
            (response) => {
                if (!!response && response.data) {
                    setLogs(response.data);
                } else {
                setLogs(dummyLogs);
                }
            }
            // TODO connect to websocket
        )

        sessionStorage.removeItem("logsFilterParams");
        setHouseId("");
        setDeviceType("");
        setMessageType("");
        setRegex("");
    }

    const filterButtonPressed = (e) => {
        if (validateInput()) {
            postFilterLogsRequest(e);
        } else {
          console.log("Invalid input")
          alert("Invalid input")
        }
    }

    const postFilterLogsRequest = useCallback(
        (e) => {
            e.preventDefault();

            const filterParams = {houseId, deviceType, messageType: logType, regex}
            sessionStorage.setItem("logsFilterParams", JSON.stringify(filterParams));

            getPreviosLogs(filterParams).then(
                (response) => {
                    if (!!response && response.data) {
                        setLogs(response.data);
                    } else {
                    setLogs(dummyLogs);
                    }
                }
                // TODO connect to websocket
            )
        }, [houseId, deviceType, logType, regex]
    )

    const handleTypeChange = (event) => {
        setDeviceType(event.target.value);
      };

    if (!!logs){
    return <>
            <center><h3>Logs</h3></center>
            <br/>
            <div className="borderedBlock">
                <LabeledInput value={houseId} label="House id" inputName="houseId" placeholder="Type house id" required onChangeFunc={setHouseId}/>

                <Row className='mt-2'>
                    <Col sm={4}/>
                    <Col sm={4} align='center'>
                        <Form.Label>Select device type:</Form.Label>
                        <Form.Select onChange={handleTypeChange}>
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
                        <Form.Select onChange={handleTypeChange}>
                            {possibleLogTypes.map((option, index) => (
                                <option key={index} value={option}>{option}</option>
                            ))}
                        </Form.Select>
                    </Col>
                    <Col sm={4}/>
                </Row> 

                <LabeledInput value={regex} label="Regex" inputName="regex" placeholder="Type regex" required onChangeFunc={setRegex}/>
                
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

            <LogsViewer logs={logs} />
            {/* just gives nice space in the bottom */}
            <p className='mt-3'></p> 
        </>
    }

}


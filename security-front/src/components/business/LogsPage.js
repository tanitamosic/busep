import React, { useCallback } from 'react';
import '../../assets/styles/business.css';
import {useEffect, useState} from 'react';
import { Row, Col, Button, Form } from 'react-bootstrap';
import LabeledInput from '../forms/LabeledInput';
import { checkLettersInput, checkEmailInput, checkNumInput } from '../../services/utils/InputValidation';
import LogsViewer from './LogsViewer';
import { getPreviosLogs } from '../../services/api/ObjectsApi';
import { useWsLogs } from '../../secureStore/authSlice';

export default function LogsPage(){

    const [houseId, setHouseId] = useState("");
    const [deviceType, setDeviceType] = useState("");
    const [logType, setMessageType] = useState("");
    const [regex, setRegex ] = useState("");

    const [filterParams, setFilterParams] = useState();
    const [prevWsLogs, setPrevWcLogs] = useState();

    const [logs, setLogs ] = useState();
    const { wsLogs, updateWsLogs } = useWsLogs();

    const possibleDeviceTypes = ["", "SMART_CAM", "SMART_LIGHT", "SMART_LOCK", "SMART_SMOKE", "SMART_TEMP"];
    const possibleLogTypes = ["", "INFO", "WARN", "ERROR", "ALARM"];
    
    const dummyLogs = [
        // {
        //     "id": "f012e5f5-ac1e-4798-bd02-0c2f8ba8a1fe",
        //     "timestamp": [2023, 6, 17, 18, 3, 28],
        //     "deviceId": 1,
        //     "deviceType": "SMART_CAM",
        //     "ipAddress": "0:0:0:0:0:0:0:1",
        //     "message": "Motion detected",
        //     "logType": "INFO",
        //     "houseId": 1
        //   },
        //   {
        //     "id": "8e5679ac-874b-43a7-a788-2d1c3f82c34b",
        //     "timestamp": [2023, 6, 17, 18, 5, 12],
        //     "deviceId": 2,
        //     "deviceType": "SMART_LIGHT",
        //     "ipAddress": "0:0:0:0:0:0:0:1",
        //     "message": "Light turned on",
        //     "logType": "INFO",
        //     "houseId": 2
        //   },
        //   {
        //     "id": "9bf0c610-62f0-41d6-8a27-8d4e583e77d8",
        //     "timestamp": [2023, 6, 17, 18, 7, 45],
        //     "deviceId": 3,
        //     "deviceType": "SMART_LOCK",
        //     "ipAddress": "0:0:0:0:0:0:0:1",
        //     "message": "Door locked",
        //     "logType": "INFO",
        //     "houseId": 1
        //   },
        //   {
        //     "id": "293d6e59-4b61-4866-87dd-95f4ac9721d2",
        //     "timestamp": [2023, 6, 17, 18, 9, 20],
        //     "deviceId": 4,
        //     "deviceType": "SMART_SMOKE",
        //     "ipAddress": "0:0:0:0:0:0:0:1",
        //     "message": "Smoke detected",
        //     "logType": "WARN",
        //     "houseId": 3
        //   },
        //   {
        //     "id": "db269f7a-9c52-4fd1-b02a-37e27ee9b8c9",
        //     "timestamp": [2023, 6, 17, 18, 11, 5],
        //     "deviceId": 5,
        //     "deviceType": "SMART_TEMP",
        //     "ipAddress": "0:0:0:0:0:0:0:1",
        //     "message": "Temperature above threshold",
        //     "logType": "ERROR",
        //     "houseId": 1
        //   },
        //   {
        //   "id": "f012e5f5-ac1e-4798-bd02-0c2f8ba8a1fe",
        //     "timestamp": [2023, 6, 17, 18, 3, 28],
        //     "deviceId": 1,
        //     "deviceType": "SMART_CAM",
        //     "ipAddress": "0:0:0:0:0:0:0:1",
        //     "message": "Motion detected",
        //     "logType": "INFO",
        //     "houseId": 1
        //   },
        //   {
        //     "id": "8e5679ac-874b-43a7-a788-2d1c3f82c34b",
        //     "timestamp": [2023, 6, 17, 18, 5, 12],
        //     "deviceId": 2,
        //     "deviceType": "SMART_LIGHT",
        //     "ipAddress": "0:0:0:0:0:0:0:1",
        //     "message": "Light turned on",
        //     "logType": "INFO",
        //     "houseId": 2
        //   },
        //   {
        //     "id": "9bf0c610-62f0-41d6-8a27-8d4e583e77d8",
        //     "timestamp": [2023, 6, 17, 18, 7, 45],
        //     "deviceId": 3,
        //     "deviceType": "SMART_LOCK",
        //     "ipAddress": "0:0:0:0:0:0:0:1",
        //     "message": "Door locked",
        //     "logType": "ALARM",
        //     "houseId": 1
        //   },
        //   {
        //     "id": "9bf0c610-62f0-41d6-8a27-8d4e583e77d8",
        //     "timestamp": [2023, 6, 17, 18, 7, 45],
        //     "deviceId": 3,
        //     "deviceType": "SMART_LOCK",
        //     "ipAddress": "0:0:0:0:0:0:0:1",
        //     "message": "Door locked",
        //     "logType": "INFO",
        //     "houseId": 1
        //   },
        //   {
        //     "id": "293d6e59-4b61-4866-87dd-95f4ac9721d2",
        //     "timestamp": [2023, 6, 17, 18, 9, 20],
        //     "deviceId": 4,
        //     "deviceType": "SMART_SMOKE",
        //     "ipAddress": "0:0:0:0:0:0:0:1",
        //     "message": "Smoke detected",
        //     "logType": "WARN",
        //     "houseId": 3
        //   },
        //   {
        //     "id": "db269f7a-9c52-4fd1-b02a-37e27ee9b8c9",
        //     "timestamp": [2023, 6, 17, 18, 11, 5],
        //     "deviceId": 5,
        //     "deviceType": "SMART_TEMP",
        //     "ipAddress": "0:0:0:0:0:0:0:1",
        //     "message": "Temperature above threshold",
        //     "logType": "ERROR",
        //     "houseId": 1
        //   },
        //   {
        //     "id": "f012e5f5-ac1e-4798-bd02-0c2f8ba8a1fe",
        //       "timestamp": [2023, 6, 17, 18, 3, 28],
        //       "deviceId": 1,
        //       "deviceType": "SMART_CAM",
        //       "ipAddress": "0:0:0:0:0:0:0:1",
        //       "message": "Motion detected",
        //       "logType": "INFO",
        //       "houseId": 1
        //     },
        //     {
        //       "id": "8e5679ac-874b-43a7-a788-2d1c3f82c34b",
        //       "timestamp": [2023, 6, 17, 18, 5, 12],
        //       "deviceId": 2,
        //       "deviceType": "SMART_LIGHT",
        //       "ipAddress": "0:0:0:0:0:0:0:1",
        //       "message": "Light turned on",
        //       "logType": "INFO",
        //       "houseId": 2
        //     },
        //     {
        //       "id": "9bf0c610-62f0-41d6-8a27-8d4e583e77d8",
        //       "timestamp": [2023, 6, 17, 18, 7, 45],
        //       "deviceId": 3,
        //       "deviceType": "SMART_LOCK",
        //       "ipAddress": "0:0:0:0:0:0:0:1",
        //       "message": "Door locked",
        //       "logType": "INFO",
        //       "houseId": 1
        //     },
        //     {
        //     "id": "f012e5f5-ac1e-4798-bd02-0c2f8ba8a1fe",
        //         "timestamp": [2023, 6, 17, 18, 3, 28],
        //         "deviceId": 1,
        //         "deviceType": "SMART_CAM",
        //         "ipAddress": "0:0:0:0:0:0:0:1",
        //         "message": "Motion detected",
        //         "logType": "INFO",
        //         "houseId": 1
        //     },
        //     {
        //         "id": "8e5679ac-874b-43a7-a788-2d1c3f82c34b",
        //         "timestamp": [2023, 6, 17, 18, 5, 12],
        //         "deviceId": 2,
        //         "deviceType": "SMART_LIGHT",
        //         "ipAddress": "0:0:0:0:0:0:0:1",
        //         "message": "Light turned on",
        //         "logType": "INFO",
        //         "houseId": 2
        //     },
        //     {
        //         "id": "9bf0c610-62f0-41d6-8a27-8d4e583e77d8",
        //         "timestamp": [2023, 6, 17, 18, 7, 45],
        //         "deviceId": 3,
        //         "deviceType": "SMART_LOCK",
        //         "ipAddress": "0:0:0:0:0:0:0:1",
        //         "message": "Door locked",
        //         "logType": "INFO",
        //         "houseId": 1,
        //         "genericLogType": "DEVICE"
        //     }
    ]

    useEffect(() => {
        setLogs(dummyLogs);
    }, [])

    // everytime when log comes from websocket
    useEffect(() => {
        if (!!wsLogs && (!prevWsLogs || (wsLogs.id !== prevWsLogs.id))){
            setPrevWcLogs(wsLogs)

            if (!filterParams || isFilteredWsLog(wsLogs, filterParams)){ 
                setLogs((prevLogs) => {
                    const newLogs = [...prevLogs];
                    newLogs.push(wsLogs);
                    return newLogs;
                  });
            }
        }
    }, [wsLogs, filterParams])

    const isFilteredWsLog = (wsLogsMessage, _filterParams) => {
        let valid;

        if (!!_filterParams){
            valid = isFilterParamsEmpty(_filterParams) 
                        ||
                    ((_filterParams.houseId.length === 0 || wsLogsMessage.houseId === _filterParams.houseId) && 
                    (_filterParams.deviceType.length === 0 || wsLogsMessage.deviceType === _filterParams.deviceType) && 
                    (_filterParams.logType.length === 0 || wsLogsMessage.logType === _filterParams.logType))
            // TODO what to do for regex ???
            ;
        } else {
            valid = true;
        }
        
        return valid;
    }

    const isFilterParamsEmpty = (_filterParams) => {
        let valid = ((_filterParams.houseId.length === 0 ) && 
                    (_filterParams.deviceType.length === 0) && 
                    (_filterParams.regex.length === 0) && 
                    (_filterParams.logType.length === 0 ))
                    ;
        
        return valid;
    }

    // when open the page
    useEffect(() => {
        getPreviosLogs({}).then(
            (response) => {
                if (!!response && response.data) {
                    setLogs(response.data);
                } else {
                setLogs(dummyLogs);
                }
            }
        )
        // }
    }, [])
  
    const validateInput = () => {
        let valid = (checkNumInput(houseId) || houseId.length === 0 ) && 
                    (checkLettersInput(deviceType) || deviceType.length === 0 ) && 
                    (checkLettersInput(logType) || logType.length === 0 )
                    // for regex no need to check
                    ;
  
        return valid;
    }

    // when reset filters button pressed
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
        )

        setHouseId("");
        setDeviceType("");
        setMessageType("");
        setRegex("");
        setFilterParams();
    }

    const filterButtonPressed = (e) => {
        if (validateInput()) {
            const _filterParams = {houseId, deviceType, logType, regex}
            setFilterParams(_filterParams);

            postFilterLogsRequest(e, _filterParams);
        } else {
          console.log("Invalid input")
          alert("Invalid input")
        }
    }

    // when filter button pressed
    const postFilterLogsRequest = useCallback(
        (e, _filterParams) => {
            e.preventDefault();
            if (!!_filterParams){
                getPreviosLogs(_filterParams).then(
                    (response) => {
                        if (!!response && response.data) {
                            setLogs(response.data);
                        } else {
                        setLogs(dummyLogs);
                        }
                    }
                )
            } else {
                alert("No filter params selected.")
            }
            
        }, []
    )

    const handleDeviceTypeChange = (event) => {
        setDeviceType(event.target.value);
      };

      const handleLogTypeChange = (event) => {
        setMessageType(event.target.value);
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


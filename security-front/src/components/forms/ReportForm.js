import {useState, useEffect, useRef, useCallback} from 'react';
import {checkEmailInput, checkNumInput, checkPasswordInput } from '../../services/utils/InputValidation';
import { Form, Button, Container, Col, Row} from 'react-bootstrap';
import LabeledInput from './LabeledInput';
import '../../assets/styles/buttons.css';
import { checkLettersInput, checkDateInput, isPositiveNumber } from '../../services/utils/InputValidation';
import { getLoggedUserEmail, getRole } from '../../services/utils/AuthService';
import { postReportRequest } from '../../services/api/ObjectsApi';

export function ReportForm({setReportFunc}) {
    const [fromDate, setStartDateFrom] = useState("");
    const [toDate, setStartDateTo] = useState("");
    const [reportSource, setReportSource] = useState(""); // empty for all users, 0 for all houses
    const [logType, setMessageType] = useState("");

    const possibleLogTypes = ["", "WARN", "ERROR", "ALARM"];

    const [isAdmin, setIsAdmin] = useState(false);
    const [placeholder, setPlaceholder] = useState("houseId/ 0")

    useEffect(() => {
        const role = getRole();

        if (!!role){
            setIsAdmin(role === "admin");
            setPlaceholder("user@xyz.com / houseId / empty / 0")
        }
    }, [])


    const validateInput = () => {
        let valid =   (checkDateInput(fromDate)) && 
                  (checkDateInput(toDate)) &&
                  (possibleLogTypes.includes(logType))
                  ; 

        if (isAdmin){
            valid = valid && (reportSource === "" || checkEmailInput(reportSource) || reportSource === "0" || checkNumInput(reportSource));
        } else {
            valid = valid && (reportSource === "0" || checkNumInput(reportSource)); // TODO must check on back if user is the owner of the house
        }

      return valid;
    }

    const reportButtonPressed = (e) => {
        if (validateInput()) {
          getReport(e);
        } else {
          console.log("Invalid input")
          alert("Invalid input")
        }
      }

    const getReport = useCallback(
        (e) => {
            e.preventDefault();
            let reportDTO = {reportSource, logType, fromDate, toDate};
            console.log(reportDTO)

            if (!isAdmin && reportSource === ""){
                reportDTO.reportSource = getLoggedUserEmail();
            }

            postReportRequest(reportDTO, isAdmin).then(
                (response) => {
                    console.log(response.data);
                    setReportFunc(!!response ? response.data : {});
                }, (error) => {
                  alert("Invalid report");
                }
            );
        }, [reportSource, logType, fromDate, toDate, isAdmin, setReportFunc]
    )

    const handleLogTypeChange = (event) => {
        setMessageType(event.target.value);
    };

    return (<>
        <Row className='mt-5' >
            <Col sm={2} />
            <div className="borderedBlock">
                <Col sm={true} >
                    <Form>
                        <Row className='mt-2'>
                            <Col sm={2}/>
                            <Col sm={4} align='center'>
                                <LabeledInput value={reportSource} label="Source:" inputName="reportSource" placeholder={placeholder} required onChangeFunc={setReportSource}/>
                            </Col>
                            <Col sm={4} align='center'>
                                <Form.Label>Select log type:</Form.Label>
                                <Form.Select onChange={handleLogTypeChange}>
                                    {possibleLogTypes.map((option, index) => (
                                        <option key={index} value={option}>{option}</option>
                                    ))}
                                </Form.Select>
                            </Col>
                            <Col sm={2}/>
                        </Row> 
                        <Row className='mt-2'>
                            <Col sm={2}/>
                            <Col sm={4} align='center'>
                                <LabeledInput value={fromDate} label="From:" inputName="startDateFrom" placeholder="yyyy-mm-dd" required onChangeFunc={setStartDateFrom}/>
                            </Col>
                            <Col sm={4} align='center'>
                                <LabeledInput value={toDate} label="Until:" inputName="startDateTo" placeholder="yyyy-mm-dd" required onChangeFunc={setStartDateTo}/>
                            </Col>
                            <Col sm={2}/>
                        </Row> 
                        <Row className='mt-2'>
                            <Col sm={4}/>
                            <Col sm={4} align='center'>
                                <Button className='formButton' onClick={reportButtonPressed}>
                                    Get report
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
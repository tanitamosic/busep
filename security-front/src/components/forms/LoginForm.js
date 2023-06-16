import {useState, useEffect, useRef, useCallback} from 'react';
import {checkLettersInput, checkEmailInput, checkPasswordInput } from '../../services/utils/InputValidation';
import { Form, Button, Container, Col, Row} from 'react-bootstrap';
import { sendLoginRequest } from '../../services/api/LoginApi';
import LabeledInput from './LabeledInput';
import '../../assets/styles/buttons.css';
import { useNavigate  } from "react-router-dom";    
import { getToken, getDecodedToken } from '../../services/utils/AuthService';
import { getRole } from '../../services/utils/AuthService';
import { setAuthToken } from '../../secureStore/authSlice';
import { useDispatch } from 'react-redux';
import Cookies from 'js-cookie';

export function LoginForm() {
    

    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [pin, setPin] = useState("");

    const navigate = useNavigate ();
    const dispatch = useDispatch();
    const userRole = getRole();

    useEffect(() => {
        if(!!userRole){
            navigate("/" + userRole.toLowerCase());
        }
    }, [navigate, userRole])

    const loginButtonPressed = (e) => {
      if (validateInput()) {
        postLoginRequest(e);
      } else {
        console.log("Invalid input")
        alert("Invalid input")
      }
    }

    const validateInput = () => {
      let valid = (checkEmailInput(email) && email.length > 0 ) && 
                  password.length > 0 && 
                  pin.length > 0 
                  ;

      return valid;
    }

    const postLoginRequest = useCallback(
        (e) => {
            e.preventDefault();
            const userJson = {email, password, pin}

            sendLoginRequest(userJson).then(
                (response) => {
                    const token = response.data.jwt;
                    
                    const oneHour = new Date();
                    oneHour.setTime(oneHour.getTime() + 60 * 60 * 1000); // 1 hour in milliseconds
                    Cookies.set('token', token, { expires: oneHour, path: '/' });

                    dispatch(setAuthToken(token));
                    let role = getRole();

                    navigate("/" + role);
                    window.dispatchEvent(new Event("userRoleUpdated"));
                    return response;
                }, (error) => {
                  console.log(error);
                }
            );
        }, [dispatch, email, navigate, password, pin]
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
                                Login
                            </Col>
                            <Col sm={4}/>
                        </Row> 
                        <LabeledInput value={email} label="Email" inputName="email" placeholder="Type your email" required onChangeFunc={setEmail}/>
                        <LabeledInput value={password} label="Password" inputName="password" placeholder="Type your password" required onChangeFunc={setPassword}/>
                        <LabeledInput value={pin} label="Pin" inputName="password" placeholder="Type your PIN" required onChangeFunc={setPin}/>
    
                        <Row className='mt-2'>
                            <Col sm={4}/>
                            <Col sm={4} align='center'>
                                <Button className='formButton' onClick={loginButtonPressed}>
                                    Login
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
import React from 'react'
import '../assets/styles/nav.css';
import '../assets/styles/style.css';
import {Nav, Navbar, NavDropdown} from 'react-bootstrap';
import Stomp from 'stompjs';
import SockJS from 'sockjs-client';
import { setWsLogs, resetWsLogs } from '../secureStore/authSlice';
import { useWsLogs } from '../secureStore/authSlice';
import { getLoggedUserEmail } from '../services/utils/AuthService';
import { useState, useEffect } from 'react';

export default function ClientNavbar(){
    const logo = require('../assets/images/logosh.png')

    const { wsLogs, updateWsLogs } = useWsLogs();

    useEffect(() => {
        const connectAndSubscribe = () => {
            const socket = new SockJS('http://localhost:8081/ws');
            const stompClient = Stomp.over(socket);
    
            stompClient.connect({}, () => {
                stompClient.subscribe('/topic/log/' + getLoggedUserEmail(), (message) => {
                    const receivedMessage = JSON.parse(message.body)
                    updateWsLogs(receivedMessage);
                })
            });
    
            return () => {
                socket.close();
                stompClient.disconnect();  
                resetWsLogs();
            };
            
        };

        connectAndSubscribe();
    } , []);

    return <Navbar bg="darkBlue" variant="dark" sticky='top' expand="md" collapseOnSelect> 
        <Navbar.Brand ><img src={logo}  className="brand" alt="logo" /> Safy Alert</Navbar.Brand>
        <Navbar.Toggle />
        <Navbar.Collapse className="ps-2">
            <Nav className="ms-auto">
                <Nav.Link href="/client/my-objects">My objects</Nav.Link>
                <Nav.Link href="/logs">Logs</Nav.Link> 
                <Nav.Link href="/logout">Logout</Nav.Link>
            </Nav>
        </Navbar.Collapse>
    </Navbar>
}
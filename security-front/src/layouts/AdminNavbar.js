import React from 'react'
import '../assets/styles/nav.css';
import '../assets/styles/style.css';
import {Nav, Navbar, NavDropdown} from 'react-bootstrap';
import { useEffect } from 'react';
import Stomp from 'stompjs';
import SockJS from 'sockjs-client';

export default function AdminNavbar(){
    const logo = require('../assets/images/logosh.png')

    const connectAndSubscribe = () => {
        const socket = new SockJS('http://localhost:8081/ws');
        const stompClient = Stomp.over(socket);
        console.log("ws connectandsubscribe")

        stompClient.connect({}, () => {
            stompClient.subscribe('/topic/log', (message) => {
                console.log("ws connect")
                const receivedMessage = JSON.parse(message.body)
            })
        });

        return () => {
            console.log("ws disconnect");
            stompClient.disconnect();  
        };
    };

    useEffect(() => {
        connectAndSubscribe();
    } , []);

    return <Navbar bg="darkBlue" variant="dark" sticky='top' expand="md" collapseOnSelect> 
        <Navbar.Brand ><img src={logo}  className="brand" alt="logo" /> Safy Alert</Navbar.Brand>
        <Navbar.Toggle />
        <Navbar.Collapse className="ps-2">
            <Nav className="ms-auto">
                <Nav.Link href="/admin/certificates">Certificates</Nav.Link>
                <Nav.Link href="/admin/requests">Reqests</Nav.Link>
                <Nav.Link href="/admin/clients">Clients</Nav.Link>
                <Nav.Link href="/admin/all-objects">All objects</Nav.Link> 
                <Nav.Link href="/admin/new-object">New object</Nav.Link> 
                <Nav.Link href="/admin/logs">Logs</Nav.Link> 
                <Nav.Link href="/logout">Logout</Nav.Link>
            </Nav>
        </Navbar.Collapse>
    </Navbar>
}
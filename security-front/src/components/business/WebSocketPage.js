import Stomp from 'stompjs';
import SockJS from 'sockjs-client';
import '../../assets/styles/business.css';
import {useEffect, useState} from 'react';

export default function WebSocketPage() {

    const connectAndSubscribe = () => {
        const socket = new SockJS('https://localhost:8081/ws');
        const stompClient = Stomp.over(socket);
        console.log("ws connectandsubscribe")

        stompClient.connect({}, () => {
            stompClient.subscribe('/topic/log/admin@gmail.com', (message) => {
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
}
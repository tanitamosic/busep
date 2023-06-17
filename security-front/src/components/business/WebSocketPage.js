import Stomp from 'stompjs';
import SockJS from 'sockjs-client';
import '../../assets/styles/business.css';
import {useEffect, useState} from 'react';

export default function WebSocketPage() {

    useEffect(() => {
        const connectAndSubscribe = () => {
            const socket = new SockJS('https://localhost:8081/ws');
            const stompClient = Stomp.over(socket);
            stompClient.connect({}, () => {
                stompClient.subscribe('/topic/log', (message) => {
                    const receivedMessage = JSON.parse(message.body)
            });
        });
    }});
}
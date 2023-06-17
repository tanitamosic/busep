import React from 'react';
import { useEffect, useRef } from 'react';

const LogsViewer = ({ logs }) => {

    const containerRef = useRef(null);

    useEffect(() => {
        // Scroll to the bottom of the container when logs change
        containerRef.current.scrollTop = containerRef.current.scrollHeight;
    }, [logs]);

    const formatTimestamp = (timestamp) => {
        const [year, month, day, hour, minute, second] = timestamp;
        const formattedDate = new Date(Date.UTC(year, month - 1, day, hour, minute, second));
        return formattedDate.toLocaleString();
      };

  return (
    <div className="borderedBlock">

    <div

    ref={containerRef}
      style={{
        width: '100%',
        height: '300px',
        overflow: 'auto',
        backgroundColor: '#000',
        color: '#fff',
        fontFamily: 'Courier New',
        padding: '10px',
      }}
    >
      {logs.map((log, index) => (
        <div
          key={index}
        >
          {formatTimestamp(log.timestamp) + " "} 
          <span style={{
                color: log.logType === 'WARN' ? 'yellow' : log.logType === 'ERROR' ? 'red' : log.logType === 'ALARM' ? 'red' : 'lightblue',
              }}>  
              {log.logType === "ALARM" && "*"}{log.logType} {log.logType === "ALARM" && "*"}
            </span> 
            {"___houseId:" + log.houseId + "___device:_" + log.deviceType + "(" + log.deviceId + ")___"}
            
            <span style={{
                color: log.logType === 'WARN' ? 'yellow' : log.logType === 'ERROR' ? 'red' : log.logType === 'ALARM' ? 'red' : 'lightblue',
              }}>  
              {log.message} 
            </span> 
             {/* {"___from___" + log.ipAddress + " " + log.id} */}
             {"___from___" + log.id}
        </div>
      ))}
    </div>
    </div>

  );
};

export default LogsViewer;
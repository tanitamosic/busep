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


    const formatLog = (log, index) => {
        if (log.genericLogType === "DEVICE"){
            return <>
              <div key={index} >
                {"DEV_LOG]__"}{formatTimestamp(log.timestamp) + " "} 
                <span style={{ color: log.logType === 'WARN' ? 'yellow' : log.logType === 'ERROR' ? 'red' : 'lightblue'}}>  
                  {log.logType}
                </span> 

                {"___houseId:" + log.houseId + "___device:_" + log.deviceType + "(" + log.deviceId + ")___"}
                
                <span style={{ color: log.logType === 'WARN' ? 'yellow' : log.logType === 'ERROR' ? 'red' : log.logType === 'ALARM' ? 'red' : 'lightblue' }}>  
                  {log.message} 
                </span> 
                {"___from___" + log.ipAddress}
            </div>
            </>
        } else if (log.genericLogType === "DEVICE_ALARM"){
          return <>
              <div key={index} >
                {"[DEV_AL]__"}{formatTimestamp(log.timestamp) + " "} 
                <span style={{ color: 'red'}}>  
                  {"*ALARM*___severity:" + log.severity}
                </span> 

                {"___houseId:" + log.houseId + "___device:_" + log.deviceType + "(" + log.deviceId + ")___"}
                
                <span style={{ color:'red' }}>  
                  {log.message} 
                </span> 
            </div>
          </>
        } else if (log.genericLogType === "REQUEST_ALARM"){
            return <>
            <div key={index} >
              {formatTimestamp(log.timestamp) + " "} 
              <span style={{ color: 'red'}}>  
                {"[REQ_AL]__"}{"*ALARM*___severity:" + log.severity}
              </span> 

              {"___requestType:" + log.requestType + "___"}
              
              <span style={{ color:'red' }}>  
                {log.message} 
              </span> 
              {"___from___" + log.source}
            </div>
          </>
        }
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
      {logs.map((log, index) => formatLog(log, index))}
    </div>
    </div>

  );
};

export default LogsViewer;
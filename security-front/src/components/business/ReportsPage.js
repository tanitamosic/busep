import React, { useCallback } from 'react';
import '../../assets/styles/business.css';
import {useEffect, useState} from 'react';
import { Row, Col, Form, Button } from 'react-bootstrap';
import ReportCharts from './ReportCharts';
import { ReportForm } from '../forms/ReportForm';

export default function ReportsPage(){

    const [report, setReport] = useState();

    useEffect(() => {
        const _report = [
            {"source": "house1", "numOfLogs": 5},
            {"source": "house2", "numOfLogs": 8},
            {"source": "apartment1", "numOfLogs": 3},
            {"source": "office1", "numOfLogs": 10},
            {"source": "store1", "numOfLogs": 2},
            {"source": "building1", "numOfLogs": 6}
          ];
          
          setReport(_report);
    }, [])

    return <>
        <br/>
        <center><h1>Reports</h1></center>
        <ReportForm setReportFunc={setReport}/>
        <ReportCharts data={report} />
        {report 
        ? 
            <center>
                <h2>
                    {"Total logs: " + report.reduce((acc, { numOfLogs }) => acc + numOfLogs, 0)}
                </h2>
                <br/>
                <br/>
            </center>
        : 
        null
        }
    </>
}


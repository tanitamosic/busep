import React, { useCallback } from 'react';
import '../../assets/styles/business.css';
import {useEffect, useState} from 'react';
import { Row, Col, Form, Button } from 'react-bootstrap';
import ReportCharts from './ReportCharts';
import { ReportForm } from '../forms/ReportForm';

export default function ReportsPage(){

    const [report, setReport] = useState();

    useEffect(() => {
        const _report = [];
          
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


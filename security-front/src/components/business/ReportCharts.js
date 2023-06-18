import React from 'react';
import { Row, Col } from 'react-bootstrap';
import { VictoryBar, VictoryChart, VictoryAxis, VictoryTheme, VictoryZoomContainer, VictoryLabel } from 'victory';
import "bootstrap/dist/css/bootstrap.css";
// import "react-bootstrap-table-next/dist/react-bootstrap-table2.min.css";

export default function ReportCharts({data}){
    // console.log(data)

    if (!!data){
        return <>
        <Row>
            <Col sm={2} />
            <Col sm={8} align="center" className='mt-3'>
                <p style={{fontSize: "xx-large"}}>Logs</p>
            </Col>
            <Col sm={2} />
        </Row>
        <Row >
            <Col sm={2} />
            <Col sm={8}>
                <VictoryChart
                    theme={VictoryTheme.material}
                    domainPadding={20}
                    containerComponent={
                        <VictoryZoomContainer
                        allowZoom={false}
                        zoomDomain={{x:[0,4]}}/>
                    }
                    animate={{
                        duration: 1000,
                    }}
                    
                >
                    <VictoryAxis />
                    <VictoryAxis
                    dependentAxis
                    tickFormat={(x) => (`${x}`)}
                    />
                    <VictoryBar
                        data={data}
                        x="source"
                        y="numOfLogs"
                        barWidth={30}
                        labels={({ datum }) => datum.numOfLogs}
                        style={
                            { 
                                labels: { fill: "white" },
                                data: { fill: "#5da4b4" } 
                            }
                        }
                        labelComponent={<VictoryLabel dy={30}/>}
                        
                    />
                </VictoryChart>
            </Col>
            <Col sm={2} />
        </Row>
    </>
    }
    
}
import React from 'react'
import BreadcrumbCustom from '@components/Breadcrumb/BreadcrumbCustom'
import BarChart from '@components/Bar'
import LineChart from '@components/Line'
import PieChart from '@components/Pie'
import { Col, Row } from 'antd'
import './statisticAnalysis.less'
export interface IProps {
    test?: any
}

interface IState {
    test?: any
}

export default class StatisticAnalysis extends React.Component<IProps, IState> {
    constructor(props: IProps, state: IState) {
        super(props)
    }

    render() {
        return (
            <div className='statistic-analysis'>
            <BreadcrumbCustom />
                <Row style={{margin: '20px 0'}}>
                    <Col md={5} sm={12}>
                        <h3>总用电量(°)：23</h3>
                    </Col>
                    <Col md={5} sm={12}>
                        <h3>总用水量(m³)：223</h3>
                    </Col>
                    <Col md={5} sm={12}>
                        <h3>发卡数量(张)：43</h3>
                    </Col>
                    <Col md={5} sm={12}>
                        <h3>充值终端(个)：78</h3>
                    </Col>
                    <Col md={4} sm={12}>
                        <h3>机井(台)：623</h3>
                    </Col>
                </Row>
                
                <Row>
                    <Col md={12} sm={24}>
                        <LineChart />
                    </Col>
                    <Col md={12} sm={24}>
                        <PieChart />
                    </Col>
                    {/* <Col md={24} sm={24}>
                        <BarChart />
                    </Col> */}
                </Row>
                <Row>
                   
                    <Col md={24} sm={24}>
                        <BarChart />
                    </Col>
                </Row>



            </div>
        )
    }
}

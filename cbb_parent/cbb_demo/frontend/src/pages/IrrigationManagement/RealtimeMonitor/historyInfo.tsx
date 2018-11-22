import React from 'react'
import BreadcrumbCustom from '@components/Breadcrumb/BreadcrumbCustom'
import { Table, Row, Col, Icon } from 'antd'
import './RealTimeMonitor.less'
import HttpClient from '@utils/HttpClient'
// const dataSource = [{
//     key: '1',
//     cardNumber: '000001',
//     openTime: '2018-10-05 12:06:23',
//     closeTime: '2018-10-10 12:06:23',
//     electricity: '123',
//     water: '34',
//     electricityCon: '12',
//     waterCon: '34',
//     residualElectricity: '34',
//     residualWater: '89',
//     waterLevel: '12'
// }, {
//     key: '2',
//     cardNumber: '000002',
//     openTime: '2017-03-05 12:06:23',
//     closeTime: '2018-04-10 12:06:23',
//     electricity: '63',
//     water: '784',
//     electricityCon: '120',
//     waterCon: '34',
//     residualElectricity: '314',
//     residualWater: '829',
//     waterLevel: '15'
// }]


const columns = [{
    title: '开泵时间',
    dataIndex: 'openTime',
    key: 'openTime',
    sorter: (a, b) => a.openTime - b.openTime,
    fixed: true
}, {
    title: '关泵时间',
    dataIndex: 'closeTime',
    key: 'closeTime',
    sorter: (a, b) => a.closeTime - b.closeTime
}, {
    title: 'IC卡号',
    dataIndex: 'cardCode',
    key: 'cardCode',
    
}, {
    title: '村级编号',
    dataIndex: 'villageCode',
    key: 'villageCode',
}, {
    title: '本次用电量(°)',
    dataIndex: 'currentUsedElectric',
    key: 'currentUsedElectric'
}, {
    title: '本次用水量(m³)',
    dataIndex: 'currentUsedWater',
    key: 'currentUsedWater'
}, {
    title: '剩余用电量(°)',
    dataIndex: 'restElectric',
    key: 'restElectric'
}, {
    title: '剩余用水量(m³)',
    dataIndex: 'restWater',
    key: 'restWater'

}, {
    title: '水位(m)',
    dataIndex: 'waterLevel',
    key: 'waterLevel'

}]

export interface IProps {
    test?: any
    match?: any
}

interface IState {
    test?: any
    pagination?: any
    historyInfos?: any
    deviceCode?: any
    areaName?: any
    deviceName?: any
    loading? : boolean
    allUsedElectric? : any
    allUsedWater? : any
}

export default class HistoryInfo extends React.Component<IProps, IState> {
    constructor(props: IProps, state: IState) {
        super(props)
        this.state = {
            historyInfos: [],
            loading: false,
            allUsedElectric: '',
            allUsedWater: '',
            pagination: {
                showSizeChanger: true,
                showQuickJumper: true,
                total: 0,
                showTotal: total => `共 ${total} 条`
            }
        }
    }

    componentDidMount() {
        this.getHistoryData(this.props.match.params.id)
    }

    getHistoryData = (param) => {
        this.setState({
            loading: true
        })
        HttpClient.post(`/api/RealtimeMonitor/getHistoryData/${param}`, {}).then((res: any) => {
            if (res && res.data) {
                this.setState({
                    historyInfos: res.data.pumpRecords,
                    deviceCode: res.data.deviceCode,
                    areaName: res.data.areaName,
                    deviceName: res.data.deviceName,
                    allUsedElectric: res.data.allUsedElectric,
                    allUsedWater: res.data.allUsedWater,
                    loading: false,
                    pagination: {
                        showSizeChanger: true,
                        showQuickJumper: true,
                        total: res.data.pumpRecords.length,
                        showTotal: total => `共 ${total} 条`
                    }
                })
            } else {
                this.setState({
                    loading: false
                })
            }
        })
    }

    handleTableChange = () => {
        this.getHistoryData(this.props.match.params.id)
    }

    render() {
        return (
            <div className='real-time-monitor'>
                <BreadcrumbCustom />
                <Icon type='redo' style={{ fontSize: '20px', color: '#08c', float: 'right', marginTop: -30, cursor: 'pointer' }} />
                <div className='real-time-monitor-top'>
                    <Row>
                        <Col md={1} sm={0}>
                            <Icon type='home' style={{ fontSize: '24px', color: '#08c' }} />
                        </Col>
                        <Col md={4} sm={24}>
                            <h3>测站编码：{this.state.deviceCode}</h3>
                        </Col>
                    </Row>
                    <Row>
                        <Col md={4} sm={24}>
                            <span>机井名称：{this.state.deviceName}</span>
                        </Col>
                        <Col md={4} sm={24}>
                            <span className='real-time-addr'>地址：{this.state.areaName}</span>
                        </Col>
                        <Col md={4} sm={24}>
                            <span>累计用电量(°)：{this.state.allUsedElectric}</span>
                        </Col>
                        <Col md={4} sm={24}>
                            <span>累计用水量(m³)：{this.state.allUsedWater}</span>
                        </Col>

                    </Row>
                </div>
                <h3>近半年灌溉信息</h3>
                <Table dataSource={this.state.historyInfos}
                    columns={columns}
                    scroll={{ x: 576 }}
                    rowKey={record => record.pumpRecordId}
                    pagination={this.state.pagination}
                    loading={this.state.loading}
                    onChange={this.handleTableChange}
                />
            </div>
        )
    }
}

import React from 'react'
import BreadcrumbCustom from '@components/Breadcrumb/BreadcrumbCustom'
import { Table, Row, Col, Icon } from 'antd'
import ammeterS from '../../../assets/images/ammeterS.png'
import batteryLowS from '../../../assets/images/batteryLowS.png'
import faultS from '../../../assets/images/faultS.png'
import stutterStopS from '../../../assets/images/stutterStopS.png'
import waterMeterS from '../../../assets/images/waterMeterS.png'
import waterShortageS from '../../../assets/images/waterShortageS.png'
import exploitableS from '../../../assets/images/exploitableS.png'
import ammeterL from '../../../assets/images/ammeterL.png'
import batteryLowL from '../../../assets/images/batteryLowL.png'
import faultL from '../../../assets/images/faultL.png'
import stutterStopL from '../../../assets/images/stutterStopL.png'
import waterMeterL from '../../../assets/images/waterMeterL.png'
import waterShortageL from '../../../assets/images/waterShortageL.png'
import exploitableL from '../../../assets/images/exploitableL.png'
import HttpClient from '@utils/HttpClient'
// const dataSource = [{
//     warnType: '2,4,6',
//     warnTime: '2018-8-12: 07:45:05',
//     allUsedElectric: '56',
//     allUsedWater: '23',
//     waterLevel: '65'
// }, {
//     warnType: '6',
//     warnTime: '2018-8-12: 07:45:05',
//     allUsedElectric: '56',
//     allUsedWater: '23',
//     waterLevel: '65'
// }, {
//     warnType: '0,1',
//     warnTime: '2018-8-12: 07:45:05',
//     allUsedElectric: '56',
//     allUsedWater: '23',
//     waterLevel: '65'
// }, {
//     warnType: '3,5',
//     warnTime: '2018-8-12: 07:45:05',
//     allUsedElectric: '56',
//     allUsedWater: '23',
//     waterLevel: '65'
// }, {
//     warnType: '2,4,6',
//     warnTime: '2018-8-12: 07:45:05',
//     allUsedElectric: '56',
//     allUsedWater: '23',
//     waterLevel: '65'
// }, {
//     warnType: '6',
//     warnTime: '2018-8-12: 07:45:05',
//     allUsedElectric: '56',
//     allUsedWater: '23',
//     waterLevel: '65'
// }, {
//     warnType: '1',
//     warnTime: '2018-8-12: 07:45:05',
//     allUsedElectric: '56',
//     allUsedWater: '23',
//     waterLevel: '65'
// }, {
//     warnType: '3',
//     warnTime: '2018-8-12: 07:45:05',
//     allUsedElectric: '56',
//     allUsedWater: '23',
//     waterLevel: '65'
// }]
const columns = [{
    title: '报警类别',
    dataIndex: 'warnType',
    key: 'warnType',
    render: (text, record) => {
        let warningInfosrc
        if (text === '0') {
            warningInfosrc = exploitableL
        } else if (text === '1') {
            warningInfosrc = batteryLowL
        } else if (text === '2') {
            warningInfosrc = waterShortageL
        } else if (text === '3') {
            warningInfosrc = faultL
        } else if (text === '5') {
            warningInfosrc = ammeterL
        } else if (text === '4') {
            warningInfosrc = waterMeterL
        } else if (text === '6') {
            warningInfosrc = stutterStopL
        }
        return (<img src={warningInfosrc} />)
    },
    fixed: true

}, {
    title: '上报时间',
    dataIndex: 'warnTime',
    key: 'warnTime',
    sorter: (a, b) => a.warnTime - b.warnTime
}, {
    title: '累计用电量(°)',
    dataIndex: 'allUsedElectric',
    key: 'allUsedElectric'

}, {
    title: '累计用水量(m³)',
    dataIndex: 'allUsedWater',
    key: 'allUsedWater'
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
    warningInfos?: any
    deviceCode?: any
    areaName?: any
    deviceName?: any
    loading?: boolean
}

export default class WarningInfo extends React.Component<IProps, IState> {
    constructor(props: IProps, state: IState) {
        super(props)
        this.state = {
            warningInfos: [],
            loading: false,
            deviceCode: '',
            areaName: '',
            deviceName: '',
            pagination: {
                showSizeChanger: true,
                showQuickJumper: true,
                total: 0,
                showTotal: total => `共 ${total} 条`
            }
        }
    }

    componentDidMount() {
        this.getWarnData(this.props.match.params.id)
    }

    handleWarnInfos = (dataSource) => {
        const warnInfos = dataSource.filter(item => item.warnType.length > 1)
        const warnInfo = dataSource.filter(item => item.warnType.length === 1)
        const newWarnArr: any = []
        warnInfos.map(item => {
            const warnTypes = item.warnType.split(',')
            warnTypes.map(warnItem => {
                const newObj: any = {}
                newObj.warnType = warnItem
                newObj.warnTime = item.warnTime
                newObj.allUsedElectric = item.allUsedElectric
                newObj.allUsedWater = item.allUsedWater
                newObj.waterLevel = item.waterLevel
                newWarnArr.push(newObj)
            })
        })
        const warningInfos = newWarnArr.concat(warnInfo)
        this.setState({ warningInfos })
    }

    getWarnData = (param) => {
        this.setState({
            loading: true
        })
        HttpClient.post(`/api/RealtimeMonitor/getWarnInfos/${param}`, {}).then((res: any) => {
            if (res && res.data) {
                this.handleWarnInfos(res.data.warnInfos)
                this.setState({
                    deviceCode: res.data.deviceCode,
                    areaName: res.data.areaName,
                    deviceName: res.data.deviceName,
                    loading: false,
                    pagination: {
                        showSizeChanger: true,
                        showQuickJumper: true,
                        total: res.data.warnInfos.length,
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
        this.getWarnData(this.props.match.params.id)
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
                            <span className='real-time-addr'>区划：{this.state.areaName}</span>
                        </Col>
                    </Row>

                </div>
                <h3>近半年报警信息</h3>
                <Row className='imgRemark'>
                    <Col md={10} sm={0} />
                    <Col md={2} sm={12}><img src={exploitableS} />:<span>开采状态</span></Col>
                    <Col md={2} sm={12}><img src={stutterStopS} />:<span>急停状态</span></Col>
                    <Col md={2} sm={12}><img src={faultS} />:<span>回路故障</span></Col>
                    <Col md={2} sm={12}><img src={batteryLowS} />:<span>电量不足</span></Col>
                    <Col md={2} sm={12}><img src={waterShortageS} />:<span>水量不足</span></Col>
                    <Col md={2} sm={12}><img src={ammeterS} />:<span>电表故障</span></Col>
                    <Col md={2} sm={12}><img src={waterMeterS} />:<span>水表故障</span></Col>
                </Row>
                <Table
                    // dataSource={this.state.warningInfos}
                    dataSource={this.state.warningInfos}
                    columns={columns}
                    scroll={{ x: 576 }}
                    rowKey={record => record.warnId}
                    pagination={this.state.pagination}
                    loading={this.state.loading}
                    onChange={this.handleTableChange} />
            </div>
        )
    }
}

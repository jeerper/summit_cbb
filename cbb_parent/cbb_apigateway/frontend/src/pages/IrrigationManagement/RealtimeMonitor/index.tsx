import React from 'react'
import { Table, Input, Button, Select, Row, Col, Form, Popconfirm, Icon, TreeSelect } from 'antd'
import { Link } from 'react-router-dom'
import './RealTimeMonitor.less'
import BreadcrumbCustom from '@components/Breadcrumb/BreadcrumbCustom'
import HttpClient from '@utils/HttpClient'
import signalN from '../../../assets/images/signalN.svg'
import signalXs from '../../../assets/images/signalXs.svg'
import signalS from '../../../assets/images/signalS.svg'
import signalM from '../../../assets/images/signalM.svg'
import signalL from '../../../assets/images/signalL.svg'
import signalXl from '../../../assets/images/signalXl.svg'
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

const FormItem = Form.Item
const Option = Select.Option
const TreeNode = TreeSelect.TreeNode


export interface IProps {
    test?: any
}

interface IState {
    test?: any
    pagination?: any
    realTimeInfos: any
    areas?: any
    stationCode?: any
    area?: any
    pumpStatus?: any
    loading?: boolean
}

export default class RealtimeMonitor extends React.Component<IProps, IState> {
    constructor(props: IProps, state: IState) {
        super(props)
        this.state = {
            realTimeInfos: [],
            areas: [],
            stationCode: '',
            area: '',
            pumpStatus: ' ',
            loading: false,
            pagination: {
                showSizeChanger: true,
                showQuickJumper: true,
                total: 0,
                showTotal: total => `共 ${total} 条`
            }
        }
    }

    componentDidMount() {
        this.getRealTimeInfos()
    }

    getRealTimeInfos = (params = {}) => {
        this.setState({
            loading: true
        })
        HttpClient.post('/api/RealtimeMonitor/latestData', {}).then((res) => {
            if (res && res.data) {
                this.setState({
                    realTimeInfos: res.data,
                    loading: false,
                    pagination: {
                        showSizeChanger: true,
                        showQuickJumper: true,
                        total: res.data.length,
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


    handleStationCode = (e) => {
        e.preventDefault()
        this.setState({
            stationCode: e.target.value
        })
    }

    handleArea = (value) => {
        this.setState({
            area: value
        })
    }

    handlePumpStatus = (value) => {
        this.setState({
            pumpStatus: value
        })
    }

    handleQuery = (e) => {
        e.preventDefault()
        const params = `areaId=${this.state.area}&deviceCode=${this.state.stationCode}&pumpStatus=${this.state.pumpStatus}`
        this.queryFn(params)
    }

    handleReset = () => {
        this.setState({
            area: '',
            stationCode: '',
            pumpStatus: ' '
        })
    }
    queryFn = (params) => {
        this.setState({
            loading: true
        })
        HttpClient.post(`/api/RealtimeMonitor/latestData?${params}`, {}).then(res => {
            if (res && res.data) {
                this.setState({
                    realTimeInfos: res.data,
                    loading: false,
                    pagination: {
                        showSizeChanger: true,
                        showQuickJumper: true,
                        total: res.data.length,
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

    handleTableChange = (pagination, filters, sorter) => {
        const pager = { ...this.state.pagination }
        pager.current = pagination.current
        this.setState({
            pagination: pager,
        })
        this.getRealTimeInfos({
            results: pagination.pageSize,
            page: pagination.current,
            sortField: sorter.field,
            sortOrder: sorter.order,
            ...filters,
        })

    }

    handleConfirm = (record) => {
        console.log(record)
    }

    handleHistoryClick = (record) => {
        window.location.replace('/#/app/real-time/history/' + record.deviceCode)
    }

    handleWarnClick = (record) => {
        window.location.replace('/#/app/real-time/warning/' + record.deviceCode)
    }

    render() {
        const userName = localStorage.getItem('userName')
        const columns = [{
            title: '测站编码',
            dataIndex: 'deviceCode',
            key: 'deviceCode',
            className: 'clickable',
            onCell: (record) => {
                return {
                    onClick: this.handleHistoryClick.bind(this, record)
                }
            },
            fixed: true
        }, {
            title: '机井名称',
            dataIndex: 'deviceName',
            key: 'deviceName',
            className: 'clickable',
            onCell: (record) => {
                return {
                    onClick: this.handleHistoryClick.bind(this, record)
                }
            },

        }, {
            title: '区划',
            dataIndex: 'areaName',
            key: 'areaName',
            className: 'clickable',
            onCell: (record) => {
                return {
                    onClick: this.handleHistoryClick.bind(this, record)
                }
            },
        },
        {
            title: '累计用电量(°)',
            dataIndex: 'allUsedElectric',
            key: 'allUsedElectric',
            className: 'clickable',
            onCell: (record) => {
                return {
                    onClick: this.handleHistoryClick.bind(this, record)
                }
            }
        }, {
            title: '累计用水量(m³)',
            dataIndex: 'allUsedWater',
            key: 'allUsedWater',
            className: 'clickable',
            onCell: (record) => {
                return {
                    onClick: this.handleHistoryClick.bind(this, record)
                }
            }

        }, {
            title: '水位(m)',
            dataIndex: 'waterLevel',
            key: 'waterLevel',
            className: 'clickable',
            onCell: (record) => {
                return {
                    onClick: this.handleHistoryClick.bind(this, record)
                }
            }
        }, {
            title: '上报时间',
            dataIndex: 'reportTime',
            key: 'reportTime',
            className: 'clickable',
            sorter: (a, b) => a.time - b.time,
            onCell: (record) => {
                return {
                    onClick: this.handleHistoryClick.bind(this, record)
                }
            },
        }, {
            title: '泵状态',
            dataIndex: 'pumpStatus',
            key: 'pumpStatus',
            className: 'clickable',
            render: (text) => {
                const status = text === '0' ? '关闭' : '开启'
                return (<span>{status}</span>)
            },
            onCell: (record) => {
                return {
                    onClick: this.handleHistoryClick.bind(this, record)
                }
            },
        }, {
            title: '报警类别',
            dataIndex: 'warnType',
            key: 'warnType',
            className: 'clickable',
            onCell: (record) => {
                return {
                    onClick: this.handleWarnClick.bind(this, record)
                }
            },
            render: (text, record) => {
                let warningInfosrc
                const warningInfo = text.length > 1 ? (text.split(',')[0] === '0' ? text.split(',')[1] : text.split(',')[0]) : text
                if (warningInfo === '0') {
                    warningInfosrc = exploitableL
                } else if (warningInfo === '1') {
                    warningInfosrc = batteryLowL
                } else if (warningInfo === '2') {
                    warningInfosrc = waterShortageL
                } else if (warningInfo === '3') {
                    warningInfosrc = faultL
                } else if (warningInfo === '5') {
                    warningInfosrc = ammeterL
                } else if (warningInfo === '4') {
                    warningInfosrc = waterMeterL
                } else if (warningInfo === '6') {
                    warningInfosrc = stutterStopL
                }
                return (<img src={warningInfosrc} />)
            }

        }, {
            title: '信号强度',
            dataIndex: 'signalStrength',
            key: 'signalStrength',
            className: 'clickable',
            onCell: (record) => {
                return {
                    onClick: this.handleHistoryClick.bind(this, record)
                }
            },
            render: (text, record) => {
                let singalImgSrc
                if (text === 0) {
                    singalImgSrc = signalN
                } else if (text > 0 && text <= 6) {
                    singalImgSrc = signalXs
                } else if (text > 6 && text <= 12) {
                    singalImgSrc = signalS
                } else if (text > 12 && text <= 18) {
                    singalImgSrc = signalM
                } else if (text > 18 && text <= 24) {
                    singalImgSrc = signalL
                } else if (text > 24 && text <= 31) {
                    singalImgSrc = signalXl
                } else {
                    singalImgSrc = signalN
                }
                return (<img src={singalImgSrc} />)
            }
        }, {
            title: '操作',
            dataIndex: 'operation',
            key: 'operation',
            className: userName === 'admin' ? '' : 'hiddenOperation',
            render: (text, record) => {
                return (
                    this.state.realTimeInfos.length >= 1
                        ? (
                            <Popconfirm okText='确认' cancelText='取消' title={record.pumpStatus === '0' ? '确认远程开泵?' : '确认远程关泵?'} onConfirm={() => this.handleConfirm(record)}>
                                <Button type={record.pumpStatus === '0' ? 'primary' : 'danger'} size='small'>{record.pumpStatus === '0' ? '开泵' : '关泵'}</Button>
                            </Popconfirm>
                        ) : null
                )
            }
        }]

        return (
            <div className='real-time-monitor'>
                <BreadcrumbCustom />
                <Link to='/app/cartographic'>
                    <Icon type='environment' style={{ fontSize: '24px', float: 'right', marginTop: -30 }} />
                </Link>
                <div>
                    <Form layout='inline' className='real-time-form-top'>
                        <Row>
                            <Col md={6} sm={6} >
                                <FormItem label='测站编码'>
                                    <Input placeholder='请输入' value={this.state.stationCode} onChange={this.handleStationCode} />
                                </FormItem>
                            </Col>
                            <Col md={6} sm={6} >
                                <FormItem label='区划'>
                                    <TreeSelect
                                        showSearch
                                        placeholder='请选择'
                                        dropdownStyle={{ maxHeight: 400, overflow: 'auto' }}
                                        allowClear
                                        treeDefaultExpandAll
                                        value={this.state.area}
                                        onChange={this.handleArea}
                                    >
                                        {
                                            this.state.areas.map((parentItem) => (
                                                <TreeNode title={parentItem.areaName} key={parentItem.areaId} value={parentItem.areaId}>
                                                    {
                                                        parentItem.childs.map((childItem) => (
                                                            <TreeNode title={childItem.areaName} key={childItem.areaId} value={childItem.areaId}>
                                                                {
                                                                    childItem.childs.map((item) => (
                                                                        <TreeNode title={item.areaName} key={item.areaId} value={item.areaId} />
                                                                    ))}
                                                            </TreeNode>
                                                        ))}
                                                </TreeNode>
                                            ))}
                                    </TreeSelect>
                                </FormItem>
                            </Col>
                            <Col md={6} sm={6}>
                                <FormItem label='泵状态'>
                                    <Select value={this.state.pumpStatus} onChange={this.handlePumpStatus}>
                                        <Option value='1'>开启</Option>
                                        <Option value='0'>关闭</Option>
                                        <Option value=' '>全部</Option>
                                    </Select>
                                </FormItem>
                            </Col>
                            <Col md={6} sm={6} >
                                <FormItem>
                                    <Button type='primary' onClick={this.handleQuery}>查询</Button>
                                    <Button className='realTime-btn' onClick={this.handleReset}>重置</Button>
                                </FormItem>
                            </Col>
                        </Row>
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
                    </Form>

                    <Table columns={columns}
                        scroll={{ x: 1100 }}
                        dataSource={this.state.realTimeInfos}
                        // dataSource={dataSource}
                        rowKey={record => record.deviceId}
                        pagination={this.state.pagination}
                        loading={this.state.loading}
                        onChange={this.handleTableChange}
                    />
                </div>
            </div>
        )
    }
}

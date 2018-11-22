import React from 'react'
import BreadcrumbCustom from '@components/Breadcrumb/BreadcrumbCustom'
import { Table, Row, Col, Icon } from 'antd'
import './ICCardInfo.less'
import HttpClient from '@utils/HttpClient'

const columns = [{
    title: '操作类型',
    dataIndex: 'operateType',
    key: 'operateType',
    fixed: true
    // render: (text) => {
    //     let operationType
    //     if (text === '0') {
    //         operationType = '开卡'
    //     } else if (text === '1') {
    //         operationType = '充值'
    //     } else if (text === '2') {
    //         operationType = '挂失'
    //     } else if (text === '3') {
    //         operationType = '注销'
    //     }
    //     return (<span>{operationType}</span>)
    // }

}, {
    title: '购买电量(°)',
    dataIndex: 'buyElectric',
    key: 'buyElectric'

}, {
    title: '购买水量(m³)',
    dataIndex: 'buyWater',
    key: 'buyWater'
},
{
    title: '剩余电量(°)',
    dataIndex: 'restElectric',
    key: 'restElectric'
}, {
    title: '剩余水量(m³)',
    dataIndex: 'restWater',
    key: 'restWater'
}, {
    title: '操作时间',
    dataIndex: 'operateTime',
    key: 'operateTime',
    sorter: (a, b) => a.operateTime - b.operateTime
}]
export interface IProps {
    test?: any
    match?: any
}

interface IState {
    test?: any
    pagination?: any
    cardDetails?: any
    cardCode?: any
    cardNumber?: any
    userName?: any
    operateTime?: any
    villageCode?: any
    loading?: boolean
    cardStatus?: any
}

export default class ICCardDetail extends React.Component<IProps, IState> {
    constructor(props: IProps, state: IState) {
        super(props)
        this.state = {
            cardDetails: [],
            cardCode: '',
            cardNumber: '',
            loading: false,
            userName: '',
            operateTime: '',
            villageCode: '',
            cardStatus: '',
            pagination: {
                showSizeChanger: true,
                showQuickJumper: true,
                total: 0,
                showTotal: total => `共 ${total} 条`
            }
        }
    }

    componentDidMount() {
        this.getCardDetail(this.props.match.params.id)
    }

    getCardDetail = (param) => {
        this.setState({
            loading: true
        })
        HttpClient.get(`/api/card/getCardDetail/${param}`, {}).then((res: any) => {
            if (res && res.data) {
                this.setState({
                    cardDetails: res.data.cardOperationList,
                    cardCode: res.data.cardCode,
                    cardNumber: res.data.cardNumber,
                    userName: res.data.userName,
                    operateTime: res.data.operateTime,
                    villageCode: res.data.villageCode,
                    cardStatus: res.data.cardStatus,
                    loading: false,
                    pagination: {
                        showSizeChanger: true,
                        showQuickJumper: true,
                        total: res.data.cardOperationList.length,
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
        this.getCardDetail(this.props.match.params.id)
    }

    render() {
        return (
            <div className='IC-card-info'>
                <BreadcrumbCustom />
                <Icon type='redo' style={{ fontSize: '20px', color: '#08c', float: 'right', marginTop: -30, cursor: 'pointer' }} />
                <div className='IC-card-info-top'>
                    <Row >
                        <Col md={1} sm={24}>
                            <Icon type='credit-card' style={{ fontSize: '24px', color: '#08c' }} />
                        </Col>
                        <Col md={6} sm={24}>
                            <h3>IC卡号: {this.state.cardCode}</h3>
                        </Col>
                    </Row>
                    <Row style={{ marginTop: 10 }}>
                        <Col md={4} sm={24}>
                            <span>农户名称： {this.state.userName}</span>
                        </Col >
                        <Col md={8} sm={24}>
                            <span>序列号：{this.state.cardNumber}</span>
                        </Col>
                    </Row>
                    <Row style={{ marginTop: 10 }}>
                        <Col md={4} sm={24}>
                            <span>村级编号：{this.state.villageCode}</span>
                        </Col>
                        <Col md={6} sm={24}>
                            <span>开卡时间： {this.state.operateTime}</span>
                        </Col>
                        <Col md={2} sm={24}>
                            <span>状态: {this.state.cardStatus}</span>
                        </Col>
                    </Row>

                </div>
                <h3>近半年历史纪录</h3>
                <Table dataSource={this.state.cardDetails}
                    columns={columns}
                    scroll={{ x: 720 }}
                    rowKey={record => record.cardOperateId}
                    onChange={this.handleTableChange}
                    loading={this.state.loading}
                    pagination={this.state.pagination} />
            </div>
        )
    }
}

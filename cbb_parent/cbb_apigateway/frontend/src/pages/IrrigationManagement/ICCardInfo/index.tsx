import React from 'react'
import { Table, Input, Button, Row, Col, Form } from 'antd'
import './ICCardInfo.less'
import BreadcrumbCustom from '@components/Breadcrumb/BreadcrumbCustom'
import HttpClient from '@utils/HttpClient'
const FormItem = Form.Item

export interface IProps {
    test?: any
}

interface IState {
    test?: any
    pagination?: any
    cardInfos?: any
    userName?: any
    cardCode?: any
    loading?: boolean
}

export default class ICCardInfo extends React.Component<IProps, IState> {
    constructor(props: IProps, state: IState) {
        super(props)
        this.state = {
            cardInfos: [],
            loading: false,
            userName: '',
            cardCode: '',
            pagination: {
                showSizeChanger: true,
                showQuickJumper: true,
                total: 0,
                showTotal: total => `共 ${total} 条`
            }
        }

    }

    componentDidMount() {
        this.getICCardInfos()
    }

    getICCardInfos = (params = {}) => {
        this.setState({
            loading: true
        })
        HttpClient.post('/api/card/querycardInfos', {}).then(res => {
            if (res && res.data) {
                this.setState({
                    cardInfos: res.data,
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

    handleUserName = (e) => {
        e.preventDefault()
        this.setState({
            userName: e.target.value
        })

    }

    handleCardCode = (e) => {
        e.preventDefault()
        this.setState({
            cardCode: e.target.value
        })

    }

    handleQuery = (e) => {
        e.preventDefault()
        const param = `cardCode=${this.state.cardCode}&userName=${this.state.userName}`
        this.queryICCardInfos(param)

    }

    queryICCardInfos = (param) => {
        this.setState({
            loading: true
        })
        HttpClient.post(`/api/card/querycardInfos?${param}`).then((res: any) => {
            if (res && res.data) {
                this.setState({
                    cardInfos: res.data,
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

    handleReset = () => {
        this.setState({
            cardCode: '',
            userName: ''
        })
    }

    handleDetailClick = (record) => {
        window.location.replace('/#/app/ic_info/detail/' + record.cardId)
    }

    handleTableChange = (pagination, filters, sorter) => {
        const pager = { ...this.state.pagination }
        pager.current = pagination.current
        this.setState({
            pagination: pager,
        })
        this.getICCardInfos({
            results: pagination.pageSize,
            page: pagination.current,
            sortField: sorter.field,
            sortOrder: sorter.order,
            ...filters,
        })

    }


    render() {

        const columns = [{
            title: 'IC卡号',
            dataIndex: 'cardCode',
            key: 'cardCode',
            className: 'clickable',
            onCell: (record) => {
                return {
                    onClick: this.handleDetailClick.bind(this, record)
                }
            },
            fixed: true
        }, {
            title: '村级编号',
            dataIndex: 'villageCode',
            key: 'villageCode',
            className: 'clickable',
            onCell: (record) => {
                return {
                    onClick: this.handleDetailClick.bind(this, record)
                }
            }
        }, {
            title: '农户名称',
            dataIndex: 'userName',
            key: 'userName',
            className: 'clickable',
            onCell: (record) => {
                return {
                    onClick: this.handleDetailClick.bind(this, record)
                }
            }
        }, {
            title: '联系电话',
            dataIndex: 'userPhone',
            key: 'userPhone',
            className: 'clickable',
            onCell: (record) => {
                return {
                    onClick: this.handleDetailClick.bind(this, record)
                }
            }
        },
        {
            title: '卡序列号',
            dataIndex: 'cardNumber',
            key: 'cardNumber',
            className: 'clickable',
            onCell: (record) => {
                return {
                    onClick: this.handleDetailClick.bind(this, record)
                }
            }
        }, {
            title: '剩余电量(°)',
            dataIndex: 'restElectric',
            key: 'restElectric',
            className: 'clickable',
            onCell: (record) => {
                return {
                    onClick: this.handleDetailClick.bind(this, record)
                }
            }
        }, {
            title: '剩余水量(m³)',
            dataIndex: 'restWater',
            key: 'restWater',
            className: 'clickable',
            onCell: (record) => {
                return {
                    onClick: this.handleDetailClick.bind(this, record)
                }
            }
        }, {
            title: '活动时间',
            dataIndex: 'modifiedTime',
            key: 'modifiedTime',
            className: 'clickable',
            onCell: (record) => {
                return {
                    onClick: this.handleDetailClick.bind(this, record)
                }
            }
        }, {
            title: '用途',
            dataIndex: 'cardFunction',
            key: 'cardFunction',
            className: 'clickable',
            onCell: (record) => {
                return {
                    onClick: this.handleDetailClick.bind(this, record)
                }
            }
        }, {
            title: '状态',
            dataIndex: 'cardStatus',
            key: 'cardStatus',
            className: 'clickable',
            width: 80,
            onCell: (record) => {
                return {
                    onClick: this.handleDetailClick.bind(this, record)
                }
            }
        }]

        return (
            <div className='IC-card-info'>
                <BreadcrumbCustom />
                <div>
                    <Form layout='inline' className='IC-card-form-top'>
                        <Row>
                            <Col md={6} sm={6} >
                                <FormItem label='IC卡号'>
                                    <Input placeholder='请输入' className='inputs' value={this.state.cardCode} onChange={this.handleCardCode} />
                                </FormItem>
                            </Col>
                            <Col md={6} sm={6} >
                                <FormItem label='农户名称'>
                                    <Input placeholder='请输入' className='inputs' value={this.state.userName} onChange={this.handleUserName} />
                                </FormItem>
                            </Col>
                            <Col md={6} sm={6} >
                                <FormItem>
                                    <Button type='primary' className='ICard-btn' onClick={this.handleQuery}>查询</Button>
                                    <Button className='ICard-btn' onClick={this.handleReset}>重置</Button>
                                </FormItem>
                            </Col>
                        </Row>
                    </Form>
                    <Table columns={columns}
                        scroll={{ x: 1100 }}
                        dataSource={this.state.cardInfos}
                        rowKey={record => record.cardId}
                        pagination={this.state.pagination}
                        loading={this.state.loading}
                        onChange={this.handleTableChange}

                    />
                </div>

            </div>
        )
    }
}

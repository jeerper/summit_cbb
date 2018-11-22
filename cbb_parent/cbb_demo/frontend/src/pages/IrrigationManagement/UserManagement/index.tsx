import React from 'react'
import { Table, Input, Button, Select, Row, Col, Form, Modal, message } from 'antd'
import './UserManagement.less'
import BreadcrumbCustom from '@components/Breadcrumb/BreadcrumbCustom'
import AddUser from './addUser'
import EditUser from './editUser'
const FormItem = Form.Item
const Option = Select.Option
import HttpClient from '@utils/HttpClient'

const columns = [{
    title: '用户名',
    dataIndex: 'userName',
    fixed: true
}, {
    title: '姓名',
    dataIndex: 'trueName'
}, {
    title: '区划',
    dataIndex: 'areaName'
},
{
    title: '联系电话',
    dataIndex: 'userPhone'
}, {
    title: '状态',
    dataIndex: 'userStatus',
    render: text => (text === '1' ? '启用' : '禁用'),
}]

export interface IProps {
    sendChangeData?: (content?: {}) => void
}

interface IState {
    pagination?: any
    isChange?: boolean
    isAdd?: boolean
    selectedRowKeys?: any
    selectedRow?: any
    data?: any
    loading?: boolean
    userName?: any
    trueName?: any
    userStatus?: any
}

export default class UserManagement extends React.Component<IProps, IState> {
    constructor(props: IProps, state: IState) {
        super(props)
        this.state = {
            pagination: {
                showSizeChanger: true,
                showQuickJumper: true,
                total: 0,
                showTotal: (total, range) => `共 ${total} 条`
            },
            isChange: false,
            isAdd: false,
            loading: false,
            selectedRowKeys: [],
            selectedRow: [],
            data: [],
            userName: '',
            trueName: '',
            userStatus: ' '
        }
    }

    componentDidMount() {
        this.getUserData()
    }

    handleTableChange = (pagination, filters, sorter) => {
        const pager = { ...this.state.pagination }
        pager.current = pagination.current
        this.setState({
            pagination: pager,
        })
        this.getUserData({
            results: pagination.pageSize,
            page: pagination.current,
            sortField: sorter.field,
            sortOrder: sorter.order,
            ...filters,
        })

    }

    getUserData = (params = {}) => {
        this.setState({
            loading: true,
        })
        HttpClient.post('/api/user/queryUsers', {}).then((res: any) => {
            this.setState({
                loading: false,
                data: res.data,
                pagination: {
                    total: res.data.length,
                    showTotal: (total, range) => `共 ${total} 条`
                }
            })
        })
    }

    getUserName = (e) => {
        this.setState({
            userName: e.target.value
        })
    }
    getTrueName = (e) => {
        this.setState({
            trueName: e.target.value
        })
    }
    getUserStatus = (value) => {
        this.setState({
            userStatus: value
        })
    }
    queryUserInfo = (e) => {
        e.preventDefault()
        const queryParams = 'userName=' + this.state.userName + '&' + 'trueName=' + this.state.trueName + '&' + 'status=' + this.state.userStatus
        this.queryDataFn(queryParams)
    }

    queryDataFn(queryParams) {
        HttpClient.post('/api/user/queryUsers?' + queryParams).then((res) => {
            if (!res) {
                message.info('无相关数据')
            } else {
                this.setState({
                    data: res.data
                })
            }

        })
    }

    onSelectChange = (selectedRowKeys, selectedRow) => {
        this.setState({ selectedRowKeys, selectedRow })
    }

    modifyPage = () => {
        this.setState({
            isChange: !this.state.isChange
        })
        if (!this.state.isChange) {
            this.getUserData()
        }
    }

    addUser = (e) => {
        e.preventDefault()
        this.setState({
            isAdd: true
        })
        this.modifyPage()
    }

    editUser = (e) => {
        e.preventDefault()
        const userInfo: any = this.state.selectedRow
        if (userInfo.length === 1) {
            this.modifyPage()
            this.setState({
                isAdd: false
            })
            if (this.props.sendChangeData) {
                this.props.sendChangeData(userInfo[0])
            }
        } else {
            const msg = userInfo.length === 0 ? '请选择一个用户' : '每次只能编辑一个用户'
            message.warn(msg)
        }
    }

    deleteUser = (e) => {
        e.preventDefault()
        const self = this
        const confirm = Modal.confirm
        const userIds: any = this.state.selectedRowKeys
        if (userIds.length === 1) {
            // 1是内置账户admin的id
            if (userIds[0] === 1) {
                message.error('不允许删除')
                return
            }
            confirm({
                title: '提示',
                content: '确定要删除该记录？',
                okText: '确认',
                cancelText: '取消',
                onOk() {
                    self.deleteUserFn(userIds[0])
                },
                onCancel() {
                    console.log('Cancel')
                },
            })
        } else {
            const msg = userIds.length === 0 ? '请选择一条数据' : '每次只能删除一条数据'
            message.warn(msg)
        }

    }

    deleteUserFn = (userId) => {
        HttpClient.delete(`/api/user/deleteUserById?userId=${userId}`, {}).then((res) => {
            if (res.data === -1) {
                message.error('不允许删除')
                return;
            }
            if (res) {
                message.success('删除成功', 3)
                this.getUserData()
                this.setState({
                    selectedRowKeys: []
                })
            }
        })
    }

    render() {
        const { selectedRowKeys } = this.state
        const rowSelection = {
            loading: true,
            selectedRowKeys,
            onChange: this.onSelectChange,
            getCheckboxProps: record => ({
                id: record.userId + ''
            })
        }
        const userManagement = (
            <div>
                <BreadcrumbCustom />
                <div>
                    <Form layout='inline' className='user-from-top'>
                        <Row>
                            <Col md={6} sm={6} >
                                <FormItem label='用户名'>
                                    <Input placeholder='请输入' className='inputs' value={this.state.userName} onChange={this.getUserName} />
                                </FormItem>
                            </Col>
                            <Col md={6} sm={6} >
                                <FormItem label='姓名'>
                                    <Input placeholder='请输入' className='inputs' value={this.state.trueName} onChange={this.getTrueName} />
                                </FormItem>
                            </Col>
                            <Col md={6} sm={6} >
                                <FormItem label='状态'>
                                    <Select value={this.state.userStatus} onChange={this.getUserStatus}>
                                        <Option value=' '>全部</Option>
                                        <Option value='1'>启用</Option>
                                        <Option value='0'>禁用</Option>
                                    </Select>
                                </FormItem>
                            </Col>
                            <Col md={6} sm={6} >
                                <FormItem>
                                    <Button type='primary' className='user-btn' onClick={this.queryUserInfo}>查询</Button>
                                    <Button className='user-btn' >重置</Button>
                                </FormItem>
                            </Col>
                        </Row>
                        <Row className='choice-btn'>
                            <Button className='user-btn' style={{ marginLeft: '0px' }} type='primary' onClick={this.addUser}>新增</Button>
                            <Button className='user-btn' type='primary' onClick={this.editUser}>修改</Button>
                            <Button className='user-btn' type='danger' onClick={this.deleteUser}>删除</Button>
                        </Row>
                    </Form>
                    <Table columns={columns}
                        scroll={{ x: 500 }}
                        dataSource={this.state.data}
                        rowSelection={rowSelection}
                        rowKey={record => record.userId}
                        pagination={this.state.pagination}
                        onChange={this.handleTableChange}
                    />
                </div>
            </div>
        )
        const { isChange, isAdd, selectedRow } = this.state
        return (

            <div className='user-management'>

                {
                    isChange ? (isAdd ? <AddUser changePage={this.modifyPage} /> : <EditUser changePage={this.modifyPage} receivedUserInfo={selectedRow} />) : userManagement

                }

            </div>
        )

    }
}

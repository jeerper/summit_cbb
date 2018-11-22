import React from 'react'
import { Table, Input, Button, TreeSelect, Row, Col, Form, message, Modal } from 'antd'
import './deviceManagement.less'
import BreadcrumbCustom from '@components/Breadcrumb/BreadcrumbCustom'
import AddDevice from '@pages/IrrigationManagement/DeviceManagement/addDevice'
import ModifyDevice from '@pages/IrrigationManagement/DeviceManagement/modifyDevice'
import { exportExcel } from 'xlsx-oc'
import XLSX from 'xlsx'
import HttpClient from '@utils/HttpClient'

const FormItem = Form.Item
const TreeNode = TreeSelect.TreeNode

const columns = [{
    title: '测站编码',
    dataIndex: 'deviceCode',
    fixed: true
}, {
    title: '机井名称',
    dataIndex: 'deviceName'
}, {
    title: '区划',
    dataIndex: 'areaName'
},
{
    title: '经度(°)',
    dataIndex: 'longitude'
}, {
    title: '纬度(°)',
    dataIndex: 'latitude'
}, {
    title: '建站年月',
    dataIndex: 'greatTime'
}
    , {
    title: '管理人员',
    dataIndex: 'deviceManager'
}
    , {
    title: '联系方式',
    dataIndex: 'managerPhone'
}]

export interface IProps {
    sendChangeData?: (content?: {}) => void
}

interface IState {
    isChangePage?: boolean
    isAdd?: boolean
    selectedRowKeys?: any[]
    selectedRows?: any[]
    pagination?: any
    treeSelect?: any
    data?: any
    loading?: boolean
    queryDeviceCode?: any
    queryDeviceName?: any
    divisionData?: any
}

export default class DeviceManagement extends React.Component<IProps, IState> {
    constructor(props: IProps, state: IState) {
        super(props)
        this.state = {
            isChangePage: false,
            isAdd: true,
            loading: false,
            selectedRowKeys: [],
            selectedRows: [],
            treeSelect: [],
            data: [],
            pagination: {
                showSizeChanger: true,
                showQuickJumper: true,
                total: 0,
                showTotal: (total, range) => `共 ${total} 条`
            },
            queryDeviceCode: '',
            queryDeviceName: '',
            divisionData: []
        }
    }

    componentDidMount() {
        this.getDeviceData()
        this.getDivisionData()
    }

    onSelectChange = (selectedRowKeys, selectedRows) => {
        this.setState({ selectedRowKeys, selectedRows })
    }
    onTreeChange = (value: any) => {
        this.setState({ treeSelect: value })
    }

    getDivisionData = (params = {}) => {
        this.setState({
            loading: true,
        })
        HttpClient.post('/api/area/queryTreeAreas', {}).then((res: any) => {
            this.setState({
                loading: false,
                divisionData: res.data
            })
        })
    }
    modifyDevice = () => {
        this.setState({
            isChangePage: !this.state.isChangePage,
        })
        if (!this.state.isChangePage) {
            this.getDivisionData()
            this.getDeviceData()
        }
    }
    handleTableChange = (pagination, filters, sorter) => {
        const pager = { ...this.state.pagination }
        pager.current = pagination.current
        this.setState({
            pagination: pager,
        })
        this.getDeviceData({
            results: pagination.pageSize,
            page: pagination.current,
            sortField: sorter.field,
            sortOrder: sorter.order,
            ...filters,
        })

    }

    getDeviceData = (params = {}) => {
        this.setState({
            loading: true,
        })
        HttpClient.post('api/queryDevices', {}).then((res: any) => {
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

    getDeviceCode = (e) => {
        this.setState({
            queryDeviceCode: e.target.value
        })
    }

    getDeviceName = (e) => {
        this.setState({
            queryDeviceName: e.target.value
        })
    }
    handleSearch = (e) => {
        e.preventDefault()
        const queryParams = 'deviceCode=' + this.state.queryDeviceCode + '&' + 'deviceName=' + this.state.queryDeviceName + '&' + 'areaId=' + this.state.treeSelect
        this.queryDataFn(queryParams)
    }

    queryDataFn(queryParams) {
        HttpClient.post('/api/queryDevices?' + queryParams).then((res) => {
            if (!res) {
                message.info('无相关数据')
            } else {
                this.setState({
                    data: res.data
                })
            }

        })
    }

    handleReset = (e) => {
        e.preventDefault()
        this.setState({
            queryDeviceCode: '',
            queryDeviceName: '',
            treeSelect: ''
        })
    }

    addDevice = (e) => {
        e.preventDefault()
        this.modifyDevice()
        this.setState({
            isAdd: true
        })
    }
    editDevice = (e) => {
        e.preventDefault()
        const deviceInfo: any = this.state.selectedRows
        if (deviceInfo.length === 1) {
            this.modifyDevice()
            this.setState({
                isAdd: false
            })
            if (this.props.sendChangeData) {
                this.props.sendChangeData(deviceInfo[0])
            }
        } else {
            const msg = deviceInfo.length === 0 ? '请选择一个测站' : '每次只能编辑一个测站'
            message.warn(msg)
        }

    }
    deleteDevice = () => {
        const self = this
        const confirm = Modal.confirm
        const deviceIds: any = self.state.selectedRowKeys
        if (deviceIds.length === 1) {
            confirm({
                title: '提示',
                content: '确定要删除该记录？',
                okText: '确认',
                cancelText: '取消',
                onOk() {
                    self.deleteDeviceFn(deviceIds[0])
                },
                onCancel() {
                    console.log('Cancel')
                },
            })
        } else {
            const msg = deviceIds.length === 0 ? '请选择一个测站' : '每次只能删除一个测站'
            message.warn(msg)
        }

    }

    deleteDeviceFn = (deviceId) => {
        HttpClient.delete(`/api/deleteDeviceById/${deviceId}`, {}).then((res) => {
            if (res) {
                message.success('删除成功', 3)
                this.getDeviceData()
                this.setState({
                    selectedRowKeys: []
                })
            }
        })
    }

    handleFileChange = (file) => {
        const { files } = file.target
        const fileReader = new FileReader()
        fileReader.onload = event => {
            try {
                const obj: any = (event.target as any).result
                const workbook = XLSX.read(obj, { type: 'binary' })
                let data = []
                for (const sheet in workbook.Sheets) {
                    if (workbook.Sheets.hasOwnProperty(sheet)) {
                        data = data.concat(XLSX.utils.sheet_to_json(workbook.Sheets[sheet]))
                    }
                }
                message.success('上传成功！')
            } catch (e) {
                message.error('文件类型不正确！')
            }
        }
        // 以二进制方式打开文件
        fileReader.readAsBinaryString(files[0])
    }

    exportDefaultExcel = (e) => {
        e.preventDefault()
        const headers = [{ k: 'deviceCode', v: '测站编码' }, { k: 'deviceName', v: '机井名称' }, { k: 'areaName', v: '区划' },
        { k: 'longitude', v: '经度(°)' }, { k: 'latitude', v: '纬度(°)' }, { k: 'greatTime', v: '建站年月' },
        { k: 'deviceManager', v: '管理人员' }, { k: 'managerPhone', v: '联系方式' }]
        exportExcel(headers, this.state.data)
    }

    exportDefaultModel = (e) => {
        e.preventDefault()
        const headers = [{ k: '', v: '测站编码' }, { k: '', v: '机井名称' }, { k: '', v: '区划' },
        { k: '', v: '经度(°)' }, { k: '', v: '纬度(°)' }, { k: '', v: '建站年月' },
        { k: '', v: '管理人员' }, { k: '', v: '联系方式' }]
        exportExcel(headers, this.state.data)
    }

    render() {
        const { selectedRowKeys } = this.state
        const rowSelection = {
            loading: true,
            selectedRowKeys,
            onChange: this.onSelectChange,
            getCheckboxProps: record => ({
                id: record.deviceId + ''
            })
        }
        const deviceManager = (
            <div>
                <BreadcrumbCustom />
                <Form layout='inline' className='device-form-top'>
                    <Row className='search-btn'>
                        <Col sm={6} md={6}>
                            <FormItem label='测站编码'>
                                <Input placeholder='请输入' className='inputs' value={this.state.queryDeviceCode} onChange={this.getDeviceCode} />
                            </FormItem>
                        </Col>
                        <Col sm={6} md={6}>
                            <FormItem label='机井名称'>
                                <Input placeholder='请输入' className='inputs' value={this.state.queryDeviceName} onChange={this.getDeviceName} />
                            </FormItem>
                        </Col>
                        <Col sm={6} md={6}>
                            <FormItem label='区划'>
                                <TreeSelect
                                    showSearch
                                    value={this.state.treeSelect}
                                    dropdownStyle={{ maxHeight: 400, overflow: 'auto' }}
                                    placeholder='请选择'
                                    allowClear={true}
                                    treeDefaultExpandAll
                                    onChange={this.onTreeChange}
                                >
                                    {
                                        this.state.divisionData.map((item) => (
                                            <TreeNode title={item.areaName} key={item.areaId} value={item.areaId}>
                                                {
                                                    item.childs.map((sonItem) => (
                                                        <TreeNode title={sonItem.areaName} key={sonItem.areaId} value={sonItem.areaId}>
                                                            {
                                                                sonItem.childs.map((grandItem) => (
                                                                    <TreeNode title={grandItem.areaName} key={grandItem.areaId} value={grandItem.areaId} isLeaf />
                                                                ))}
                                                        </TreeNode>
                                                    ))}
                                            </TreeNode>
                                        ))}
                                </TreeSelect>
                            </FormItem>
                        </Col>
                        <Col sm={6} md={6}>
                            <FormItem>
                                <Button type='primary' className='device-btn' onClick={this.handleSearch}>查询</Button>
                                <Button className='device-btn' onClick={this.handleReset}>重置</Button>
                            </FormItem>
                        </Col>
                    </Row>
                    <Row className='choice-btn'>
                        <Button className='device-btn' style={{ marginLeft: '0px' }} type='primary' onClick={this.addDevice}>新增</Button>
                        <Button className='device-btn' type='primary' onClick={this.editDevice}>修改</Button>
                        <Button className='device-btn' type='danger' onClick={this.deleteDevice}>删除</Button>
                        <Button className='device-btn' type='default' onClick={this.exportDefaultExcel}>导出</Button>
                        <Button className='device-btn upload'>
                            <input type='file' accept='.xlsx, .xls' onChange={this.handleFileChange} />
                            <span>导入</span>
                        </Button>
                        <Button className='device-btn' type='default' onClick={this.exportDefaultModel}>模板</Button>
                    </Row>
                </Form>
                <Table
                    columns={columns}
                    scroll={{ x: 1000 }}
                    rowSelection={rowSelection}
                    dataSource={this.state.data}
                    rowKey={record => record.deviceId}
                    pagination={this.state.pagination}
                    loading={this.state.loading}
                    onChange={this.handleTableChange}
                />
            </div>
        )
        const { isChangePage, isAdd, selectedRows } = this.state
        return (
            <div className='device-management'>
                {
                    isChangePage ? (isAdd ? (<AddDevice changePage={this.modifyDevice} />) : (<ModifyDevice changePage={this.modifyDevice} deviceData={selectedRows} />)) : deviceManager
                }
            </div>
        )
    }
}

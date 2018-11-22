import React from 'react'
import BreadcrumbCustom from '@components/Breadcrumb/BreadcrumbCustom'
import './divisionMangement.less'
import { Tree, Button, Table, Layout, message, Modal, Row } from 'antd'
import AddDivision from '@pages/IrrigationManagement/DivisionManagement/addDivision'
import ModifyDivision from '@pages/IrrigationManagement/DivisionManagement/modifyDivision'
import { exportExcel } from 'xlsx-oc'
import XLSX from 'xlsx'
import HttpClient from '@utils/HttpClient'

const { Header, Sider, Content } = Layout
const DirectoryTree = Tree.DirectoryTree
const TreeNode = Tree.TreeNode

const columns = [
    {
        title: '区划名',
        dataIndex: 'areaName',
        fixed: true
    },
    {
        title: '灌区',
        dataIndex: 'irrigatedArea'
    },
    {
        title: '级别',
        dataIndex: 'areaLevel'
    },
    {
        title: '备注',
        dataIndex: 'areaDescription'
    }
]

export interface IProps {
    sendChangeData?: (content?: {}) => void
}

interface IState {
    isChangePage?: boolean
    isAdd?: boolean
    loading?: boolean
    selectedRowKeys?: any[]
    selectedRows?: any[]
    pagination?: any
    treeSelect?: any
    treeData?: any
    data?: any
    pid?: any
    areaPid?: any
    pName?: any
    areaPname?: any
    pLevel?: any
    areaPlevel?: any
}

export default class DivisionManagement extends React.Component<IProps, IState> {
    constructor(props: IProps, state: IState) {
        super(props)
        this.state = {
            isChangePage: false,
            isAdd: true,
            loading: false,
            selectedRowKeys: [],
            selectedRows: [],
            treeSelect: '',
            treeData: [],
            data: [],
            pagination: {
                showSizeChanger: true,
                showQuickJumper: true,
                total: 0,
                showTotal: (total, range) => `共 ${total} 条`
            },
            pid: '',
            pName: '未配置',
            pLevel: ''
        }
    }
    componentDidMount() {
        this.getDivisionData()
        this.changeMenus(this.state.pid)
    }

    handleTableChange = (pagination, filters, sorter) => {
        const pager = { ...this.state.pagination }
        pager.current = pagination.current
        this.setState({
            pagination: pager,
        })
        this.getDivisionData({
            results: pagination.pageSize,
            page: pagination.current,
            sortField: sorter.field,
            sortOrder: sorter.order,
            ...filters,
        })

    }
    getDivisionData = (params = {}) => {
        this.setState({
            loading: true,
        })
        HttpClient.post('/api/area/queryTreeAreas', {}).then((res: any) => {
            const pagination = { ...this.state.pagination }
            this.setState({
                loading: false,
                treeData: res.data,
                pagination
            })
            this.changeMenus(res.data[0].areaId)

        })
    }

    onSelect = (selectedKeys, e: any) => {
        const len = e.selectedNodes.length
        const title = e.selectedNodes[len - 1].props.title
        this.setState({
            treeSelect: selectedKeys,
            pid: String(selectedKeys),
            pName: title
        })
        this.changeMenus(selectedKeys)
    }

    changeMenus = (queryParams) => {
        this.setState({
            loading: true,
        })
        HttpClient.post(`/api/area/queryChildAreas?areaId=${queryParams}`).then((res: any) => {
            if (res.data.length === 0) {
                this.setState({
                    loading: false,
                    data: [],
                    pLevel: Number(this.state.pLevel) + 1
                })
            } else {
                this.setState({
                    loading: false,
                    data: res.data,
                    pLevel: res.data[0].areaLevel,
                    pagination: {
                        total: res.data.length,
                        showTotal: (total, range) => `共 ${total} 条`
                    }
                })
            }
        })
    }
    onSelectChange = (selectedRowKeys, selectedRows) => {
        this.setState({ selectedRowKeys, selectedRows })
    }
    modifyDivision = () => {
        this.setState({
            isChangePage: !this.state.isChangePage,
        })
        if (!this.state.isChangePage) {
            this.getDivisionData()
        }
    }
    addDivision = (e) => {
        e.preventDefault()
        this.modifyDivision()
        this.setState({
            isAdd: true
        })
    }
    editDivision = (e) => {
        e.preventDefault()
        const devisionInfo: any = this.state.selectedRows
        if (devisionInfo.length === 1) {
            this.modifyDivision()
            this.setState({
                isAdd: false
            })
            if (this.props.sendChangeData) {
                this.props.sendChangeData(devisionInfo[0])
            }
        } else {
            const msg = devisionInfo.length === 0 ? '请选择一个区域' : '每次只能编辑一个区域'
            message.warn(msg)
        }

    }
    deleteDivision = () => {
        const self = this
        const confirm = Modal.confirm
        const divisionIds: any = self.state.selectedRowKeys
        if (divisionIds.length === 1) {
            confirm({
                title: '提示',
                content: '确定要删除该记录？',
                okText: '确认',
                cancelText: '取消',
                onOk() {
                    self.deleteDivisionFn(divisionIds[0])
                },
                onCancel() {
                    console.log('Cancel')
                },
            })
        } else {
            const msg = divisionIds.length === 0 ? '请选择一个菜单' : '每次只能删除一个菜单'
            message.warn(msg)
        }

    }
    deleteDivisionFn = (divisionId) => {
        HttpClient.delete(`/api/area/deleteAreaById/${divisionId}`, {}).then((res) => {
            if (res) {
                message.success('删除成功', 3)
                this.getDivisionData()
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

    exportDefaultModel = (e) => {
        e.preventDefault()
        const headers = [{ k: '', v: '区划名' }, { k: '', v: '灌区' },
        { k: '', v: '级别' }, { k: '', v: '备注' }]
        exportExcel(headers, this.state.data)
    }
    render() {
        const { selectedRowKeys } = this.state
        const rowSelection = {
            loading: true,
            selectedRowKeys,
            onChange: this.onSelectChange,
            getCheckboxProps: record => ({
                id: record.areaId + ''
            })
        }
        const { data } = this.state
        const divisionManager = (
            <div>
                <BreadcrumbCustom />
                <Layout>
                    <Layout className='division-content'>
                        <Header className='division-head'>
                            <Row >
                                <Button className='division-btn' type='primary' onClick={this.addDivision}>新增</Button>
                                <Button className='division-btn' type='primary' onClick={this.editDivision}>修改</Button>
                                <Button className='division-btn' type='danger' onClick={this.deleteDivision}>删除</Button>
                                <Button className='division-btn upload'>
                                    <input type='file' accept='.xlsx, .xls' onChange={this.handleFileChange} />
                                    <span>导入</span>
                                </Button>
                                <Button className='division-btn' type='default' onClick={this.exportDefaultModel}>模板</Button>
                            </Row>
                        </Header>
                        <Content>
                            <Table
                                rowSelection={rowSelection}
                                columns={columns}
                                scroll={{ x: 500 }}
                                rowKey={record => record.areaId}
                                dataSource={data}
                                pagination={this.state.pagination}
                                loading={this.state.loading}
                                onChange={this.handleTableChange}
                            />
                        </Content>
                    </Layout>
                    <Sider
                        breakpoint='md'
                        collapsedWidth='0'
                        onBreakpoint={(broken) => { console.log(broken)}}
                        onCollapse={(collapsed, type) => { console.log(collapsed, type)}}
                    >
                        <DirectoryTree
                            multiple
                            defaultExpandAll={false}
                            onSelect={this.onSelect}
                        >
                            {
                                this.state.treeData.map((item) => (
                                    <TreeNode title={item.areaName} key={item.areaId}>
                                        {
                                            item.childs.map((sonItem) => (
                                                <TreeNode title={sonItem.areaName} key={sonItem.areaId}>
                                                    {
                                                        sonItem.childs.map((grandItem) => (
                                                            <TreeNode title={grandItem.areaName} key={grandItem.areaId} isLeaf />
                                                        ))}
                                                </TreeNode>
                                            ))}
                                    </TreeNode>
                                ))}
                        </DirectoryTree>
                    </Sider>
                </Layout>
            </div>
        )

        const { isChangePage, isAdd, selectedRows } = this.state

        return (
            <div className='division-management'>
                {
                    isChangePage ? (isAdd ? (<AddDivision changePage={this.modifyDivision} areaPid={this.state.pid} areaPname={this.state.pName} areaPlevel={this.state.pLevel} />) : (<ModifyDivision changePage={this.modifyDivision} divisionData={selectedRows} areaPname={this.state.pName} />)) : divisionManager
                }
            </div>
        )
    }
}

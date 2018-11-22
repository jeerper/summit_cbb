import React from 'react'
import { Button, Form, Input, Card, TreeSelect, DatePicker, message } from 'antd'
import './../deviceManagement.less'
import { FormComponentProps } from 'antd/lib/form'
import HttpClient from '@utils/HttpClient'
import moment from 'moment'

const FormItem = Form.Item
const TreeNode = TreeSelect.TreeNode

export interface IProps extends FormComponentProps {
    changePage?: any
    form: any
}

interface IState {
    deviceCode?: any
    deviceName?: any
    areaId?: any
    areaName?: string
    longitude?: any
    latitude?: any
    greatTime?: any
    deviceManager?: any
    managerPhone?: any
    divisionData?: any
    loading?: boolean
}

class AddDevice extends React.Component<IProps, IState> {
    constructor(props: IProps, state: IState) {
        super(props)
        this.state = {
            deviceCode: '',
            deviceName: '',
            areaId: '',
            areaName: '',
            longitude: '',
            latitude: '',
            greatTime: '',
            deviceManager: '',
            managerPhone: '',
            divisionData: [],
            loading: false
        }
    }
    componentDidMount() {
        this.getDivisionData()
    }

    onTreeChange = (value: any, label: string) => {
        this.setState({ 
            areaId: value,
            areaName: label[0]
         })
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

    handleSubmit = (e) => {
        e.preventDefault()
        this.props.form.validateFields((err, fieldsValue) => {
            if (err) {
                return
            }
            const values = {
                ...fieldsValue,
                'datePicker': fieldsValue['date-picker'].format('YYYY-MM-DD')
            }
            const params = {
                deviceCode: values.deviceCode,
                deviceName: values.deviceName,
                areaId: this.state.areaId,
                areaName: this.state.areaName,
                longitude: values.longitude,
                latitude: values.latitude,
                greatTime: values.datePicker,
                deviceManager: values.deviceManager,
                managerPhone: values.managerPhone
            }
            HttpClient.post('/api/insertDevice', params).then((res) => {
                if (res) {
                    message.success('添加成功', 3)
                    this.props.changePage()
                }
            })
        })
    }
    render() {
        const formItemLayout = {
            labelCol: {
                xs: { span: 24 },
                sm: { span: 7 },
            },
            wrapperCol: {
                xs: { span: 24 },
                sm: { span: 16 },
                md: { span: 12 },
            },
        }

        const submitFormLayout = {
            wrapperCol: {
                xs: { span: 24, offset: 0 },
                sm: { span: 10, offset: 7 },
            },
        }

        const { getFieldDecorator } = this.props.form
        const date = new Date()
        const config = {
            initialValue: moment(date, 'YYYY-MM-DD'),
            rules: [{ required: false, type: 'object', message: '请选择日期!' }],
        }
        return (
            <div className='add-device'>
                <Card title='新增设备'
                    bordered={false}
                >
                    <Form
                        onSubmit={this.handleSubmit}
                    >
                        <FormItem
                            {...formItemLayout}
                            label='测站编码'>
                            {getFieldDecorator('deviceCode', {
                                rules: [{
                                    required: true,
                                    message: '请检查长度',
                                    pattern: /^[0-9]{8,14}$/
                                }],
                            })(
                                <Input className='input' name='deviceCode' />
                            )}
                        </FormItem>

                        <FormItem
                            {...formItemLayout}
                            label='机井名称'>
                            {getFieldDecorator('deviceName', {
                                rules: [{
                                    required: true,
                                    message: '请输入机井名称',
                                    max: 100
                                }],
                            })(
                                <Input className='input' name='deviceName' />
                            )}

                        </FormItem>

                        <FormItem
                            {...formItemLayout}
                            label='区划'>
                            <TreeSelect
                                    showSearch
                                    value={this.state.areaId}
                                    dropdownStyle={{ maxHeight: 400, overflow: 'auto' }}
                                    placeholder='选择区划'
                                    allowClear
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
                        <FormItem
                            {...formItemLayout}
                            label='经度(°)'>
                            {getFieldDecorator('longitude', {
                                rules: [{
                                    message: '请输入经度',
                                    pattern: /^[\-\+]?(0?\d{1,2}\.\d{1,5}|1[0-7]?\d{1}\.\d{1,7}|180\.0{1,7})$/
                                }],
                            })(
                                <Input className='input' name='longitude' />
                            )}

                        </FormItem>
                        <FormItem
                            {...formItemLayout}
                            label='纬度(°)'>
                            {getFieldDecorator('latitude', {
                                rules: [{
                                    message: '请输入纬度',
                                    pattern: /^[\-\+]?([0-8]?\d{1}\.\d{1,7}|90\.0{1,7})$/
                                }],
                            })(
                                <Input className='input' name='latitude' />
                            )}

                        </FormItem>
                        <FormItem
                            {...formItemLayout}
                            label='建站年月'>
                            {getFieldDecorator('date-picker', config)(
                                <DatePicker showToday />
                            )}

                        </FormItem>
                        <FormItem
                            {...formItemLayout}
                            label='管理人员'>
                            {getFieldDecorator('deviceManager', {
                                rules: [{
                                    message: '请输入管理人员名称',
                                    max: 20
                                }],
                            })(
                                <Input className='input' name='deviceManager' />
                            )}

                        </FormItem>
                        <FormItem
                            {...formItemLayout}
                            label='联系电话'>
                            {getFieldDecorator('managerPhone', {
                                rules: [{
                                    message: '请输入联系电话',
                                    pattern: /^[0-9]{7,15}$/
                                }],
                            })(
                                <Input className='input' name='managerPhone' />
                            )}

                        </FormItem>
                        <FormItem {...submitFormLayout} className='form-btn' >
                            <Button type='primary' htmlType='submit'>保存</Button>
                            <Button style={{ marginLeft: 8 }} onClick={this.props.changePage}>关闭</Button>
                        </FormItem>
                    </Form>
                </Card>
            </div>)
    }
}
export default Form.create()(AddDevice)

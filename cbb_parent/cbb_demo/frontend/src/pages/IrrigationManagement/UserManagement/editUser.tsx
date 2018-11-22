import React from 'react'
import { Button, Form, Input, Card, Radio, TreeSelect, message } from 'antd'
import { FormComponentProps } from 'antd/lib/form'
import HttpClient from '@utils/HttpClient'

const RadioGroup = Radio.Group
const FormItem = Form.Item
const TreeNode = TreeSelect.TreeNode
export interface IProps extends FormComponentProps {
    changePage?: any
    receivedUserInfo?: any
    form: any
}

interface IState {
    userName?: any
    trueName?: any
    userPwd?: any
    areaId?: any
    areaName?: any
    userPhone?: any
    userStatus?: any
    divisionData?: any
    loading?: boolean
}

class EditUser extends React.PureComponent<IProps, IState> {
    constructor(props: IProps, state: IState) {
        super(props)
        this.state = {
            userName: this.props.receivedUserInfo[0].userName,
            trueName: this.props.receivedUserInfo[0].trueName,
            userPwd: '',
            areaId: this.props.receivedUserInfo[0].areaId,
            areaName: this.props.receivedUserInfo[0].areaName,
            userPhone: this.props.receivedUserInfo[0].userPhone,
            userStatus: this.props.receivedUserInfo[0].userStatus,
            loading: false,
            divisionData: []
        }
    }

    filterOption = (inputValue, option) => {
        return option.title.indexOf(inputValue) > -1
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
        this.props.form.validateFields((err, values) => {
            if (err) {
                return
            }
            const params = {
                userId: this.props.receivedUserInfo[0].userId,
                userName: values.userName,
                trueName: values.trueName,
                userPwd: values.userPwd,
                areaId: this.state.areaId,
                areaName: this.state.areaName,
                userPhone: values.userPhone,
                userStatus: values.userStatus
            }

            HttpClient.put('/api/user/updateUserById', params).then((res) => {
                if (res) {
                    message.success('修改成功', 3)
                    this.props.changePage()
                }
            })
        })
    }

    render() {
        const { getFieldDecorator } = this.props.form
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

        return (
            <div className='modify-user'>
                <Card title='修改用户'
                    bordered={false}
                >
                    <Form
                        onSubmit={this.handleSubmit}
                    >
                        <FormItem
                            {...formItemLayout}
                            label='用户名'>
                            {getFieldDecorator('userName', {
                                initialValue: this.state.userName,
                                rules: [{
                                    required: true,
                                    message: '请输入用户名',
                                    max: 100
                                }],
                            })(
                                <Input className='input' name='userName' />
                            )}
                        </FormItem>

                        <FormItem
                            {...formItemLayout}
                            label='姓名'>

                            {getFieldDecorator('trueName', {
                                initialValue: this.state.trueName,
                                rules: [{
                                    message: '请输入姓名',
                                    max: 20
                                }],
                            })(
                                <Input className='input' name='trueName'
                                />
                            )}
                        </FormItem>

                        <FormItem
                            {...formItemLayout}
                            label='密码'>
                            {getFieldDecorator('userPwd', {
                                rules: [{
                                    required: true,
                                    message: '密码为必输的8位数字字母组合',
                                    pattern: /^[0-9a-zA-Z]{6,20}$/
                                }],
                            })(
                                <Input className='input' name='userPwd' />
                            )}
                        </FormItem>

                        <FormItem {...formItemLayout} label='区划'>
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
                            label='联系电话'>
                            {getFieldDecorator('userPhone', {
                                initialValue: this.state.userPhone,
                                rules: [{
                                    message: '必须为数字',
                                    pattern: /^[0-9]{7,20}$/
                                }],
                            })(
                                <Input className='input' name='userPhone' />
                            )}
                        </FormItem>

                        <FormItem
                            {...formItemLayout}
                            label='状态'>
                            {getFieldDecorator('userStatus', {
                                initialValue: this.state.userStatus
                            })(
                                <RadioGroup >
                                    <Radio value='1'>启用</Radio>
                                    <Radio value='0'>禁用</Radio>
                                </RadioGroup>
                            )}
                        </FormItem>

                        <FormItem {...submitFormLayout} className='form-btn' >
                            <Button type='primary' htmlType='submit'>
                                提交
                        </Button>
                            <Button style={{ marginLeft: 8 }} onClick={this.props.changePage}>返回</Button>
                        </FormItem>
                    </Form>
                </Card>
            </div>
        )
    }
}

export default Form.create()(EditUser)

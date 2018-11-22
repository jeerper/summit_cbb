import React from 'react'
import { Button, Form, Input, Select, Card, message } from 'antd'
import './../divisionMangement.less'
import { FormComponentProps } from 'antd/lib/form'
import HttpClient from '@utils/HttpClient'

const FormItem = Form.Item
const Option = Select.Option
export interface IProps extends FormComponentProps {
    changePage?: any
    divisionData?: any
    form: any
    areaPname?: any
}

interface IState {
    areaName?: string
    irrigatedArea?: string
    areaLevel?: any
    areaDescription?: string
}

class ModifyDivision extends React.Component<IProps, IState> {
    constructor(props: IProps, state: IState) {
        super(props)
        this.state = {
            areaName: this.props.divisionData[0].areaName,
            irrigatedArea: this.props.divisionData[0].irrigatedArea,
            areaLevel: this.props.divisionData[0].areaLevel,
            areaDescription: this.props.divisionData[0].areaDescription
        }
    }

    handleSubmit = (e) => {
        e.preventDefault()
        this.props.form.validateFields((err, values) => {
            if (err) {
                return
            }
            const params = {
                areaId: this.props.divisionData[0].areaId,
                areaName: values.areaName,
                irrigatedArea: values.irrigatedArea,
                areaLevel: values.areaLevel,
                areaDescription: values.areaDescription
            }
            HttpClient.put('/api/area/updateAreaById', params).then((res) => {
                if (res) {
                    message.success('修改成功', 3)
                    this.props.changePage()
                }
            })
        })
    }
    render() {
        const formItemLayout = {
            labelCol: {
                xs: { span: 24 },
                sm: { span: 7 }
            },
            wrapperCol: {
                xs: { span: 24 },
                sm: { span: 16 },
                md: { span: 12 }
            }
        }

        const submitFormLayout = {
            wrapperCol: {
                xs: { span: 24, offset: 0 },
                sm: { span: 10, offset: 7 }
            }
        }

        const { getFieldDecorator } = this.props.form
        return (
            <div className='modify-division'>
                <Card title='修改机构'
                    bordered={false}
                >
                    <Form
                        onSubmit={this.handleSubmit}
                    >
                        <FormItem
                            {...formItemLayout}
                            label='上级区划名'>
                            <Input className='input' name='superiorDivision' disabled={true} value={this.props.areaPname}/>
                        </FormItem>

                        <FormItem
                            {...formItemLayout}
                            label='区划名'
                        >
                            {getFieldDecorator('areaName', {
                                initialValue: this.state.areaName,
                                rules: [{
                                    required: true,
                                    message: '请输入区划名',
                                    max: 100
                                }],
                            })(
                                <Input className='input' name='areaName'/>
                            )}

                        </FormItem>
                        <FormItem
                            {...formItemLayout}
                            label='灌区'
                        >
                            {getFieldDecorator('irrigatedArea', {
                                initialValue: this.state.irrigatedArea,
                                rules: [{
                                    required: false,
                                    message: '请输入灌区名',
                                    max: 100
                                }],
                            })(
                                <Input className='input' name='irrigatedArea'/>
                            )}

                        </FormItem>
                        <FormItem
                            {...formItemLayout}
                            label='层级'>
                            {getFieldDecorator('areaLevel', {
                                initialValue: this.state.areaLevel,
                                rules: [{
                                    required: false,
                                    message: '请选择层级'
                                }],
                            })(
                                <Select style={{ width: 100 }}>
                                    <Option value='1'>1</Option>
                                    <Option value='2'>2</Option>
                                    <Option value='3'>3</Option>
                                </Select>
                            )}

                        </FormItem>
                        <FormItem
                            {...formItemLayout}
                            label='备注'
                        >
                            {getFieldDecorator('areaDescription', {
                                initialValue: this.state.areaDescription,
                                rules: [{
                                    required: false,
                                    message: '请输入备注',
                                    max: 256,
                                   
                                }],
                            })(
                                <Input className='input' name='areaDescription'/>
                            )}
                        </FormItem>
                        <FormItem {...submitFormLayout}  className='form-btn' >
                            <Button type='primary' htmlType='submit'>保存</Button>
                            <Button style={{ marginLeft: 8 }} onClick={this.props.changePage}>关闭</Button>
                        </FormItem>
                    </Form>
                    </Card>
            </div>)
    }
}
export default Form.create()(ModifyDivision)

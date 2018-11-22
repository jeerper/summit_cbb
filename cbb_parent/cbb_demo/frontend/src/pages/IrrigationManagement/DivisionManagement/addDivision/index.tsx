import React from 'react'
import { Button, Form, Input, Card, message } from 'antd'
import './../divisionMangement.less'
import { FormComponentProps } from 'antd/lib/form'
import HttpClient from '@utils/HttpClient'

const FormItem = Form.Item
export interface IProps extends FormComponentProps {
    changePage?: any
    form: any
    areaPid?: any
    areaPname?: any
    areaPlevel?: any
}

interface IState {
    parentId?: any
    parentName?: any
    areaName?: string
    irrigatedArea?: string
    areaLevel?: number
    areaDescription?: string
}

class AddDivision extends React.Component<IProps, IState> {
    constructor(props: IProps, state: IState) {
        super(props)
        this.state = {
            parentId: '',
            parentName: '',
            areaName: '',
            irrigatedArea: '',
            areaLevel: this.props.areaPlevel,
            areaDescription: ''
        }
    }

    handleSubmit = (e) => {
        e.preventDefault()
        this.props.form.validateFields((err, values) => {
            if (err) {
                return
            }
            const params = {
                areaName: values.areaName,
                irrigatedArea: values.irrigatedArea,
                areaLevel: this.state.areaLevel,
                areaDescription: values.areaDescription,
                parentId: this.props.areaPid
            }

            HttpClient.post('/api/area/insertArea', params).then((res) => {
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
            <div className='add-division'>
                <Card title='新增机构'
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
                                rules: [{
                                    required: true,
                                    message: '请输入区划名',
                                    max: 100
                                }],
                            })(
                                <Input className='input' name='areaName' />
                            )}

                        </FormItem>
                        <FormItem
                            {...formItemLayout}
                            label='灌区'
                        >
                            {getFieldDecorator('irrigatedArea', {
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
                            <Input className='input' name='areaLevel' disabled={true} value={this.state.areaLevel}/>
                        </FormItem>
                        <FormItem
                            {...formItemLayout}
                            label='备注'
                        >
                            {getFieldDecorator('areaDescription', {
                                rules: [{
                                    required: false,
                                    message: '请输入备注',
                                    max: 256
                                }],
                            })(
                                <Input className='input' name='areaDescription' />
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
export default Form.create()(AddDivision)
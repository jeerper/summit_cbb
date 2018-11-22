import React from 'react'
import UserLayout from '@layouts/UserLayout/UserLayout'
import { Button, Form, Icon, Input } from 'antd'
import './login.less'
import HttpClient from '@utils/HttpClient'
import { FormComponentProps } from 'antd/lib/form'

const FormItem = Form.Item

export interface IProps extends FormComponentProps {
    test?: any
    form: any
    history? : any
}

interface IState {
    test?: any
    userName?: any
    userPwd?: any
}

class LoginComponent extends React.PureComponent<IProps, IState> {
    constructor(props: IProps, state: IState) {
        super(props)
        this.state = {
            userName: '',
            userPwd: ''
        }
    }

    login = (e) => {
        e.preventDefault()
        this.props.form.validateFields((err, values) => {
            if (err) {
                return
            }
            const params = {
                userName: values.userName, 
                userPwd: values.userPwd
            }
            localStorage.setItem('userName', values.userName)
            const param = JSON.stringify(params)
            HttpClient.post('/login', param).then((res: any) => {
                localStorage.setItem('accessToken', res.headers.authorization)
             //   window.location.replace('/#/app/real-time')
                this.props.history.push('/app/real-time')
            })
        })
    }


    render() {
        const { getFieldDecorator } = this.props.form

        const child = (<div className='login-div'>
            <Form onSubmit={this.login} className='login-form'>
                <FormItem >
                    {getFieldDecorator('userName', {
                        rules: [{
                            required: true
                        }],
                    })(
                        <Input prefix={<Icon type='user' style={{ color: 'rgba(0,0,0,.25)' }} />} placeholder='用户名'/>
                    )}
                    
                </FormItem>
                <FormItem >
                    {getFieldDecorator('userPwd', {
                        rules: [{
                            required: true
                        }],
                    })(
                        <Input prefix={<Icon type='lock' style={{ color: 'rgba(0,0,0,.25)' }} />} type='password' placeholder='密码' />
                    )}
                   
                </FormItem>
                <FormItem >
                    <Button type='primary'  htmlType='submit' className='login-form-button'>
                        登录
                    </Button>
                </FormItem>
            </Form>
        </div>)

        return (
            <UserLayout children={child} />
        )
    }
}

export default Form.create()(LoginComponent)

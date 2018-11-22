import React from 'react'
// 动态添加文档标题
import DocumentTitle from 'react-document-title'
import './basicLayout.less'

import {Layout} from 'antd'
import SiderMenu from '@components/SiderMenu'
import HeaderView from '@layouts/Header/Header'
import FooterView from '@layouts/Footer/FooterView'
import LoginComponent from '@pages/User/Login'
import PubSub from 'pubsub-js'

const { Content} = Layout

export interface IProps {
    test?: string
}

interface IState {
    isLogin?: boolean
}

class BasicLayout extends React.PureComponent<IProps, IState> {

    loginToken = null

    constructor(props: IProps, state: IState) {
        super(props)

        this.state = {
            isLogin: false
        }

    }

    /**
     * 动态更新文档title
     * @param pathname RouterIndex属性
     */
    getPageTitle = (pathname?: string) => {
        if (!pathname) {
            return 'Summit Operation'
        }
        return `${pathname} - Summit Operation`
    }

    checkLogin = (msg: any, data: any) => {
        this.setState({
            isLogin: data
        })
    }

    componentDidMount() {
        this.loginToken = PubSub.subscribe('isLogin', this.checkLogin)
    }

    componentWillUnmount() {
        if (this.loginToken) {
            PubSub.unsubscribe(this.loginToken)
        }
    }

    render() {
        // const {collapsed} = this.state
        const isLogin = this.state.isLogin

        const layout = (<Layout>
            <SiderMenu/>

            <Layout>
                <HeaderView/>
                <Content style={{margin: '24px 24px 0', background: '#ff0000', minHeight: 280}}>
                    Content
                </Content>
                <FooterView/>
            </Layout>
        </Layout>)

        const loginComponent = (
            <LoginComponent/>
        )

        return (
            <React.Fragment>
                <DocumentTitle title={this.getPageTitle()}>
                    {isLogin ? layout : loginComponent}
                </DocumentTitle>
            </React.Fragment>
        )
    }
}

export default BasicLayout

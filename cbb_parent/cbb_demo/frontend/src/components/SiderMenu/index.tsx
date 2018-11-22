import React from 'react'
import {Icon, Layout, Menu} from 'antd'
import './siderMenu.less'
import PubSub from 'pubsub-js'
import {Link} from 'react-router-dom'

const {Sider} = Layout

export interface IProps {
    flatMenuKeys?: any
}

interface IState {
    collapsed?: boolean
}

class SiderMenu extends React.PureComponent<IProps, IState> {

    subToken = null

    constructor(props: IProps, state: IState) {
        super(props)

        this.state = {
            collapsed: false,
        }

    }

    componentDidMount() {
        this.subToken = PubSub.subscribe('collapsed', this.collapsedSub)
    }

    componentWillUnmount() {
        if (this.subToken) {
            PubSub.unsubscribe(this.subToken)
        }
    }


    collapsedSub = (msg: any, data: any) => {
        this.setState({
            collapsed: data,
        })
    }

    render() {

        return (<Sider
            trigger={null}
            collapsible
            collapsed={this.state.collapsed}
            breakpoint='lg'
            width={256}
            className='sider light'
        >
            <div className='logo'>
                <Link to='/'>
                    <h1>{!this.state.collapsed ? 'Summit Operation' : 'Operation'}</h1>
                </Link>
            </div>


            <Menu theme='light' mode='inline' defaultSelectedKeys={['1']}>
                <Menu.Item key='1'>
                    <Icon type='user'/>
                    <span>nav 1</span>
                </Menu.Item>
                <Menu.Item key='2'>
                    <Icon type='video-camera'/>
                    <span>nav 2</span>
                </Menu.Item>
                <Menu.Item key='3'>
                    <Icon type='upload'/>
                    <span>nav 3</span>
                </Menu.Item>
            </Menu>
        </Sider>)
    }

}

export default SiderMenu

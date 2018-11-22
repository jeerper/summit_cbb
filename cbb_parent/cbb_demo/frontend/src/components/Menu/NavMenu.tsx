import React from 'react'
import './menu.less'
import { Icon, Menu } from 'antd'
import { NavLink } from 'react-router-dom'

const SubMenu = Menu.SubMenu

export interface IProps {
    test?: any
}

interface IState {
    test?: any
    openKeys?: string[]
}

export default class NavMenu extends React.PureComponent<IProps, IState> {
    rootSubmenuKeys = ['sub1']

    constructor(props: IProps, state: IState) {
        super(props)
        this.state = {
            openKeys: ['sub1'],
        }
    }

    onOpenChange = (openKeys) => {
        const stateOpenKeys = this.state.openKeys
        if (stateOpenKeys) {
            const latestOpenKey = openKeys.find((key: string) => stateOpenKeys.indexOf(key) === -1)
            if (this.rootSubmenuKeys.indexOf(latestOpenKey) === -1) {
                this.setState({ openKeys })
            } else {
                this.setState({
                    openKeys: latestOpenKey ? [latestOpenKey] : [],
                })
            }
        }
    }

    render() {
        return (<Menu theme='dark'
            mode='inline'
            openKeys={this.state.openKeys}
            onOpenChange={this.onOpenChange}
        >
            <Menu.Item key='1'>
                <NavLink to='/app/real-time'>
                    <Icon type='pie-chart' />
                    <span>监测数据</span>
                </NavLink>
            </Menu.Item>
            <Menu.Item key='2'>
                <NavLink to='/app/ic_info'>
                    <Icon type='desktop' />
                    <span>IC卡信息</span>
                </NavLink>
            </Menu.Item>
            <Menu.Item key='3'>
                <NavLink to='/app/statistic'>
                    <Icon type='desktop' />
                    <span>统计分析</span>
                </NavLink>
            </Menu.Item>
            <SubMenu
                key='sub1'
                title={<span><Icon type='team' /><span>系统管理</span></span>}
            >
                <Menu.Item key='4'>
                    <NavLink to='/app/division'>区划管理</NavLink>
                </Menu.Item>
                <Menu.Item key='5'>
                    <NavLink to='/app/device'>设备管理</NavLink></Menu.Item>
                <Menu.Item key='6'>
                    <NavLink to='/app/user'>用户管理</NavLink>
                </Menu.Item>
            </SubMenu>
        </Menu>)
    }
}

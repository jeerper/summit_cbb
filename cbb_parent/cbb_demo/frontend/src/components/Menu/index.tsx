import React from 'react'
import './menu.less'
import { Layout, Menu, Icon } from 'antd'
import { NavLink } from 'react-router-dom'


const { Sider } = Layout
const SubMenu = Menu.SubMenu

export interface IProps {
  test?: any
  sendCollapsed?: (content?: any) => void
}

interface IState {
  test?: any
  collapsed?: any
  size?: any
}

export default class MenuSider extends React.PureComponent<IProps, IState> {
  constructor(props: IProps, state: IState) {
    super(props)
    this.state = {
      collapsed: true,
      size: 20
    }
  }
  

  
  onCollapse = (collapsed, type) => {
    this.setState({
      size: collapsed ? 20 : 14,
      collapsed
    })
    if (this.props.sendCollapsed) {
      this.props.sendCollapsed(collapsed)
    }
  }

  render() {
    return (

      <Sider
        className='menu-sider'
        collapsible
        collapsed={this.state.collapsed}
        onCollapse={this.onCollapse}
        style={{ overflow: 'auto', height: '100vh', position: 'fixed', left: 0 }}
        defaultCollapsed={true}
      >
        <Menu
          theme='dark'
          mode='inline'
        >
          <Menu.Item key='1'>
            <NavLink to='/app/real-time'>
              <Icon type='radar-chart' style={{ fontSize: this.state.size, marginRight: 20 }} />
              <span>监测数据</span>
            </NavLink>
          </Menu.Item>
          <Menu.Item key='2'>
            <NavLink to='/app/ic_info'>
              <Icon type='credit-card' style={{ fontSize: this.state.size, marginRight: 20 }} />
              <span>IC卡信息</span>
            </NavLink>
          </Menu.Item>
          <Menu.Item key='3'>
            <NavLink to='/app/statistic'>
              <Icon type='line-chart' style={{ fontSize: this.state.size, marginRight: 20 }} />
              <span>统计分析</span>
            </NavLink>
          </Menu.Item>
          <SubMenu
            key='sub1'
            title={<span><Icon type='appstore' style={{ fontSize: this.state.size, marginRight: 20 }} /><span>系统管理</span></span>}
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
        </Menu>
      </Sider>

    )
  }
}

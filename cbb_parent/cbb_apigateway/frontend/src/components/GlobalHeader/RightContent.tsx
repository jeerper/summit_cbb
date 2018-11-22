import React from 'react'
import './globalHeader.less'
import {Avatar, Dropdown, Icon, Menu, Spin} from 'antd'

interface ICurrentUser {
    name: string
    avatar?: string
}

export interface IProps {
    currentUser?: any | ICurrentUser
}

interface IState {
    test?: any
}

class HeaderRightContent extends React.PureComponent<IProps, IState> {
    constructor(props: IProps, state: IState) {
        super(props)
    }

    render() {

        const {currentUser} = this.props

        const menu = (
            <Menu className='menu' selectedKeys={[]}>
                <Menu.Item key='userCenter'>
                    <Icon type='user'/>个人中心
                </Menu.Item>
                <Menu.Item key='logout'>
                    <Icon type='logout'/>退出登录
                </Menu.Item>
            </Menu>
        )
        return (
            <div className='right'>
                {
                    currentUser.name ? (
                        <Dropdown overlay={menu}>
                            <span className='action account'>
                                <Avatar
                                    size='small'
                                    className='avatar'
                                    src={currentUser.avatar}
                                    alt='avatar'
                                />
                                <span className='name'>{currentUser.name}</span>
                            </span>
                        </Dropdown>
                    ) : (
                        <Spin size='small' style={{marginLeft: 8, marginRight: 8}}/>
                    )
                }
            </div>
        )
    }
}

export default HeaderRightContent

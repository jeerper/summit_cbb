import React from 'react'
import {Icon, Layout} from 'antd'
// import {Link} from 'react-router-dom'
import logo from '../../assets/images/logo.png'
import './userLayout.less'

const { Footer } = Layout

const copyright = (
    <React.Fragment>
        Copyright <Icon type='copyright'/> 2018 西安山脉科技股份有限公司
    </React.Fragment>
)

export interface IProps {
    children?: React.ReactNode
}

interface IState {
    test?: any
}

class UserLayout extends React.PureComponent<IProps, IState> {
    constructor(props: IProps, state: IState) {
        super(props)
    }

    render() {
        const {children} = this.props

        return (
            <div className='user-container'>
                <div className='user-content'>
                    <div className='user-top'>
                        <div className='user-header'>
                            {/*<Link to='/'>*/}
                                <img alt='logo' className='user-logo' src={logo}/>
                                <span className='user-title'>机井灌溉管理平台</span>
                            {/*</Link>*/}
                        </div>
                        {/* <div className='user-desc'>专业水利信息化解决方案提供商</div> */}
                    </div>
                    {children}
                </div>
                <Footer style={{textAlign: 'center'}}>{copyright}</Footer>
            </div>
        )
    }
}

export default UserLayout

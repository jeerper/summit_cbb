import React from 'react'
import { Layout, Icon } from 'antd'
import Animate from 'rc-animate'
import './myHeader.less'
import { NavLink } from 'react-router-dom'
import logo from '../../assets/images/logo.png'
const { Header } = Layout

interface IProps {
    history?: any
}
export default class MyHeader extends React.Component <IProps> {
    constructor(props: IProps) {
        super(props)
    }

    logout = () => {
        localStorage.removeItem('accessToken')
        localStorage.removeItem('userName')
     //   this.props.history.push('/login')
        // HttpClient.post('SmartHOMS/logout', {}).then((res: any) => {
        //     if (res) {
        //         window.location.replace('/#/login')
        //     }
        // })

    }

    render() {
        const HeaderDom = (<Header className='myHeader'>
            <div className='myHeader-left'>
            <img alt='logo' className='header-logo' src={logo}/>
                <span>机井灌溉管理平台</span>
            </div>

            <div className='myHeader-right'>
            <NavLink to='/login'>
            <span onClick={this.logout}>
               <Icon type='logout' style={{fontSize: 16, cursor: 'pointer'}}/>
               </span>
            </NavLink>
            {/* <div className='myHeader-right'>
           
            <span onClick={this.logout}>
               <Icon type='user' style={{fontSize: 16, cursor: 'pointer'}}/>
               </span> */}
           
              
                
                
                {/* <NavLink to='/app/gis'> */}
                    {/* <Button size='small'>
                        Gis地图
                </Button> */}
                {/* </NavLink> */}
                {/* <NavLink to='/app'> */}
                    {/* <Button size='small'>
                        运维平台
                </Button> */}
                {/* </NavLink> */}
                {/* <Button size='small' icon='poweroff' onClick={this.logout}>退出</Button> */}
            </div>
        </Header>)

        return (<Animate component='' transitionName='fade'>
            {HeaderDom}
        </Animate>)
    }
}

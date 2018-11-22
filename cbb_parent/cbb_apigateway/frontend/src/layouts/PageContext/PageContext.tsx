import React from 'react'
import { Layout } from 'antd'
import MenuSider from '@components/Menu'
import { Route, Switch, Redirect } from 'react-router-dom'
import ICCardInfo from '@pages/IrrigationManagement/ICCardInfo'
import ICCardDetail from '@pages/IrrigationManagement/ICCardInfo/detail'
import RealtimeMonitor from '@pages/IrrigationManagement/RealtimeMonitor'
import HistoryInfo from '@pages/IrrigationManagement/RealtimeMonitor/historyInfo'
import WarningInfo from '@pages/IrrigationManagement/RealtimeMonitor/warningInfo'
import StatisticAnalysis from '@pages/IrrigationManagement/StatisticAnalysis'
import DeviceManagement from '@pages/IrrigationManagement/DeviceManagement'
import DivisionManagement from '@pages/IrrigationManagement/DivisionManagement'
import UserManagement from '@pages/IrrigationManagement/UserManagement'
import CartographicInfo from '@pages/IrrigationManagement/CartographicInfo'
import './pageContext.less'
import MyHeader from '@components/MyHeader'
const { Content, Footer } = Layout
interface IProps {
    test?: any
    sendStyle?: any
}

interface IState {
    test?: any
    width?: any
}

export default class PageContext extends React.PureComponent<IProps, IState> {
    constructor(props: IProps, state: IState) {
        super(props)
        this.state = {
            width: 80
        }
    }

    getCollapsed = (collapsed) => {
        if (!collapsed) {
            this.setState({
                width: 200
            })
        } else {
            this.setState({
                width: 80
            })
        }
    }
    render() {
        return (
            <div>
                <Layout>
                    <MyHeader />
                
                <Layout style={{ marginTop: '64px' }}>

                    <MenuSider sendCollapsed={this.getCollapsed} />
                    <Layout style={{ marginLeft: this.state.width }}>
                        <Content style={{ margin: '10px', background: '#fff', minHeight: 526 }}>
                            <Switch>
                                <Route exact path='/app/real-time/history/:id' component={HistoryInfo} />
                                <Route exact path='/app/real-time/warning/:id' component={WarningInfo} />
                                <Route exact path='/app/real-time' component={RealtimeMonitor} />
                                <Route exact path='/app/cartographic' component={CartographicInfo} />
                                <Route exact path='/app/ic_info/detail/:id' component={ICCardDetail} />
                                <Route exact path='/app/ic_info' component={ICCardInfo} />
                                <Route exact path='/app/statistic' component={StatisticAnalysis} />
                                <Route exact path='/app/division' component={DivisionManagement} />
                                <Route exact path='/app/device' component={DeviceManagement} />
                                <Route exact path='/app/user' component={UserManagement} />
                                <Route path='*' render={(props) => <Redirect to='/login' />} />
                            </Switch>
                        </Content>
                        <Footer className='footer'>
                            <span>Copyright ©2018 西安山脉科技股份有限公司</span>
                        </Footer>
                    </Layout>
                </Layout>
                </Layout>
            </div>)
    }
}

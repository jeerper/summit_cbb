import React from 'react'
import {Layout} from 'antd'

import './projectLayout.less'
import {Route, Switch} from 'react-router-dom'
import PageContext from '@layouts/PageContext/PageContext'
// import GisContext from '@layouts/GisContext/GisContext'
import MyHeader from '@components/MyHeader'
const {Content} = Layout

export interface IProps {
    test?: any
}

interface IState {
    test?: any
}

export default class ProjectLayout extends React.PureComponent<IProps, IState> {
    constructor(props: IProps, state: IState) {
        super(props)
    }

    render() {
        return (<Layout>
           
           <MyHeader />
            <Content className='projectLayout-content'>
                <Switch>
                    {/* <Route exact path='/app/gis' component={GisContext}/> */}
                    <Route path='/app' component={PageContext}/>
                </Switch>
            </Content>
        </Layout>)
    }
}

import * as React from 'react'
import * as ReactDOM from 'react-dom'
import './index.less'
import {HashRouter, Redirect, Route, Switch} from 'react-router-dom'
// import ProjectLayout from '@layouts/ProjectLayout/ProjectLayout'
import LoginComponent from '@pages/User/Login'
import PageContext from '@layouts/PageContext/PageContext'
const App = () => (
    <HashRouter>
        <div>
            <Switch>
                <Route path='/login' component={LoginComponent} />
                {/* <Route path='/app' component={ProjectLayout} /> */}
                <Route path='/app' component={PageContext}/>
                <Redirect to='/login' />
            </Switch>
        </div>
    </HashRouter>
)

ReactDOM.render(
    <App/>,
    document.getElementById('root') as HTMLElement,
)

declare var module: any
if (module.hot) {
    module.hot.accept()
}

import React from 'react'
import Animate from 'rc-animate'
import {Layout} from 'antd'
import './header.less'
import GlobalHeader from '@components/GlobalHeader'

const {Header} = Layout

export interface IProps {
    test?: boolean
}

interface IState {
    test?: any
}

class HeaderView extends React.PureComponent<IProps, IState> {

    constructor(props: IProps, state: IState) {
        super(props)
    }

    render() {

        const HeaderDom = (<Header style={{padding: 0, background: 'red'}}>
            <GlobalHeader/>
        </Header>)

        return (<Animate component='' transitionName='fade'>
            {HeaderDom}
        </Animate>)
    }
}

export default HeaderView

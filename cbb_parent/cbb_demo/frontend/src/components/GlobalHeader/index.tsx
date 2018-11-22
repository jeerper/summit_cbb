import React from 'react'
import {Icon} from 'antd'
import './globalHeader.less'
import PubSub from 'pubsub-js'

export interface IProps {
    test?: boolean
}

interface IState {
    collapsed?: boolean
}

class GlobalHeader extends React.PureComponent<IProps, IState> {
    constructor(props: IProps, state: IState) {
        super(props)
        this.state = {
            collapsed: false,
        }
    }

    toggle = () => {
        this.setState({
            collapsed: !this.state.collapsed,
        })

        PubSub.publish('collapsed', !this.state.collapsed)
    }

    render() {
        const {collapsed} = this.state

        return (
            <div className='header'>
                <Icon
                    className='trigger'
                    type={collapsed ? 'menu-unfold' : 'menu-fold'}
                    onClick={this.toggle}
                />
            </div>
        )
    }
}

export default GlobalHeader

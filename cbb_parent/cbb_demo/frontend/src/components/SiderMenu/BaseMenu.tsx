import React from 'react'

export interface IProps {
    test?: any
}

interface IState {
    test?: any
}

class BaseMenu extends React.PureComponent<IProps, IState> {
    constructor(props: IProps, state: IState) {
        super(props)
    }

    render() {
        return ({})
    }
}

export default BaseMenu

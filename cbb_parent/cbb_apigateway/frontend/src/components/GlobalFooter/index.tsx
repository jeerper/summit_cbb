import React from 'react'
import './globalFooter.less'

export interface IProps {
    links?: Array<{
        key?: string;
        title: React.ReactNode;
        href: string;
        blankTarget?: boolean;
    }>
    copyright?: React.ReactNode
    // style?: React.CSSProperties // 使用硬编码
}

interface IState {
    test?: any
}

export default class GlobalFooter extends React.PureComponent<IProps, IState> {

    constructor(props: IProps, state: IState) {
        super(props)
    }

    render() {
        const {links, copyright} = this.props

        return (
            <div className='globalFooter'>
                {
                    links && (
                        <div className='links'>
                            {
                                links.map(link => (
                                    <a key={link.key} title={link.key}
                                       target={link.blankTarget ? '_blank' : '_self'}
                                       href={link.href}>
                                        {link.title}
                                    </a>
                                ))
                            }

                        </div>
                    )
                }
                {copyright && (<div className='copyright'>{copyright}</div>)}
            </div>
        )
    }
}

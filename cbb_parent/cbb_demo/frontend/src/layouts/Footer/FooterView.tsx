import React from 'react'
import {Icon, Layout} from 'antd'
import GlobalFooter from '@components/GlobalFooter'

const {Footer} = Layout

const FooterView = () => (
    <Footer style={{padding: 0}}>
        <GlobalFooter
            links={[]}
            copyright={
                <React.Fragment>
                    Copyright <Icon type='copyright'/> 2018 西安山脉科技股份有限公司
                </React.Fragment>
            }
        />
    </Footer>
)

export default FooterView

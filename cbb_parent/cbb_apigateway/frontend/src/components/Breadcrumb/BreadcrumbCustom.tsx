import React from 'react'
import { Link, withRouter } from 'react-router-dom'
import { Breadcrumb } from 'antd'
import './BreadcrumbCustom.less'
interface IProps {
  test?: any
}
interface IState {
  test?: any
}
const breadcrumbNameMap = {
  '/app/real-time': '监测数据',
  '/app/real-time/history': '历史信息',
  '/app/real-time/warning': '报警信息',
  '/app/ic_info': 'IC卡管理',
  '/app/ic_info/detail': '详情',
  '/app/user': '用户管理',
  '/app/division': '区划管理',
  '/app/device': '设备管理',
  '/app/cartographic': '地理位置',
  '/app/statistic': '数据统计'
}

const Home = withRouter((props) => {
  const { location } = props
  const snippets = location.pathname.split('/').filter(i => i)
  const pathSnippets = snippets.length === 4 ? snippets.slice(0, snippets.length - 1) : snippets
  const extraBreadcrumbItems = pathSnippets.map((_, index) => {
    const url = `/${pathSnippets.slice(0, index + 1).join('/')}`
    return (
      <Breadcrumb.Item key={url}>
        <Link to={url}>
          {breadcrumbNameMap[url]}
        </Link>
      </Breadcrumb.Item>
    )
  })

  const breadcrumbItems = extraBreadcrumbItems
  return (
    <div className='breadCrumb'>
      <Breadcrumb>
        {breadcrumbItems}
      </Breadcrumb>
    </div>
  )
})


export default class BreadcrumbCustom extends React.PureComponent<IProps, IState> {
  constructor(props: IProps, state: IState) {
    super(props)
  }

  render() {
    return (

      <Home />

    )
  }
}





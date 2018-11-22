import React from 'react'
import {Icon, message } from 'antd'
// import { Icon, message, Form, Row, TreeSelect, Button } from 'antd'
import { Link } from 'react-router-dom'
import BreadcrumbCustom from '@components/Breadcrumb/BreadcrumbCustom'
import './CartographicInfo.less'
import HttpClient from '@utils/HttpClient'
// import BMap from 'BMap'
// import { Map, NavigationControl, ScaleControl, Boundary, Marker } from 'react-bmap';


// const FormItem = Form.Item
// const TreeNode = TreeSelect.TreeNode

interface IProps {
    test?: any
    map?: any
}
interface IState {
    test?: any
    treeSelect?: any
    divisionData?: any
}
// const DATA_INFO = [
//     [86.566953, 42.049156, '永宁镇人大主席团'],
//     [86.55143, 42.081822, '焉耆站'],
//     [86.634505, 42.083964, '五号渠乡人大主席团']
// ]
// const opts = {
//     width: 250,     // 信息窗口宽度
//     height: 80,     // 信息窗口高度
//     title: '信息窗口', // 信息窗口标题
//     enableMessage: true// 设置允许信息窗发送短息
// }
// const markers = [
//     {
//         title: '永宁镇人大主席团',
//         lng: 86.566953,
//         lat: 42.049156
//     },
//     {
//         title: '焉耆站',
//         lng: 86.55143,
//         lat: 42.081822
//     },
//     {
//         title: '五号渠乡人大主席团',
//         lng: 86.634505,
//         lat: 42.083964
//     }
// ]

export default class CartographicInfo extends React.PureComponent<IProps, IState> {
    constructor(props: IProps, state: IState) {
        super(props)
        this.state = {
            treeSelect: [],
            divisionData: []
        }
    }

    componentDidMount() {
        this.getDivisionData()
        // const map = new BMap.Map('allmap') // 创建Map实例
        // map.centerAndZoom(new BMap.Point(86.57425, 42.0591), 11) // 初始化地图,设置中心点坐标和地图级别
        // // map.addControl(new BMap.MapTypeControl()) // 添加地图类型控件
        // map.setCurrentCity('焉耆') // 设置地图显示的城市 此项是必须设置的
        // map.enableScrollWheelZoom(true) // 开启鼠标滚轮缩放
        // map.addControl(new BMap.ScaleControl()) // 比例尺
        // map.addControl(new BMap.NavigationControl()) // 平移缩放
        // const bdary = new BMap.Boundary()
        // this.getBoundary(bdary, map)
    }

    // getBoundary = (bdary, map) => {
    //     bdary.get('焉耆', (rs) => {       // 获取行政区域
    //         map.clearOverlays()       // 清除地图覆盖物       
    //         const count = rs.boundaries.length // 行政区域的点有多少个
    //         if (count === 0) {
    //             alert('未能获取当前输入行政区域')
    //             return
    //         }
    //         let pointArray = []
    //         for (let i = 0 ; i < count ; i++) {
    //             const ply = new BMap.Polygon(rs.boundaries[i], { strokeWeight: 4, strokeColor: '#438cfa', fillColor: '' }) // 建立多边形覆盖物
    //             map.addOverlay(ply)  // 添加覆盖物
    //             pointArray = pointArray.concat(ply.getPath())
    //         }
    //         map.setViewport(pointArray) // 调整视野 
    //         // this.getPoint(map)
    //     })

    // }

    // getPoint = (map) => {
    //     for (const item of DATA_INFO) {
    //         const marker = new BMap.Marker(new BMap.Point(item[0], item[1]))  // 创建标注
    //         const content = '站名：' + item[2] + '<br/>' + '坐标：' + item[0] + ', ' + item[1] +
    //             '<br/>'
    //         // 将标注添加到地图中
    //         map.addOverlay(marker)
    //         this.addClickHandler(map, content, marker)
    //     }
    // }

    // addClickHandler = (map, content, marker) => {
    //     marker.addEventListener('click', (e) => {
    //         this.openInfo(map, content, e)
    //     })
    // }

    // openInfo = (map, content, e) => {
    //     const p = e.target
    //     const point = new BMap.Point(p.getPosition().lng, p.getPosition().lat)
    //     const infoWindow = new BMap.InfoWindow(content, opts)  // 创建信息窗口对象 
    //     map.openInfoWindow(infoWindow, point) // 开启信息窗口
    // }

    onTreeChange = (value: any) => {
        this.setState({ treeSelect: value })
    }
    getDivisionData = (params = {}) => {
        HttpClient.post('/api/area/queryTreeAreas', {}).then((res: any) => {
            this.setState({
                divisionData: res.data
            })
        })
    }
    handleSearch = (e) => {
        e.preventDefault()
        const queryParams = 'areaId=' + this.state.treeSelect
        this.queryDataFn(queryParams)
    }
    queryDataFn(queryParams) {
        HttpClient.post('/api/queryDevices?' + queryParams).then((res) => {
            if (!res) {
                message.info('无相关数据')
            } 
        })
    }
    // toFixedNum(num, s) {
    //     let times = Math.pow(10, s)
    //     let des: any = num * times + 0.5
    //     des = parseInt(des, 10) / times
    //     return des + ''
    // }
    render() {

        return (
            <div className='cartographic-info'>
                <BreadcrumbCustom />

                <Link to='/app/real-time'>
                    <Icon type='radar-chart' style={{ fontSize: '24px', float: 'right', marginTop: -30 }} />
                </Link>

                {/* <div id='allmap' style={{ width: '100%', height: '100vh' }} /> */}

                {/*使用react-bmap*/}
                {/* <Map id='allmap' center={{
                    lng: 86.57425,
                    lat: 42.0591
                }}
                    style={{ width: '100%', height: '100vh' }}
                    zoom='11'
                    enableScrollWheelZoom

                >
                    <NavigationControl />
                    <ScaleControl />
                    <Boundary data={[
                        {
                            name: '焉耆',
                            count: Math.random() * 100
                        }
                    ]}
                        strokeWeight='4'
                        fillColor='blue'
                        strokeColor='white'
                    />
                    {markers.map((position, index) => {
                        let order = index + 1;
                        let updown = index / 2 ? '-' : '';
                        let active = false;
                        let leftStyle = null;
                        if (index % 3 == 0) {
                            leftStyle = { background: 'blue' }
                        } else if (index % 3 == 1) {
                            active = true;
                        }
                        let ran = Math.random() * 100;
                        let rn = this.toFixedNum(ran, 2);
                        let rate = updown + rn + '%'
                        let num = rn;
                        let name = '测试' + order;
                        return <Marker
                            key={order}
                            type={'order_tip'}
                            active={active}
                            leftStyle={leftStyle}
                            map={this.props.map}
                            // rightModule={<div>hello</div>}
                            position={position}
                            name={name}
                            num={num}
                            rate={rate}
                            order={order}
                        />
                    })}
                </Map>
                <Form layout='inline' className='searchMap'>
                    <Row>
                        <FormItem label='区划'>
                            <TreeSelect
                                showSearch
                                value={this.state.treeSelect}
                                dropdownStyle={{ maxHeight: 400, overflow: 'auto' }}
                                placeholder='请选择'
                                allowClear
                                treeDefaultExpandAll
                                onChange={this.onTreeChange}
                                style={{ width: 100 }}
                            >
                                {
                                    this.state.divisionData.map((item) => (
                                        <TreeNode title={item.areaName} key={item.areaId} value={item.areaId}>
                                            {
                                                item.childs.map((sonItem) => (
                                                    <TreeNode title={sonItem.areaName} key={sonItem.areaId} value={sonItem.areaId}>
                                                        {
                                                            sonItem.childs.map((grandItem) => (
                                                                <TreeNode title={grandItem.areaName} key={grandItem.areaId} value={grandItem.areaId} isLeaf />
                                                            ))}
                                                    </TreeNode>
                                                ))}
                                        </TreeNode>
                                    ))}
                            </TreeSelect>
                        </FormItem>
                        <FormItem>
                            <Button type='primary' className='device-btn' onClick={this.handleSearch}>查询</Button>
                        </FormItem>
                    </Row>
                </Form> */}
            </div>
        )
    }
}





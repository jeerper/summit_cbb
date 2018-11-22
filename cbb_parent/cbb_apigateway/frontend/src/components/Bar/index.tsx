import React from 'react'
import { Chart, Geom, Axis, Tooltip } from 'bizcharts'
import DataSet from '@antv/data-set'

interface IProps {
    test?: any
}
interface IState {
    showLabel: boolean
}
const data = [
    {
        'category': '水量(m³)',
        '一号井': 12.4,
        '二号井': 23.2,
        '三号井': 34.5,
        '四号井': 99.7,
        '五号井': 52.6,
        '六号井': 35.5,
        '七号井': 37.4,
        '八号井': 42.4,
        '九号井': 23,
        '十号井': 45,
        '十一号井': 34,
        '十二号井': 18,
        '十三号井': 34.5,
        '十四号井': 99.7,
        '十五号井': 52.6,
        '十六号井': 35.5,
        '十七号井': 37.4,
        '十八号井': 42.4,
        '十九号井': 23,
        '二十号井': 45
    }
]
const ds = new DataSet()
const dv = ds.createView().source(data)
dv.transform({
    type: 'fold',
    fields: ['一号井', '二号井', '三号井', '四号井', '五号井', '六号井', '七号井', '八号井', '九号井', '十号井', '十一号井', '十二号井', '十三号井', '十四号井', '十五号井', '十六号井', '十七号井', '十八号井', '十九号井', '二十号井'],
    key: 'name',
    value: 'waterCon'
})
export default class BarChart extends React.Component<IProps, IState> {
    constructor(props: IProps, state: IState) {
        super(props)
        this.state = {
            showLabel: true
        }
    }

    componentDidMount() {
        window.addEventListener('resize', this.resize, { passive: true })
    }

    componentWillUnmount() {
        window.removeEventListener('resize', this.resize)
    }

    resize = () => {
        const width = window.innerWidth
        if (width < 768) {
            this.setState({
                //     showLabel: false
            })
        } else {
            this.setState({
                showLabel: true
            })
        }
    }


    render() {

        const xAxisTitle = {
            offset: 60,
            textStyle: {
                fontSize: '12',
                textAlign: 'center',
                fill: '#999',
                fontWeight: 'bold',
            },
            position: 'center'
        }
        const yAxisTitle = {
            textStyle: {
                fontSize: '12',
                textAlign: 'center',
                fill: '#999',
                fontWeight: 'bold',
                rotate: 360
            },
            position: 'end'
        }
        const cols = {
            value: {
                min: 0
            },
            year: {
                range: [0, 1]
            },
            name: {
                alias: '机井设备名称'
            },
            waterCon: {
                alias: '用水量'
            }
        }
        const label: any = {
            offset: 30, // 数值，设置坐标轴文本 label 距离坐标轴线的距离
            // 设置文本的显示样式，还可以是个回调函数，回调函数的参数为该坐标轴对应字段的数值
            textStyle: {
                textAlign: 'center',
                fill: '#404040',
                fontSize: '12',
                rotate: -190
            }
        }

        // const {showLabel} = this.state
        return (
            <div style={{ marginTop: 20, overflow: 'auto' }}>
                <div>
                    <Chart height={300} width={1750} data={dv} scale={cols}>
                        <Axis name='name' title={xAxisTitle} label={label} />
                        <Axis name='waterCon' title={yAxisTitle} position='left'/>
                        <Tooltip
                            crosshairs={{
                                type: 'y'
                            }}
                        />
                        <Geom
                            type='interval'
                            position='name*waterCon'
                            color={'category'}
                            adjust={[
                                {
                                    type: 'dodge',
                                    marginRatio: 1 / 32
                                }
                            ]}
                        />
                    </Chart>
                </div>

            </div>
        )
    }

}








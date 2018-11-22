import React from 'react'
import { Chart, Geom, Axis, Tooltip } from 'bizcharts'

interface IProps {
    test?: any
}
interface IState {
    showLabel: boolean
}

// import DataSet from '@antv/data-set'
const data = [
    {
        'month': 'Jan',
        'waterCon': 70

    },
    {
        'month': 'Feb',
        'waterCon': 69

    },
    {
        'month': 'Mar',
        'waterCon': 95
    },
    {
        'month': 'Apr',
        'waterCon': 145
    },
    {
        'month': 'May',
        'waterCon': 184
    },
    {
        'month': 'Jun',
        'waterCon': 215
    },
    {
        'month': 'Jul',
        'waterCon': 252
    },
    {
        'month': 'Aug',
        'waterCon': 26
    },
    {
        'month': 'Sep',
        'waterCon': 233
    },
    {
        'month': 'Oct',
        'waterCon': 183
    },
    {
        'month': 'Nov',
        'waterCon': 139
    },
    {
        'month': 'Dec',
        'waterCon': 96
    }
]
export default class LineChart extends React.Component <IProps, IState> {
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
        const width =  window.innerWidth
        if (width < 768) {
            this.setState({
                showLabel: false
            })
        } else {
            this.setState({
                showLabel: true
            })
        }
    }
    render() {
        
        const cols = {
            value: {
                min: 0
            },
            year: {
                range: [0, 1]
            },
            month: {
                alias: '月份'
            },
            waterCon: {
                alias: '用水量'
            }
        }
        const xAxisTitle = {
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
        const {showLabel} = this.state
        return (
            <div>
                <Chart height={400} data={data} scale={cols} forceFit>
                    <Axis name='month' title={xAxisTitle}  label={showLabel ? {} : null}/>
                    <Axis name='waterCon' title={yAxisTitle}/>
                    <Tooltip
                        crosshairs={{
                            type: 'y'
                        }}
                    />
                    <Geom type='line' position='month*waterCon' size={2}/>
                    <Geom
                        type='point'
                        position='month*waterCon'
                        size={4}
                        shape={'circle'}
                        style={{
                            stroke: '#fff',
                            lineWidth: 1
                        }}
                    />
                </Chart>
            </div>
        )
    }

}


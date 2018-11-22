import React from 'react'
import { Chart, Geom, Axis, Tooltip, Coord } from 'bizcharts'
import DataSet from '@antv/data-set'
// import {Row, Col} from 'antd'
interface IProps {
  test?: any
}
interface IState {
  chart?: any
  legendData?: any
  legendStyle? : any
}
export default class PieChart extends React.Component<IProps, IState> {
  constructor(props: IProps, state: IState) {
    super(props)
    this.state = {
      legendData: [],
      legendStyle : {}
    }
  }

  componentDidMount() {
    this.getLegendStyle()
    window.addEventListener('resize', this.resize)
  }

  componentWillUnmount() {
    window.removeEventListener('resize', this.resize)
  }

  resize = () => {
    this.getLegendStyle()
  }

  getLegendStyle = () => {
    if (window.innerWidth < 768) {
      this.setState({
        legendStyle : {
          position: 'absolute',
          top: '40%',
          right: -30
        }
      })
    } else {
      this.setState({
        legendStyle : {
          position: 'absolute',
          top: '30%',
          right: 50
        }
      })
    }
  }

  getG2Instance = (chart) => {
    this.getLegendData(chart)
  }

  getLegendData = (chart) => {
    console.log(chart)
    if (!chart) { return }
    const gemo = chart.getAllGeoms()[0]
    if (!gemo) { return }
    const items = gemo.get('dataArray') || []
    const legendData = items.map(item => {
      const origin = item[0]._origin
      origin.color = item[0].color
      return origin
    })
    this.setState({ legendData })
  }


  render() {
    const { DataView } = DataSet
    const data = [
      {
        item: '可开采',
        count: 40
      },
      {
        item: '急停',
        count: 10
      },
      {
        item: '电量不足',
        count: 5
      },
      {
        item: '水量不足',
        count: 8
      },
      {
        item: '电表故障',
        count: 12
      },
      {
        item: '水表故障',
        count: 19
      },
      {
        item: '回路故障',
        count: 6
      }
    ]
    const dv = new DataView()
    dv.source(data).transform({
      type: 'percent',
      field: 'count',
      dimension: 'item',
      as: 'percent'
    })
    const cols = {
      percent: {
        formatter: val => {
          val = val * 100 + '%'
          return val
        }
      }
    }
    const colors = ['#7ace4c', '#d81e06', '#425088', '#2c99ff', '#fbd439', '#975be6', '#37cbcb']
    const { legendData, legendStyle } = this.state
    return (
      <div>

        {/* <Row>
          <Col md={4} sm={24}>
          <Chart
          height={400}
          data={dv}
          scale={cols}
          forceFit
          padding={[80, 100, 80, 80]}
          onGetG2Instance={this.getG2Instance}
        >
          <Coord type='theta' radius={0.75} />
          <Axis name='percent' />
          <Tooltip
            showTitle={false}
            itemTpl='<li></span>{name}: {value}</li>'
          />
          <Geom
            type='intervalStack'
            position='percent'
            color={['item', colors]}
            tooltip={[
              'item*percent',
              (item, percent) => {
                percent = percent * 100 + '%'
                return {
                  name: item,
                  value: percent
                }
              }
            ]}
            style={{
              lineWidth: 1,
              stroke: '#fff'
            }}
          />
        </Chart>
        </Col>
        <Col md={2} sm={24}>
        <ul>
          {legendData.map( (item, index) => {
            return (<li key={index}>
              <span style={{
                    backgroundColor: item.color,
                  }} />
              <span>{item.item}</span>
              <span>{item.percent}</span>
              <span>{item.count}</span>
            </li>)
          })}
        </ul>
        </Col>
        </Row> */}
        <Chart
          height={400}
          data={dv}
          scale={cols}
          forceFit
          padding={[80, 100, 80, 80]}
          onGetG2Instance={this.getG2Instance}
        >
          <Coord type='theta' radius={0.75} />
          <Axis name='percent' />
          <Tooltip
            showTitle={false}
            itemTpl='<li></span>{name}: {value}</li>'
          />
          <Geom
            type='intervalStack'
            position='percent'
            color={['item', colors]}
            tooltip={[
              'item*percent',
              (item, percent) => {
                percent = percent * 100 + '%'
                return {
                  name: item,
                  value: percent
                }
              }
            ]}
            style={{
              lineWidth: 1,
              stroke: '#fff'
            }}
          />
        </Chart>
        <ul style={legendStyle}>
          {legendData.map((item, index) => {
            return (<li key={index}>
              <span style={{
                backgroundColor: item.color,
                borderRadius: 8,
                display: 'inline-block',
                marginRight: 8,
                position: 'relative',
                top: -1,
                height: 8,
                width: 8
                  }} />
              <span>{item.item}</span>
              <span>{item.percent}</span>
              <span>{item.count}</span>
            </li>)
          })}
        </ul>

      </div>
    )
  }
}
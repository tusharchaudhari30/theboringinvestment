import React, { Component } from "react";
import * as echarts from "echarts/core";
import ReactECharts from "echarts-for-react";

export default class Piechart extends Component {
  state = {
    option: {
      color: ["black"],
      title: {},
      tooltip: {
        show: true,
      },
      series: [
        {
          type: "pie",
          label: {
            position: "outside",
            color: "white",
            fontSize: ".8rem",
          },
          labelLine: {
            show: true,
            lineStyle: {
              width: 1,
              color: "white",
            },
          },
          radius: ["40%", "70%"],
          data: this.props.data,
          emphasis: {},
          itemStyle: {
            borderColor: "white",
          },
        },
      ],
    },
  };
  componentDidUpdate(prevProps, prevState, snapshot) {
    console.log("Piechart componentDidUpdate", this.props.data);
    if (prevProps.data !== this.props.data) {
      this.setState({
        option: {
          color: ["black"],
          title: {},
          tooltip: {
            show: true,
          },
          series: [
            {
              type: "pie",
              label: {
                position: "outside",
                color: "white",
                fontSize: ".8rem",
              },
              labelLine: {
                show: true,
                lineStyle: {
                  width: 1,
                  color: "white",
                },
              },
              radius: ["40%", "70%"],
              data: this.props.data,
              emphasis: {},
              itemStyle: {
                borderColor: "white",
              },
            },
          ],
        },
      });
    }
  }

  render() {
    return (
      <ReactECharts
        echarts={echarts}
        option={this.state.option}
        notMerge={true}
        lazyUpdate={true}
        theme={"theme_name"}
        style={{
          height: "100%",
          width: "100%",
        }}
      />
    );
  }
}

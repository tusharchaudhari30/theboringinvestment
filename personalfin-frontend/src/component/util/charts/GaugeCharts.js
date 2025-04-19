import React, {Component} from "react";
import * as echarts from "echarts/core";
import ReactECharts from "echarts-for-react";

export default class GaugeCharts extends Component {
    option = {
        series: [
            {
                type: "gauge",
                center: ["50%", "90%"],
                startAngle: 180,
                endAngle: 0,
                min: this.props.start,
                max: this.props.end,
                axisLine: {
                    lineStyle: {
                        color: [[1, "black"]],
                        width: 10,
                    },
                },
                splitNumber: 10,
                itemStyle: {
                    color: "white",
                },
                progress: {
                    show: true,
                    width: 10,
                },
                pointer: {
                    show: false,
                },
                axisTick: {
                    distance: -50,
                    splitNumber: 5,
                    lineStyle: {
                        width: 1,
                        color: "white",
                    },
                },
                splitLine: {
                    distance: -50,
                    length: 10,
                    lineStyle: {
                        width: 2,
                        color: "white",
                    },
                },
                axisLabel: {
                    distance: -30,
                    color: "white",
                    fontSize: 15,
                },
                anchor: {
                    show: false,
                },
                title: {
                    show: false,
                },
                detail: {
                    valueAnimation: true,
                    width: "60%",
                    lineHeight: 40,
                    borderRadius: 8,
                    offsetCenter: [0, "-15%"],
                    fontSize: 20,
                    fontWeight: "bolder",
                    formatter: "{value} " + this.props.unit,
                    color: "inherit",
                },
                data: [
                    {
                        value: this.props.value,
                    },
                ],
            },
        ],
    };

    render() {
        return (
            <div className="">
                <div className="h-40">
                    <ReactECharts
                        echarts={echarts}
                        option={this.option}
                        notMerge={true}
                        lazyUpdate={true}
                        theme={"theme_name"}
                        style={{
                            height: "100%",
                            width: "100%",
                        }}
                    />
                </div>
            </div>
        );
    }
}

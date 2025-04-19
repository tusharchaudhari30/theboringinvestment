import React, {Component} from "react";

export default class Button1 extends Component {
    render() {
        return (
            <div
                className={
                    "focus:outline-none px-2 py-1 border border-slate-300 " +
                    this.props.className
                }
                onClick={this.props.onpress}
            >
                {this.props.value}
            </div>
        );
    }
}

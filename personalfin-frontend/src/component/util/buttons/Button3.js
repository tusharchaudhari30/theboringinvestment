import React, {Component} from "react";
import "./button.scss";

export default class Button3 extends Component {
    render() {
        return (
            <div
                className={"btn " + this.props.className}
                onClick={this.props.onpress}
                onTouchStart={() => window.navigator.vibrate(20)}
            >
                {this.props.children}
            </div>
        );
    }
}

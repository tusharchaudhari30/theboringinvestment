import React, {Component} from "react";

export default class Card extends Component {
    render() {
        return (
            <div className="border border-slate-800 m-3">
                <h4 className="p-3 w-full border-b border-slate-600">
                    {this.props.title}
                </h4>
                {this.props.children}
            </div>
        );
    }
}

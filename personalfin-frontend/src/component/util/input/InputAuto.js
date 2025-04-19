import React, {Component} from "react";
import HomeClient from "../../Client/HomeClient";

export default class InputAuto extends Component {
    state = {
        visible: false,
        assetList: [{assetName: "Please Enter More"}],
        assetName: "",
    };

    onChangeAssetName = (event) => {
        this.setState({assetName: event.target.value});
        if (event.target.value < 3) {
            this.setState({assetList: [{assetName: "Please Enter More"}]});
        }
        if (event.target.value.length >= 3) {
            HomeClient.assetList(event.target.value).then((data) => {
                if (data.length === 0) {
                    this.setState({assetList: [{assetName: "Not Found."}]});
                    return;
                }
                this.setState({assetList: data});
            });
        }
    };
    onFocusAssetName = (event) => {
        this.setState({visible: true});
    };

    onClickOption = (event) => {
        if (
            event.target.innerText === "Please Enter More" ||
            event.target.innerText === "Not Found."
        ) {
            return;
        }
        this.props.changeAsset(
            this.state.assetList.filter(
                (asset) => asset.assetName === event.target.innerText
            )[0]
        );
        this.setState({
            assetName: event.target.innerText,
            visible: !this.state.visible,
        });
    };

    render() {
        return (
            <React.Fragment>
                <input
                    className="bg-black focus:outline-none border w-full p-1 text-sm border-gray-300"
                    value={this.state.assetName}
                    onChange={this.onChangeAssetName}
                    onFocus={this.onFocusAssetName}
                    placeholder="Type the Name"
                    type="text"
                />
                <div className="bg-black fixed">
                    {this.state.visible &&
                        this.state.assetList.map((s, key) => {
                            return (
                                <div
                                    className="p-1 border-gray-400 text-base border hover:text-black hover:bg-white cursor-pointer truncate"
                                    key={key}
                                    onClick={this.onClickOption}
                                    style={{width: "250px"}}
                                >
                                    {s.assetName}
                                </div>
                            );
                        })}
                </div>
            </React.Fragment>
        );
    }
}

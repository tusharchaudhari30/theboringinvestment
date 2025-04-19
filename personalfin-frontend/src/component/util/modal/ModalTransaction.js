import React, { Component } from "react";
import { toast } from "react-toastify";
import HomeClient from "../../Client/HomeClient";
import InputAuto from "../input/InputAuto";
import "./ModalTransaction.css";
export default class ModalTransaction extends Component {
  state = {
    asset: {},
    amount: 0,
    average: 0,
    date: this.formatDate(new Date()),
  };

  formatDate(date) {
    var d = new Date(date),
      month = "" + (d.getMonth() + 1),
      day = "" + d.getDate(),
      year = d.getFullYear();

    if (month.length < 2) month = "0" + month;
    if (day.length < 2) day = "0" + day;

    return [year, month, day].join("-");
  }

  changeAsset = (asset) => {
    this.setState({ asset: asset });
  };

  saveTransaction = () => {
    if (
      this.state.asset.assetName === "" ||
      this.state.asset.assetName === undefined
    ) {
      toast.error("Please Enter Asset Name");
      return;
    }
    if (this.state.amount === 0) {
      toast.error("Please Enter Amount");
      return;
    }
    if (this.state.average === 0) {
      toast.error("Please Enter Average Amount");
      return;
    }
    HomeClient.saveTransaction(
      this.state.asset.assetName,
      this.state.asset.symbol + ".NS",
      this.state.amount,
      this.state.average,
      this.state.date.toString()
    ).then((res) => {
      if (res.message === "Failed") toast.error("Transaction save Failed");
      if (res.message === "OK") toast.success("Transaction Saved");
      this.props.changeTransactionVisible();
    });
  };

  render() {
    return (
      <React.Fragment>
        <div
          className="min-h-screen fixed bg- bg-black w-full z-10 opacity-60"
          style={{ left: 0, right: 0, top: 0 }}
        ></div>
        <div className="sm:w-6/12 w-11/12 bg-black fixed opacity-100 border border-gray-500 z-20 modalbgmain">
          <div
            className="w-full border-b text-xl flex flex-wrap justify-between border-gray-500"
            onClick={this.props.changeTransactionVisible}
          >
            <span className="py-3 px-5 text-gray-300">Transaction</span>{" "}
            <div className="border-l py-3 px-5 hover:bg-white hover:text-black hover:cursor-pointer border-gray-500">
              X
            </div>
          </div>
          <div className="w-full text-gray-300">
            <div className="w-full flex justify-center">
              <table className="text-lg">
                <tbody>
                  <tr className="py-3">
                    <td className="text-right p-3 pt-6">
                      <label className="">Asset Name : </label>
                    </td>
                    <td className="p-3 pt-6">
                      <InputAuto changeAsset={this.changeAsset} />
                    </td>
                  </tr>
                  <tr className="py-3">
                    <td className="text-right p-3">
                      <label className="">Amount :</label>
                    </td>
                    <td className="p-3">
                      <input
                        className="bg-black focus:outline-none border w-full text-sm p-1 border-gray-300"
                        value={this.state.amount}
                        onChange={(event) =>
                          this.setState({ amount: event.target.value })
                        }
                        type="number"
                      />
                    </td>
                  </tr>
                  <tr className="py-3">
                    <td className="text-right p-3">
                      <label className="">Average :</label>
                    </td>
                    <td className="p-3">
                      <input
                        className="bg-black focus:outline-none border w-full text-sm p-1 border-gray-300"
                        value={this.state.average}
                        onChange={(event) =>
                          this.setState({ average: event.target.value })
                        }
                        type="number"
                      />
                    </td>
                  </tr>
                  <tr className="py-3">
                    <td className="text-right p-3">
                      <label className="">Date :</label>
                    </td>
                    <td className="p-3">
                      <input
                        className="bg-black focus:outline-none border w-full text-sm p-1 border-gray-300"
                        onChange={(event) =>
                          this.setState({ date: event.target.value })
                        }
                        value={this.state.date}
                        type="date"
                      />
                    </td>
                  </tr>
                </tbody>
              </table>
            </div>
            <div className="flex flex-wrap justify-center py-3 pb-6">
              <button
                onClick={this.saveTransaction}
                className="focus:outline-none px-6 py-3 border border-slate-300 hover:bg-slate-200 hover:text-black"
              >
                Save
              </button>
            </div>
          </div>
        </div>
      </React.Fragment>
    );
  }
}

import React, { Component } from "react";
import { toast } from "react-toastify";
import HomeClient from "../../Client/HomeClient";
import Button1 from "../buttons/Button1";
import ModalTransaction from "../modal/ModalTransaction";

export default class TransactionTable extends Component {
  state = {
    transactionModalVisible: false,
    transactions: null,
    backendPage: 0,
  };
  changeTransactionVisible = () => {
    this.setState({
      transactionModalVisible: !this.state.transactionModalVisible,
    });
    this.props.updateData();
    this.updateData();
  };

  deleteTransaction = (id) => {
    if (window.confirm("Delete this Transaction ?") === true) {
      HomeClient.deleteTransaction(id).then((res) => {
        if (res === "delete") {
          this.updateData();
          this.props.updateData();
          toast.success("Transaction Deleted");
        }
      });
    }
  };

  updateData() {
    HomeClient.LoadTransactionTable(this.state.backendPage).then((data) =>
      this.setState({ transactions: data })
    );
  }

  componentDidMount() {
    this.updateData();
  }

  nextPage() {
    if (this.state.transactions.pages - 1 <= this.state.backendPage) return;
    HomeClient.LoadTransactionTable(this.state.backendPage + 1).then((data) => {
      this.setState({
        transactions: data,
        backendPage: this.state.backendPage + 1,
      });
    });
  }

  prevPage() {
    if (this.state.backendPage === 0) return;

    HomeClient.LoadTransactionTable(this.state.backendPage - 1).then((data) => {
      this.setState({
        transactions: data,
        backendPage: this.state.backendPage - 1,
      });
    });
  }

  loadTable() {
    if (this.state.transactions === null) {
      return (
        <React.Fragment>
          <tr>
            <td className="px-2 py-1 border-slate-400 border"></td>
            <td className="px-2 py-1 border-slate-400 border"></td>
            <td className="px-2 py-1 border-slate-400 border"></td>
            <td className="px-2 py-1 border-slate-400 border"></td>
            <td className="px-2 py-1 border-slate-400 border"></td>
            <td className="px-2 py-1 border-slate-400 border"></td>
          </tr>
        </React.Fragment>
      );
    }
    return (
      <React.Fragment>
        {this.state.transactions.length === 0 && (
          <tr>
            <td
              colSpan={6}
              className="text-center px-2 py-1 border-slate-600 border"
            >
              Ended Go Back
            </td>
          </tr>
        )}
        {this.state.transactions.transaction.map((transaction, key) => {
          return (
            <tr key={key}>
              <td className="px-2 py-1 border-slate-600 border">
                {key + this.state.backendPage * 5 + 1}
              </td>
              <td className="px-2 py-1 border-slate-600 border">
                <p className="truncate md:w-auto w-24 ">
                  {transaction.assetName}
                </p>
              </td>
              <td className="px-2 py-1 border-slate-600 border">
                {transaction.average.toFixed(2)}
              </td>
              <td className="px-2 py-1 border-slate-600 border">
                {transaction.quantity}
              </td>
              <td className="px-2 py-1 border-slate-600 border">
                {new Date(transaction.transactionDate).toLocaleDateString(
                  "en-US"
                )}
              </td>
              <td className="px-2 py-1 border-slate-600 border">
                <Button1
                  className={"hover:text-red-600 hover:border-red-600"}
                  value={"Delete"}
                  onpress={() => this.deleteTransaction(transaction.id)}
                />
              </td>
            </tr>
          );
        })}
      </React.Fragment>
    );
  }

  render() {
    return (
      <div className="text-white p-5 w-full overflow-x-auto text-sm">
        {this.state.transactionModalVisible && (
          <ModalTransaction
            changeTransactionVisible={this.changeTransactionVisible}
          />
        )}
        <div className="pb-5 flex">
          <Button1
            value={"Add Transaction"}
            onpress={this.changeTransactionVisible}
            className={"hover:bg-slate-200 hover:text-black"}
          />
        </div>
        <table>
          <tbody>
            <tr>
              <th className="px-2 py-1 border-slate-400 border">No</th>
              <th className="px-2 py-1 border-slate-400 border">Asset Name</th>
              <th className="px-2 py-1 border-slate-400 border">Average</th>
              <th className="px-2 py-1 border-slate-400 border">Quantity</th>
              <th className="px-2 py-1 border-slate-400 border">Date</th>
              <th className="px-2 py-1 border-slate-400 border">Delete</th>
            </tr>
            {this.loadTable()}
          </tbody>
        </table>
        <div className="flex flex-wrap justify-center p-5">
          <div
            className="border p-2 px-4 border-slate-400 mx-2 hover:bg-slate-200 hover:text-black cursor-pointer"
            onClick={() => this.prevPage()}
          >
            {"<"}
          </div>
          <div
            className="border p-2 px-4 border-slate-400 mx-2 hover:bg-slate-200 hover:text-black cursor-pointer"
            onClick={() => this.nextPage()}
          >
            {">"}
          </div>
        </div>
      </div>
    );
  }
}

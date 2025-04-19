import React, { Component } from "react";
import { Navigate } from "react-router-dom";
import { ToastContainer } from "react-toastify";
import HomeClient from "../Client/HomeClient";
import LoginClient from "../Client/LoginClient";
import Button3 from "../util/buttons/Button3";
import Card from "../util/Card/Card";
import GaugeCharts from "../util/charts/GaugeCharts";
import Piechart from "../util/charts/Piechart";
import PortfolioTable from "../util/Table/PortfolioTable";
import TransactionTable from "../util/Table/TransactionTable";
import "react-toastify/dist/ReactToastify.css";
export default class Home extends Component {
  state = {
    user: null,
    portfolio: null,
  };

  componentDidMount() {
    this.loadData();
    //setInterval(() => this.loadData(), 10000);
  }

  updateData = () => {
    this.loadData();
  };

  loadData = () => {
    LoginClient.validate()
      .then((user) => this.setState({ user: user }))
      .then(() => {
        HomeClient.Portfolio().then((portfolio) =>
          this.setState({ portfolio: portfolio })
        );
      });
  };

  logout = () => {
    localStorage.removeItem("token");
    this.setState({ user: false });
  };

  render() {
    if (
      localStorage.getItem("token") === undefined ||
      this.state.user === false
    ) {
      return <Navigate to="/login" replace />;
    }
    if (this.state.user == null || this.state.portfolio == null)
      return <div className="text-3xl text-center pt-28"> Loading </div>;
    return (
      <div>
        <ToastContainer
          position="top-right"
          autoClose={5000}
          hideProgressBar={false}
          newestOnTop={false}
          closeOnClick
          rtl={false}
          pauseOnFocusLoss
          draggable
          pauseOnHover
          theme="dark"
        />
        <div className="border-b border-slate-400 flex flex-wrap justify-between">
          <h1 className="font-serif text-left font-semibold text-xl p-5">
            The Investment Club.
          </h1>
          <Button3
            onpress={this.logout}
            className="border mx-5 m-3 h-10 pt-2 p-6"
          >
            Log Out
          </Button3>
        </div>
        <div className="flex flex-wrap">
          <div className="md:w-2/4 w-full xl:w-1/4">
            <Card title={"Invested"}>
              <div className="text-center text-4xl py-10 h-40">
                {this.state.portfolio.invested.toFixed(2)} ₹
              </div>
            </Card>
          </div>
          <div className="md:w-2/4 w-full xl:w-1/4">
            <Card title={"Holding"}>
              <div className="text-center text-4xl py-10 h-40">
                {this.state.portfolio.holding.toFixed(2)} ₹
                <span className="text-green-400 text-xl">
                  {this.state.portfolio.percentageReturn.toFixed(2)} %
                </span>
                <div className="w-full text-center">
                  <div className="text-green-400 text-xl">
                    {this.state.portfolio.profit.toFixed(2)} ₹{" "}
                    {/*<span className="text-sm"> 22 % P.A.</span>*/}
                  </div>
                </div>
              </div>
            </Card>
          </div>
          <div className="md:w-2/4 w-full xl:w-1/4">
            <Card title={"Price To Earning"}>
              <GaugeCharts start={0} end={60} value={41} unit={"P/E"} />
            </Card>
          </div>
          <div className="md:w-2/4 w-full xl:w-1/4">
            <Card title={"Return on Equity"}>
              <GaugeCharts start={0} end={50} value={28} unit={"%"} />
            </Card>
          </div>
          <div className="w-full md:w-1/3">
            <Card title={"Holding"}>
              <div className="h-96">
                <Piechart data={this.state.portfolio.chartModels} />
              </div>
            </Card>
          </div>
          <div className="w-full md:w-1/3">
            <Card title={"Portfolio"}>
              <div className="h-auto">
                <PortfolioTable data={this.state.portfolio.stockList} />
              </div>
            </Card>
          </div>
          <div className="w-full md:w-1/3">
            <Card title={"Transaction History"}>
              <div className="lg:h-auto">
                <TransactionTable updateData={this.updateData} />
              </div>
            </Card>
          </div>
        </div>
      </div>
    );
  }
}

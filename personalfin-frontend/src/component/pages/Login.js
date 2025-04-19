import React, { Component } from "react";
import Button3 from "../util/buttons/Button3";
import { Navigate } from "react-router-dom";
import LoginClient from "../Client/LoginClient";
import { toast, ToastContainer } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
export default class Login extends Component {
  state = {
    user: false,
    login: "",
    password: "",
    signup: false,
  };
  setLogin = (event) => {
    this.setState({ login: event.target.value });
  };
  setPassword = (event) => {
    this.setState({ password: event.target.value });
  };
  validateEmail = (email) => {
    var re = /\S+@\S+\.\S+/;
    return re.test(email);
  };
  login = () => {
    if (!this.validateEmail(this.state.login)) {
      toast.error("Enter Valid Email.");
      return;
    }
    if (this.state.password === "") {
      toast.error("Enter Password.");
      return;
    }
    LoginClient.login(this.state.login, this.state.password)
      .then((response) => response.json())
      .then((response) => {
        console.log(response);
        if (response.statuscode === "UNAUTHORIZED") {
          toast.error(response.message);
        } else {
          localStorage.setItem("token", response.token);
          this.setState({ user: true });
        }
      });
  };
  signup = () => {
    this.setState({ signup: true });
  };

  render() {
    if (this.state.user === true) {
      return <Navigate to="/" replace />;
    }
    if (this.state.signup === true) {
      return <Navigate to="/signup" replace />;
    }
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
        <h1 className="font-serif text-center font-semibold text-4xl p-5">
          The Investment Club.
        </h1>
        <div className="flex flex-wrap justify-center pt-5">
          <div className="border-slate-300 border-2 rounded-md md:w-2/4 xl:w-1/3">
            <h1 className="text-center text-2xl font-sans font-semibold pt-5">
              Please Login.
            </h1>
            <div className="w-full flex flex-wrap justify-center">
              <table className="md:m-5 text-lg">
                <tbody>
                  <tr className="py-3">
                    <td className="text-right p-3 sm:p-5">
                      <label className="">Email : </label>
                    </td>
                    <td className="p-3 sm:p-5">
                      <input
                        className="bg-black focus:outline-none border w-full "
                        value={this.state.login}
                        onChange={this.setLogin}
                        type="email"
                      />
                    </td>
                  </tr>
                  <tr className="py-3">
                    <td className="text-right p-3 sm:p-5">
                      <label className="">Password :</label>
                    </td>
                    <td className="p-3 sm:p-5">
                      <input
                        className="bg-black focus:outline-none border w-full"
                        value={this.state.password}
                        onChange={this.setPassword}
                        type="password"
                      />
                    </td>
                  </tr>
                </tbody>
              </table>
            </div>
            <div className="flex flex-wrap justify-center m-5">
              <Button3
                className="border mx-5 mb-3 h-12 pt-3 p-6"
                onpress={this.login}
              >
                Log in
              </Button3>
              <Button3
                className="border mx-5 mb-3 h-12 pt-3 p-6"
                onpress={this.signup}
              >
                Sign Up
              </Button3>
            </div>
          </div>
        </div>
      </div>
    );
  }
}

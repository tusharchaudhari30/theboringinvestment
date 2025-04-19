class LoginClient {
  static url = "http://localhost:8080";

  static async login(username, password) {
    let requestOptions = {
      method: "GET",
      redirect: "follow",
    };

    return fetch(
      this.url + `/user/login?email=${username}&password=${password}`,
      requestOptions
    );
  }

  static async validate() {
    let myHeaders = new Headers();
    myHeaders.append(
      "Authorization",
      "Bearer " + localStorage.getItem("token")
    );

    let requestOptions = {
      method: "GET",
      headers: myHeaders,
      redirect: "follow",
    };

    return fetch(this.url + "/user", requestOptions).then(
      (response) => response.status === 200
    );
  }

  static async signup(email, password) {
    var myHeaders = new Headers();
    myHeaders.append("Content-Type", "application/json");

    var raw = JSON.stringify({
      email: email,
      password: password,
    });

    var requestOptions = {
      method: "POST",
      headers: myHeaders,
      body: raw,
      redirect: "follow",
    };

    return fetch(this.url + "/user/signup", requestOptions).then((response) =>
      response.json()
    );
  }
}

export default LoginClient;

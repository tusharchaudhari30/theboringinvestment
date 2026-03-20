class LoginClient {
  static url = "";

  static async login(username, password) {
    var myHeaders = new Headers();
    myHeaders.append("Content-Type", "application/json");

    let requestOptions = {
      method: "POST",
      headers: myHeaders,
      body: JSON.stringify({ email: username, password: password }),
      redirect: "follow",
    };

    return fetch(this.url + "/authentication/login", requestOptions);
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

    return fetch(this.url + "/authentication/validate", requestOptions).then(
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

    return fetch(this.url + "/authentication/signup", requestOptions).then(
      (response) => response.json()
    );
  }
}

export default LoginClient;

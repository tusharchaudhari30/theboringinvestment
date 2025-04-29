class HomeClient {
  static url = "";

  static async Portfolio() {
    const myHeaders = new Headers();
    myHeaders.append(
      "Authorization",
      "Bearer " + localStorage.getItem("token")
    );

    const requestOptions = {
      method: "GET",
      headers: myHeaders,
      redirect: "follow",
    };

    return fetch(this.url + "/portfolio/user/portfolio", requestOptions).then(
      (response) => response.json()
    );
  }

  static async LoadTransactionTable(page) {
    var myHeaders = new Headers();
    myHeaders.append(
      "Authorization",
      "Bearer " + localStorage.getItem("token")
    );
    const requestOptions = {
      method: "GET",
      headers: myHeaders,
      redirect: "follow",
    };

    return fetch(
      this.url + "/transaction/transaction/" + page,
      requestOptions
    ).then((response) => response.json());
  }

  static async saveTransaction(assetName, ticker, amount, average, date) {
    const myHeaders = new Headers();
    myHeaders.append(
      "Authorization",
      "Bearer " + localStorage.getItem("token")
    );
    myHeaders.append("Content-Type", "application/json");

    const raw = JSON.stringify({
      assetName: assetName,
      ticker: ticker,
      average: average,
      quantity: amount,
      transactionDate: date,
    });

    const requestOptions = {
      method: "POST",
      headers: myHeaders,
      body: raw,
      redirect: "follow",
    };

    return fetch(this.url + "/transaction/transaction", requestOptions).then(
      (response) => response.json()
    );
  }

  static async assetList(assetName) {
    const myHeaders = new Headers();
    myHeaders.append(
      "Authorization",
      "Bearer " + localStorage.getItem("token")
    );

    const requestOptions = {
      method: "GET",
      headers: myHeaders,
      redirect: "follow",
    };

    return fetch(
      this.url + "/portfolio/user/asset/search?keyword=" + assetName,
      requestOptions
    ).then((response) => response.json());
  }

  static async deleteTransaction(transactionid) {
    var myHeaders = new Headers();
    myHeaders.append(
      "Authorization",
      "Bearer " + localStorage.getItem("token")
    );

    var requestOptions = {
      method: "DELETE",
      headers: myHeaders,
      redirect: "follow",
    };

    return fetch(
      this.url + "/transaction/transaction?id=" + transactionid,
      requestOptions
    ).then((response) => response.text());
  }
}

export default HomeClient;

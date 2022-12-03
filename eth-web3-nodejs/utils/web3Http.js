const Web3HttpProvider = require("web3-providers-http");
const Web3 = require("web3");

const {
  ETH_URL_HTTP = "https://polygon-mumbai.infura.io/v3/4c5c75735dc74d4091d27d5841c06e28",
} = process.env;

const web3HttpProviderOptions = {
  keepAlive: true,
  withCredentials: false,
  timeout: 20000, // ms
  headers: [
    {
      name: "Access-Control-Allow-Origin",
      value: "*",
    },
  ],
};

const provider = new Web3HttpProvider(ETH_URL_HTTP, web3HttpProviderOptions);

const web3 = new Web3(provider);

module.exports = web3;

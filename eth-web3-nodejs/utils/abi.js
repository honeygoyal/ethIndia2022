const path = require("path");
const glob = require("glob");
const fs = require("fs");

const contractPath = path.resolve(__dirname, "../contracts/build/contracts");

const getDirectories = (src) => glob.sync(`${src}/**/*.json`);

const fileNames = getDirectories(contractPath);

const contractABI = {};

fileNames.forEach((fileName) => {
  const filePath = path.resolve(contractPath, fileName);
  const compiledCode = JSON.parse(fs.readFileSync(filePath).toString());
  contractABI[compiledCode.contractName] = compiledCode;
  // console.log(contractABI[compiledCode.contractName]);
});

module.exports = contractABI;

const web3Instance = require("./web3Http");
const abi = require("./abi");

const { PLAY_STORE_ADDRESS = "0xefd566eb671d592b4B98b93f38A25d4fCeC534Ab" } =
  process.env;

const playStoreABI = abi.PlayStore;

const playStoreContract = new web3Instance.eth.Contract(
  playStoreABI.abi,
  PLAY_STORE_ADDRESS
);

const getappToCid = async (appName) => {
  const cid = await playStoreContract.methods.appToCid(appName).call();
  return { appName, cid };
};

const getBalance = async () => {
  const balance = await playStoreContract.methods.balance().call();
  return balance;
};

const getAllApps = async () => {
  const allApps = await playStoreContract.methods.getAllApps().call();
  return allApps;
};

const getAllGames = async () => {
  const allGames = await playStoreContract.methods.getAllGames().call();
  return allGames;
};

const getAllComics = async () => {
  const allComics = await playStoreContract.methods.getAllComics().call();
  return allComics;
};

const getAppIcon = async () => {
  const appIcons = await playStoreContract.methods.AppIcon().call();
  return appIcons;
};

const isDeveloper = async (address) => {
  const result = await playStoreContract.methods.developer(address).call();
  console.log(result);
  return result;
};

// getBalance().then(console.log);
// getAllApps().then(console.log);
// getAllGames().then(console.log);
// getAllComics().then(console.log);

module.exports = {
  getAllApps,
  isDeveloper,
  getAppIcon,
  getAllComics,
  getappToCid,
  getBalance,
  getAllGames,
};

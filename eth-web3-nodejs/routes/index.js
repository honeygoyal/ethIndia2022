var express = require("express");
var router = express.Router();

const {
  getAllApps,
  getAllComics,
  isDeveloper,
  getAllGames,
} = require("../utils/playStore");

router.get("/welcome", function (req, res, next) {
  res.json("Welcome");
});

router.post("/getApps", async function (req, res, next) {
  const data = await getAllApps();

  const result = [];
  data.forEach((element) => {
    const temp = {};
    temp.name = element.name;
    temp.category = element.category;
    temp.fileCid = element.fileCid;
    temp.iconCid = element.iconCid;
    result.push(temp);
  });

  return res.json(result);
});

router.post("/getComics", async function (req, res, next) {
  const data = await getAllComics();

  const result = [];
  data.forEach((element) => {
    const temp = {};
    temp.name = element.name;
    temp.category = element.category;
    temp.fileCid = element.fileCid;
    temp.iconCid = element.iconCid;
    result.push(temp);
  });

  return res.json(result);
});

router.post("/getGames", async function (req, res, next) {
  const data = await getAllGames();
  const result = [];
  data.forEach((element) => {
    const temp = {};
    temp.name = element.name;
    temp.category = element.category;
    temp.fileCid = element.fileCid;
    temp.iconCid = element.iconCid;
    result.push(temp);
  });

  return res.json(result);
});

router.post("/isDeveloper", async function (req, res, next) {
  const { address } = req.body;
  const data = await isDeveloper(address);
  return res.json(data);
});

module.exports = router;

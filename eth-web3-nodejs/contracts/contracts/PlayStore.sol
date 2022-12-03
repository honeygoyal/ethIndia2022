// SPDX-License-Identifier: MIT
pragma solidity 0.8.2;

contract PlayStore {
    struct App {
        string name;
        string category;
        string fileCid;
        string iconCid;
    }

    mapping(string => App[]) public appTypeToApps;
    mapping(string => string) public appToCid;
    mapping(address => bool) public developer;

    uint256 public balance;
    uint256 public constant Fee = 0;

    function isDeveloper(address _address) external view returns (bool) {
        return developer[_address];
    }

    function pay() external payable returns (uint256) {
        require(msg.value >= Fee, "Amount insufficient");
        balance += msg.value;
        developer[msg.sender] = true;
        return msg.value;
    }

    function getAllApps() public view returns (App[] memory) {
        uint256 len = appTypeToApps["app"].length;
        App[] memory apps = new App[](len);

        for (uint256 i = 0; i < len; ++i) {
            App storage appStorage = appTypeToApps["app"][i];
            apps[i] = appStorage;
        }
        return apps;
    }

    function getAllGames() public view returns (App[] memory) {
        uint256 len = appTypeToApps["games"].length;
        App[] memory apps = new App[](len);

        for (uint256 i = 0; i < len; ++i) {
            App storage appStorage = appTypeToApps["games"][i];
            apps[i] = appStorage;
        }
        return apps;
    }

    function getAllComics() public view returns (App[] memory) {
        uint256 len = appTypeToApps["comics"].length;
        App[] memory apps = new App[](len);

        for (uint256 i = 0; i < len; ++i) {
            App storage appStorage = appTypeToApps["comics"][i];
            apps[i] = appStorage;
        }
        return apps;
    }

    function create(
        string calldata _category,
        string calldata _appType,
        string calldata _appName,
        string calldata _fileCid,
        string calldata _iconCid
    ) external returns (string calldata) {
        require(developer[msg.sender] == true, "Not a developer");
        appTypeToApps[_appType].push(
            App({
                name: _appName,
                category: _category,
                fileCid: _fileCid,
                iconCid: _iconCid
            })
        );
        appToCid[_appName] = _fileCid;
        return _appName;
    }
}


import './App.css';
import React, { useState,useEffect } from 'react';
import '@rainbow-me/rainbowkit/styles.css';
import '@react-pdf-viewer/core/lib/styles/index.css';
import '@react-pdf-viewer/default-layout/lib/styles/index.css';
import axios from 'axios';

import {
  getDefaultWallets,
  RainbowKitProvider,
} from '@rainbow-me/rainbowkit';
import { Button } from 'react-bootstrap';
import Container from 'react-bootstrap/Container';
import Navbar from 'react-bootstrap/Navbar';
import { ConnectButton } from '@rainbow-me/rainbowkit';
import {
  chain,
  configureChains,
  createClient,
  WagmiConfig,
  useAccount,
} from 'wagmi';
import { alchemyProvider } from 'wagmi/providers/alchemy';
import { publicProvider } from 'wagmi/providers/public';
import Background from './Background';
import Payment from './Payment';
import Welcome from './Welcome';
import AppIntro  from './AppIntro';
import CreateApp from './CreateApp';
import propsData from './prop.json'
const { chains, provider } = configureChains(
  [chain.mainnet, chain.goerli, chain.polygon, chain.optimism, chain.arbitrum,chain.polygonMumbai],
  [
    alchemyProvider({ apiKey: process.env.ALCHEMY_ID }),
    publicProvider()
  ]
);

const { connectors } = getDefaultWallets({
  appName: 'My RainbowKit App',
  chains
});

const wagmiClient = createClient({
  autoConnect: true,
  connectors,
  provider
})



function App() {
  const { address, isConnected } = useAccount();
  const [paymentStatus, setPaymentStatus] = useState(false);
  const [isDeveloper, setIsDeveloper] = useState(false);
  const [showAppForm,setShowAppForm] = useState(false);
  useEffect(() => {
    if(address !== undefined){
      axios
      .post(propsData.IS_DEVELOPER_URL, 
       {
          "address": address
        }
      )
      .then((res) => {
        console.log(res)
        setIsDeveloper(res);
        setPaymentStatus(res);
      })
      .catch(() => {
      });
    }
  }, [address])
  const showForm = () => setShowAppForm(true);
  return (
    <div>
      <Navbar bg="dark" variant="dark">
        <Container className="headerContainer">
          <Navbar.Brand href="http://localhost:3000/">
           <img
              alt=""
              src={require("./Ethereum-Logo.wine.png")}
              width="30"
              height="30"
              // className="d-inline-block align-top"
            />
            Web3 Store
          </Navbar.Brand>

          <WagmiConfig client={wagmiClient}>
            <RainbowKitProvider chains={chains}>

              <ConnectButton />

            </RainbowKitProvider>
          </WagmiConfig>
          {isDeveloper && paymentStatus && isConnected && !showAppForm && <Button variant="primary" onClick={showForm}>Create App</Button>}
        </Container>
      </Navbar>

      {!isConnected && <div className='content'>
        <Background></Background>
      </div>}
      { isConnected && !paymentStatus && <div>
        <Welcome></Welcome>
      </div>}
      {isConnected && paymentStatus && !isDeveloper && <div>
        <Payment></Payment>
      </div>}
      {isConnected && paymentStatus && isDeveloper && <div>
        <AppIntro></AppIntro>
      </div>
      }
      {showAppForm && isConnected && paymentStatus && isDeveloper && <div>
        <CreateApp></CreateApp>
      </div>}



    </div>

  );
}


export default App;

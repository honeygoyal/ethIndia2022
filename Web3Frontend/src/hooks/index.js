// hooks/index.ts

import { ethers } from "ethers";
import { useCallback, useState } from 'react'
import { usePromiseTransaction } from '@usedapp/core/dist/esm/src/hooks/usePromiseTransaction'
import { Contract } from "@ethersproject/contracts";
import { useContractCall,ContractCall } from "@usedapp/core";
import simpleContractAbi from "../abi/PlayStore.json";
import { simpleContractAddress } from "../contracts";

import { Web3Provider } from '@ethersproject/providers';
import { TransactionOptions, useEthers } from '@usedapp/core';
import { Signer } from '@ethersproject/abstract-signer';

const simpleContractInterface = new ethers.utils.Interface(simpleContractAbi);
const provider = new ethers.providers.Web3Provider(window.ethereum)
const signer = provider.getSigner();
const contract = new Contract(simpleContractAddress, simpleContractInterface,signer);
export function connectContractToSigner(contract, options, library) {
  if (contract.signer) {
    return contract
  }

  if (options?.signer) {
    return contract.connect(options.signer)
  }

  if (library?.getSigner()) {
    return contract.connect(library.getSigner())
  }

  throw new TypeError('No signer available in contract, options or library')
}
export const useContractFunction = (
  contract,
  functionName,
  options,
) => {
  const { library, chainId } = useEthers()
  const [events, setEvents] = useState(
    undefined
  )
  const [receipt,setReceipt] = useState(undefined);

  const { promiseTransaction, state } = usePromiseTransaction(chainId, options)

  const send = useCallback(
    async (...args) => {
      const contractWithSigner = connectContractToSigner(contract, options, library)
      const sendPromise = contractWithSigner[functionName](...args).then(
        (result) => {
          result.chainId = chainId
          return result
        }
      )

      const receipt = await promiseTransaction(sendPromise)
     
      if (receipt) {
        setReceipt(receipt);
        if (receipt.logs && receipt.logs.length > 0) {
          setEvents(receipt.logs.map((log) => contract.interface.parseLog(log)))
        } else {
          setEvents([])
        }
      }
    },
    [contract, functionName, chainId, promiseTransaction, library, options]
  )

  return { send, state, receipt}
}
// export function useIncrement() {
//     const { state, send, events } = useContractFunction(contract, "incrementCount", {});
//     return { state, send,events };
//   }

  export function useContractMethod(methodName) {
    const { state, send,events } = useContractFunction(contract, methodName, {});
    return { state, send ,events};
  }

  // export function useSetCount() {
  //   const { state, send } = useContractFunction(contract, "setCount", {});
  //   return { state, send };
  // }

export function useCount() {
  const call ={
    abi: simpleContractInterface,
    address: simpleContractAddress,
    method: "count",
    args: [],
  };
  const [count] = useContractCall(call) ?? [];
  return count;
}
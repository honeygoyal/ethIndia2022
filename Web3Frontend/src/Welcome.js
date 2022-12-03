import React from 'react'
import './Welcome.css';
import Form from 'react-bootstrap/Form';
import { Button } from 'react-bootstrap';
import { useContractMethod } from "./hooks";
function Welcome() {

  const { send: setPayment } =
    useContractMethod("pay");
  function handlePayment(event) {
    event.preventDefault();
    setPayment();
  }
  return (
    <>
      <div className='form'>
        <Form>
          <Form.Group className="mb-4" controlId="formBasicEmail">
            <Form.Label className='labelTitle'>Developer Name</Form.Label>
            <Form.Control type="text" placeholder="Enter Developer Name" />
          </Form.Group>                                                    
          <Form.Group className="mb-3" controlId="formBasicCheckbox">
            <Form.Check type="checkbox" label="Accept Terms and Conditions" />
          </Form.Group>
          <Button onClick={handlePayment} variant="primary" type="submit">
            Create and Pay
          </Button>
        </Form>
      </div>
    </>

  )
}
export default Welcome;
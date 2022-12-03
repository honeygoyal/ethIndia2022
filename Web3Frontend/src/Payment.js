import React, { Component } from 'react'
import './Payment.css';
import Button from 'react-bootstrap/Button';
import Card from 'react-bootstrap/Card';
function Payment() {
  return (
    <>
      <div className="paymentModal">
        <Card style={{ width: '18rem' }}>

          <Card.Body>
            <Card.Title>Payment Successfull</Card.Title>
            <Card.Text>
              Payment is Successfull. Please go to Dashboard to see the apps.
            </Card.Text>
            <Button variant="primary">Go To Dashboard</Button>
          </Card.Body>
        </Card>
      </div>
    </>
  )
}
export default Payment;
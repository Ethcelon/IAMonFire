import React, { Component } from 'react';
import { Grid, Alert, Col, Row, Button, PageHeader, Image } from 'react-bootstrap';

import './BidForm.css';

 export default class BidForm extends Component {

  render() {

    return(
       <Row>
         <Col md={12} xs={12}>
           <span className="AmountOffer">$1000</span>
           <Button bsStyle="danger" bsSize="large" className="ButtonOffer">Bid!</Button>
         </Col>
       </Row>
    );
  }
}
import React, { Component } from 'react';
import { Col, Row, Button, Form, FormGroup, ControlLabel, FormControl, InputGroup } from 'react-bootstrap';

import './BidForm.css';

function isNormalInteger(str) {
    var n = Math.floor(Number(str));
    return n !== Infinity && String(n) === str && n >= 0;
}

export default class BidForm extends Component {

  constructor(props, context) {
    super(props, context);

    this.handleChange = this.handleChange.bind(this);

    this.state = {
      value: ''
    };
  }

  getValidationState() {
    if (isNormalInteger(this.state.value) && Math.floor(Number(this.state.value)) >= this.props.minAmount) return 'success';
    else return 'error';
  }

  handleChange(e) {
    this.setState({ value: e.target.value });
  }

  render() {

    return(
      <Row>
        <Col md={12} xs={12}>
          <Form>
            <FormGroup controlId="formBasicText" validationState={this.getValidationState()}>

              <InputGroup>
                <InputGroup.Addon>$</InputGroup.Addon>
                <FormControl bsSize="large" type="text" value={this.state.value} placeholder="Enter amount" onChange={this.handleChange}
                />
                <InputGroup.Addon>.00</InputGroup.Addon>
              </InputGroup>
              <FormControl.Feedback />
            </FormGroup>
            <Button bsStyle="danger" bsSize="large" className="ButtonOffer">Bid!</Button>
          </Form>
        </Col>
      </Row>
    );
  }
}
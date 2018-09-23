import React, { Component } from 'react';
import { Grid, Col, Row, Form, FormGroup, FormControl, Button, ControlLabel } from 'react-bootstrap';

import './Login.css';

export default class Login extends Component {
  render() {
    return (
      <Grid className="FireLogin" fluid={true}>
          <Row>
              <Col mdOffset={4} md={4}>
                <Form method="POST" action="https://localhost:80/login" horizontal>
                  <FormGroup controlId="formHorizontalEmail">
                    <Col componentClass={ControlLabel} sm={2}>
                      Email
                    </Col>
                    <Col sm={10}>
                      <FormControl type="email" placeholder="Email" />
                    </Col>
                  </FormGroup>

                  <FormGroup controlId="formHorizontalPassword">
                    <Col componentClass={ControlLabel} sm={2}>
                      Password
                    </Col>
                    <Col sm={10}>
                      <FormControl type="password" placeholder="Password" />
                    </Col>
                  </FormGroup>

                  <FormGroup>
                    <Col smOffset={2} sm={10}>
                      <Button type="submit">Sign in</Button>
                    </Col>
                  </FormGroup>
                </Form>
              </Col>
          </Row>
      </Grid>
    );
  }
}
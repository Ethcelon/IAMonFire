import React, { Component } from 'react';
import { Col, Row, Image } from 'react-bootstrap';

import './Description.css';

export default class Description extends Component {

  constructor(props, context) {
    super(props, context);
  }

  render() {

    return(
      <Row>
        <Col xs={12} md={12} className="AuctionDescription">
          <p> Lorem ipsum dolor sit amet, consectetur adipiscing elit. Ut a neque pellentesque, accumsan eros in, malesuada arcu. Duis scelerisque porttitor justo. Sed vitae erat id mi interdum finibus sit amet nec eros. Sed dapibus, ligula id molestie tincidunt, lorem sapien tristique dui, vel ultricies massa turpis et diam. Sed porta accumsan turpis. Suspendisse non euismod risus. Suspendisse potenti. Aliquam pulvinar, sapien a pellentesque dignissim, diam tellus fringilla nisl, mollis pellentesque enim lorem sed sapien. Vestibulum ut volutpat enim. Pellentesque a ex eu ex rutrum blandit. Pellentesque vel lacinia justo. Mauris vulputate justo quis congue scelerisque. In hac habitasse platea dictumst.
           </p>
        </Col>
      </Row>
    );
  }
}
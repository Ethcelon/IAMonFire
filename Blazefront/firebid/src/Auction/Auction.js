import React, { Component } from 'react';
import { Grid, Alert, Col, Row, Button, PageHeader, Image } from 'react-bootstrap';

import './Auction.css';

import BidList from './BidList/BidList.js';

export default class Auction extends Component {

  constructor(props) {
    super(props);

    this.state = {
      data: this.props.data
    }
  }

  render() {
    return (
      <Grid className="FireAuction" fluid={true}>

        <Row className="AuctionTitle">
          <Col md={12} xs={12}>
            <div>
              <span className="AuctionHeading">Crown Jwell</span><span className="AmountHeading">$123</span>
            </div>
          </Col>
        </Row>

        <Row className="AuctionBody">
          <Col xs={9} md={9} className="AuctionBodyLeft">
            <Row>
              <Col xs={12} md={12} className="AuctionPhotobox">
                <Image src="https://via.placeholder.com/1024x400" rounded />
              </Col>
              <Col xs={12} md={12} className="AuctionDescription">
                <p> Lorem ipsum dolor sit amet, consectetur adipiscing elit. Ut a neque pellentesque, accumsan eros in, malesuada arcu. Duis scelerisque porttitor justo. Sed vitae erat id mi interdum finibus sit amet nec eros. Sed dapibus, ligula id molestie tincidunt, lorem sapien tristique dui, vel ultricies massa turpis et diam. Sed porta accumsan turpis. Suspendisse non euismod risus. Suspendisse potenti. Aliquam pulvinar, sapien a pellentesque dignissim, diam tellus fringilla nisl, mollis pellentesque enim lorem sed sapien. Vestibulum ut volutpat enim. Pellentesque a ex eu ex rutrum blandit. Pellentesque vel lacinia justo. Mauris vulputate justo quis congue scelerisque. In hac habitasse platea dictumst.
                 </p>
              </Col>
            </Row>
          </Col>
          <Col xs={3} md={3}  className="AuctionBodyRight">
            <Row>
              <BidList data={[]} />
              <Col md={12} xs={12}>
                <span className="AmountOffer">$1000</span>

                <Button bsStyle="success" bsSize="large" className="ButtonOffer">Success</Button>
              </Col>
            </Row>
          </Col>

        </Row>
      </Grid>
    );
  }
}
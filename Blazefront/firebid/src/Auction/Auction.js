import React, { Component } from 'react';
import { Grid, Col, Row, Image } from 'react-bootstrap';

import './Auction.css';

import BidList from './BidList/BidList.js';
import BidForm from './BidForm/BidForm.js';
import Photobox from './Photobox/Photobox.jsx';
import Description from './Description/Description.jsx';

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
              <span className="AuctionHeading">Crown Jewels</span><span className="AmountHeading">$123</span>
            </div>
          </Col>
        </Row>

        <Row className="AuctionBody">
          <Col xs={9} md={9} className="AuctionBodyLeft">
            <Photobox images={["https://hrp.imgix.net/https%3A%2F%2Fhistoricroyalpalaces.picturepark.com%2FGo%2F4ANZcEQV%2FV%2F14181%2F29?s=22351631084c71204fa4cc99ed917b0f","https://i.ytimg.com/vi/S8k8zYNd8PU/maxresdefault.jpg","https://i2.wp.com/www.discoverlondontours.com/wp-content/uploads/2016/09/Coronation_Regalia.jpg","https://hrp.imgix.net/https%3A%2F%2Fhistoricroyalpalaces.picturepark.com%2FGo%2FcUBaRgpT%2FV%2F13662%2F29?s=79a9f8f0da4e2dd8c01f7b4997a8bced"]}/>
            <Description />
          </Col>
          <Col xs={3} md={3}  className="AuctionBodyRight">
            <BidList data={[]} />
            <BidForm minAmount={123} />
          </Col>

        </Row>
      </Grid>
    );
  }
}
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
            <Photobox images={["https://assets.pokemon.com/assets/cms2/img/watch-pokemon-tv/seasons/season21/season21_ep04_ss01.jpg","https://i.ytimg.com/vi/NiEABxi8G4U/maxresdefault.jpg","https://media.comicbook.com/2018/09/legendary-lets-go-1134651-640x320.jpeg","https://via.placeholder.com/1024x576"]}/>
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
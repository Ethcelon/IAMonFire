import React, { Component } from 'react';
import { Grid, Alert, Col, Row, Button, PageHeader, Image } from 'react-bootstrap';

import './BidList.css';

class Bid extends Component {

  render() {
    return(
     <Col md={12} xs={12}>
        <div className="BidItem">
          <span className="BidUserPicture">
            <Image src="https://via.placeholder.com/50x50" />
          </span>
          <span className="BidUserName">
            {this.props.user}
          </span>
          <span className="BidAmount">
            {this.props.amount}
          </span>
        </div>
     </Col>
    )
  }
}

 export default class BidList extends Component {
  constructor(props) {
    super(props);

    var data = []

    data.push({
      'user' : 'Alexander',
      'amount' : '2000'
    })

    data.push({
      'user' : 'Thanos',
      'amount' : '300'
    })

    data.push({
      'user' : 'Bro',
      'amount' : '3.50'
    })

    this.state = {
      data: data
    }
  }

  render() {

    var genListData = this.state.data.map((item, key) => <Bid user={item.user} amount={item.amount} key={key} /> );

    return(
      <Row className="BidList">
        {genListData}
      </Row>
    );
  }
}
import React, { Component } from 'react';
import { Col, Row, Image } from 'react-bootstrap';

import './Photobox.css';

class Thumbnail extends Component {
  constructor(props, context) {
    super(props, context);
    this.handle = this.handle.bind(this);
  }

  handle(){
    console.log(this.props.num);
  }

  render() {
    return(
      <Col xs={12} cd={12} className="AuctionThumbnail">
        <Image src={this.props.image} onClick={this.props.clickHandler} rounded />
      </Col>
    )
  }
}

export default class Photobox extends Component {

  constructor(props, context) {
    super(props, context);

    this.state = {selectedImage: this.props.images[0]};

    this.handleChildClick=this.handleChildClick.bind(this);
  }

  handleChildClick(num) {
    console.log(num);
    this.setState({selectedImage: this.props.images[num]});
    console.log(this.state.selectedImage);
  }

  render() {

    var thumbs = this.props.images.map((image, key) => <Thumbnail image={image} clickHandler={this.handleChildClick.bind(null, key)} key={key} num={key} /> );

    return(
      <Row>
        <Col xs={1} md={1} className="ThumbnailHolder">
          <Row>
            {thumbs}
          </Row>
        </Col>
        <Col xs={5} md={5} className="AuctionPhotobox">
          <Image className="AuctionMainPhoto" src={this.state.selectedImage} rounded />
        </Col>
      </Row>
    );
  }
}
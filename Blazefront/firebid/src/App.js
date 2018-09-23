import React, { Component } from 'react';
import { BrowserRouter as Router, Route, Link } from "react-router-dom";
import { Navbar, Nav, NavItem, NavDropdown, MenuItem } from 'react-bootstrap';

import logo from './NotFireBidLogo.png';

import Auction from './Auction/Auction.js'
import Login from './Login/Login.jsx'
import './App.css';

class Home extends Component {

  constructor(props) {
    super(props);

    this.state = {
      data: {}
    }
  }

  componentDidMount() {
    fetch(`https://localhost:80/`)
    .then(result=>result.json())
    .then(items=>console.log(items))
  }

  render() {
    return (
      <div className="App">
        hi
      </div>
    );
  }
}

const flameRouter = () => (
  <Router>
    <div>
      <Navbar inverse collapseOnSelect fixedTop className="flameNavBar">
        <Navbar.Header>
          <Navbar.Brand>
            <img src={logo} className="App-logo" alt="logo" />
          </Navbar.Brand>
          <Navbar.Toggle />
        </Navbar.Header>
        <Navbar.Collapse>
          <Nav>
            <NavItem href="/">
              Home
            </NavItem>
            <NavItem href="/auction">
              Auction
            </NavItem>
          </Nav>
          <Nav pullRight>
            <NavItem href="/login">
              Login
            </NavItem>
          </Nav>
        </Navbar.Collapse>
      </Navbar>

      <Route exact path="/" component={Home} />
      <Route path="/auction" component={Auction} />
      <Route path="/login" component={Login} />
    </div>
  </Router>
);

export default flameRouter;

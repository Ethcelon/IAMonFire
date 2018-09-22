import React, { Component } from 'react';
import { BrowserRouter as Router, Route, Link } from "react-router-dom";
import { Navbar, Nav, NavItem, NavDropdown, MenuItem } from 'react-bootstrap';

import logo from './NotFireBidLogo.png';

import Auction from './Auction/Auction.js'
import Login from './Login/Login.jsx'
import './App.css';

class App extends Component {
  render() {
    return (
      <div className="App">
        <header className="App-header">
          <img src={logo} className="App-logo" alt="logo" />
          <h1 className="App-title">Welcome to FireBid</h1>
        </header>
        <Auction />
      </div>
    );
  }
}

const Home = () => (
  <div>
    <h2>Home</h2>
  </div>
);

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

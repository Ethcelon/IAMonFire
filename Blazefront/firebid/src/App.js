import React, { Component } from 'react';
import logo from './NotFireBidLogo.png';

import Auction from './Auction/Auction.js'
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

export default App;

import React, { Component } from 'react';

export default class Home extends Component {
    
  
    render() {
      return (
        <center><div style={{backgroundImage: "url(https://i.pinimg.com/originals/8d/23/06/8d2306b98839234e49ce96a8b76e20ae.jpg)", borderRadius: '5px',}}><img width="900px" src="https://cdn.shopify.com/s/files/1/0405/7065/0788/files/logo_welcome.svg_72f96623-3169-48db-b78e-27f4ebc1b6db.png?v=1615208283"></img>
        <h1>a InfoBooks,</h1><br></br><h2>tu web preferida de intercambio y compra venta de libros.</h2><br></br>
        <br></br><a href="/books/all/0" class="btn btn-primary">Empezar a buscar libros</a><br></br><br></br></div></center>
      )
    }
  }
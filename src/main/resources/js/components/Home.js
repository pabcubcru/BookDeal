import React, { Component } from 'react';

export default class Home extends Component {
    
  
    render() {
      return (
        <center><div style={{borderRadius: '5px', backgroundImage: "url(https://i.pinimg.com/originals/8d/23/06/8d2306b98839234e49ce96a8b76e20ae.jpg)", backgroundSize: "cover" }}>
        <img width="400px" height="400px" src="https://i.ibb.co/KN8gvG1/logo-size-removebg-preview.png"></img>
        <h1 style={{fontFamily:"cursive", fontStyle:"italic"}}><b>Tu web preferida de intercambio y compra venta de libros.</b></h1><br></br><br></br>
        <a href="/books/all/0" class="btn btn-primary" ><b>Empezar a buscar libros</b></a><br></br><br></br></div></center>
      )
    }
  }
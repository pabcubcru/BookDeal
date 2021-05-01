import React, { Component } from 'react';

export default class Home extends Component {
    
  
    render() {
      return (
        <center><div style={{borderRadius: '5px'}}><br></br>
        <img width="300px" height="200px" src="https://uses0-my.sharepoint.com/personal/pabcubcru_alum_us_es/Documents/TFG/Logo3.png"></img>
        <br></br><br></br><br></br><br></br><h1 style={{fontFamily:"cursive", fontStyle:"italic"}}><b>Tu web preferida de intercambio y compra venta de libros.</b></h1><br></br>
        <br></br><a href="/books/all/0" class="btn btn-primary" style={{backgroundImage:"url(https://i.pinimg.com/originals/8d/23/06/8d2306b98839234e49ce96a8b76e20ae.jpg)", color:"black"}} >
          <b>Empezar a buscar libros</b></a><br></br><br></br></div></center>
      )
    }
  }
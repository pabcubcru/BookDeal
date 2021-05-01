import React, { Component } from 'react';

export default class Home extends Component {
    
  
    render() {
      return (
        <center><div style={{borderRadius: '5px', backgroundImage: "url(https://i.pinimg.com/originals/8d/23/06/8d2306b98839234e49ce96a8b76e20ae.jpg)", backgroundSize: "cover" }}><br></br>
        <img width="300px" height="200px" src="https://westeurope1-mediap.svc.ms/transform/thumbnail?provider=spo&inputFormat=png&cs=fFNQTw&docid=https%3A%2F%2Fuses0-my.sharepoint.com%3A443%2F_api%2Fv2.0%2Fdrives%2Fb!CN2_VP_HrEOzJespx9kDC8xGJ-S624RBsIAILHmSeVEEVv79aZeDQYIx6hF1M-kS%2Fitems%2F01EFGPKYTZHUC3X3FUOZFKVAH2UTCBJWEK%3Fversion%3DPublished&encodeFailures=1&ctag=%22c%3A%7BBB053D79-B4EC-4A76-AA80-FAA4C414D88A%7D%2C2%22&srcWidth=&srcHeight=&width=118&height=84&action=Access"></img>
        <br></br><br></br><br></br><br></br><h1 style={{fontFamily:"cursive", fontStyle:"italic"}}><b>Tu web preferida de intercambio y compra venta de libros.</b></h1><br></br>
        <br></br><a href="/books/all/0" class="btn btn-primary" ><b>Empezar a buscar libros</b></a><br></br><br></br></div></center>
      )
    }
  }
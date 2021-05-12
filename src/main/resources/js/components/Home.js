import React, { Component } from "react";

export default class Home extends Component {
  render() {
    return (
      <center>
        <div
          style={{
            borderRadius: "5px",
            backgroundImage: "url(https://i.ibb.co/YRy9kHC/paper.jpg)",
            backgroundSize: "cover",
          }}
        >
          <img
            width="400px"
            height="400px"
            src="https://i.ibb.co/vXSqB6W/logo.png"
          ></img>
          <h1 style={{ fontFamily: "cursive", fontStyle: "italic" }}>
            <b>Tu web preferida de intercambio y compra venta de libros</b>
          </h1>
          <br></br>
          <br></br>
          <a href="/books/all/0" class="btn btn-primary">
            <b>Empezar a buscar libros</b>
          </a>
          <br></br>
          <br></br>
        </div>
      </center>
    );
  }
}

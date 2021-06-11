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
            width="60%"
            height="60%"
            src="https://i.ibb.co/s2czT4D/logo-transparent.png"
          ></img>
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

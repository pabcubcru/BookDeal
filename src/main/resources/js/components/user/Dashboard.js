import React, { Component } from "react";
import userService from "../services/User";
import "./Dashboard.css";

export default class Form extends Component {
  constructor() {
    super();
    this.state = {
      numBooks: 0,
      numRequests: 0,
      numUsers: 0,
      numFavourites: 0,
      numSearchs: 0,
    };
  }

  async componentDidMount() {
      const res = await userService.getDashBoard();
      this.setState({numBooks: res.numBooks, numRequests: res.numRequests, numUsers: res.numUsers, 
    numFavourites: res.numFavourites, numSearchs: res.numSearchs})
  }

  render() {
    return (
      <div
        style={{
          backgroundImage: "url(https://i.ibb.co/YRy9kHC/paper.jpg)",
          backgroundSize: "cover",
          padding: "50px",
          borderRadius: "5px",
          fontWeight: "bold",
          marginLeft: "-100",
        }}
      >
        <h1 style={{ color: "#007bff" }}>Panel de control</h1>
        <br></br>
        <table>
          <tr>
            <th>Documento</th>
            <th>Cantidad actual</th>
          </tr>
          <tr>
            <td>Usuarios</td>
            <td>{this.state.numUsers}</td>
          </tr>
          <tr>
            <td>Libros</td>
            <td>{this.state.numBooks}</td>
          </tr>
          <tr>
            <td>Peticiones</td>
            <td>{this.state.numRequests}</td>
          </tr>
          <tr>
            <td>Libros favoritos</td>
            <td>{this.state.numFavourites}</td>
          </tr>
          <tr>
            <td>Búsquedas</td>
            <td>{this.state.numSearchs}*</td>
          </tr>
        </table>
        <br></br>
        <p>*Sólo se registra la última búsqueda de cada usuario.</p>
      </div>
    );
  }
}

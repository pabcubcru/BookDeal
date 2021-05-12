import React, { Component } from "react";
import bookService from "../services/Book";
import "../ListBooks.css";

export default class List extends Component {
  constructor() {
    super();
    this.state = {
      books: [],
      pages: [],
      actualPage: 0,
      numTotalPages: 0,
      images: [],
    };
  }

  async componentDidMount() {
    const page = this.props.match.params.page;
    const res = await bookService.listMyBooks(page);

    this.setState({
      books: res.books,
      pages: res.pages,
      actualPage: parseInt(page),
      numTotalPages: parseInt(res.numTotalPages),
      images: res.urlImages,
    });
  }

  render() {
    return (
      <div>
        <h1 style={{ float: "left", color: "black" }}>
          <b>Mis libros</b>
        </h1>
        <br></br>
        {this.state.books.length == 0 ? (
          <div>
            <br></br>
            <br></br>
            <p>
              <b>
                ¿Todavía no has añadido ningún libro?{" "}
                <a class="btn btn-primary" href="/books/new">
                  ¡Añade uno!
                </a>
              </b>
            </p>
            <br></br>
            <br></br>
            <br></br>
            <br></br>
            <br></br>
            <br></br>
            <br></br>
            <br></br>
            <br></br>
            <br></br>
            <br></br>
            <br></br>
            <br></br>
            <br></br>
            <br></br>
            <br></br>
            <br></br>
          </div>
        ) : (
          <center>
            {this.state.books.length != 0 && this.state.pages.length > 1 ? (
              <center>
                <br></br>
                <br></br>
                {this.state.actualPage != 0 ? (
                  <span>
                    <a class="btn btn-primary" href={"/books/me/0"}>
                      {String("<<")}
                    </a>
                    <a
                      style={{ margin: "5px" }}
                      class="btn btn-primary"
                      href={"/books/me/" + parseInt(this.state.actualPage - 1)}
                    >
                      {String("<")}
                    </a>
                  </span>
                ) : (
                  <p></p>
                )}
                {this.state.pages.map((page) => {
                  return (
                    <a
                      style={{
                        color:
                          this.state.actualPage == page ? "white" : "black",
                        backgroundColor:
                          this.state.actualPage == page ? "#007bff" : "",
                      }}
                      class="pag"
                      href={"/books/me/" + page}
                    >
                      {page}
                    </a>
                  );
                })}
                {this.state.actualPage != this.state.numTotalPages - 1 ? (
                  <span>
                    <a
                      style={{ margin: "5px" }}
                      class="btn btn-primary"
                      href={"/books/me/" + parseInt(this.state.actualPage + 1)}
                    >
                      {String(">")}
                    </a>
                    <a
                      class="btn btn-primary"
                      href={
                        "/books/me/" + parseInt(this.state.numTotalPages - 1)
                      }
                    >
                      {String(">>")}
                    </a>
                  </span>
                ) : (
                  <p></p>
                )}
              </center>
            ) : (
              <p></p>
            )}
            <br></br>
          </center>
        )}
        {this.state.books.map((book, i) => {
          return (
            <main class="mainBooks">
              <div class="book-card">
                <div class="book-card__cover">
                  <div class="book-card__book">
                    <div class="book-card__book-front">
                      <a style={{ zIndex: "-10" }} href={"/books/" + book.id}>
                        <img
                          class="book-card__img"
                          src={this.state.images[i]}
                        />
                      </a>
                    </div>
                    <div class="book-card__book-back"></div>
                    <div class="book-card__book-side"></div>
                  </div>
                </div>
                <div>
                  <div class="book-card__title">{book.title}</div>
                  <div class="book-card__author">{book.author}</div>
                  <div class="book-card__author">
                    <span>{book.price} €</span>
                  </div>
                </div>
              </div>
            </main>
          );
        })}
        {this.state.books.length != 0 && this.state.pages.length > 1 ? (
          <center>
            {this.state.actualPage != 0 ? (
              <span>
                <a class="btn btn-primary" href={"/books/me/0"}>
                  {String("<<")}
                </a>
                <a
                  class="btn btn-primary"
                  style={{ margin: "5px" }}
                  href={"/books/me/" + parseInt(this.state.actualPage - 1)}
                >
                  {String("<")}
                </a>
              </span>
            ) : (
              <p></p>
            )}
            {this.state.pages.map((page) => {
              return (
                <a
                  style={{
                    color: this.state.actualPage == page ? "white" : "black",
                    backgroundColor:
                      this.state.actualPage == page ? "#007bff" : "",
                  }}
                  class="pag"
                  href={"/books/me/" + page}
                >
                  {page}
                </a>
              );
            })}
            {this.state.actualPage != this.state.numTotalPages - 1 ? (
              <span>
                <a
                  style={{ margin: "5px" }}
                  class="btn btn-primary"
                  href={"/books/me/" + parseInt(this.state.actualPage + 1)}
                >
                  {String(">")}
                </a>
                <a
                  class="btn btn-primary"
                  href={"/books/me/" + parseInt(this.state.numTotalPages - 1)}
                >
                  {String(">>")}
                </a>
              </span>
            ) : (
              <p></p>
            )}
            <br></br>
            <br></br>
          </center>
        ) : (
          <p></p>
        )}
      </div>
    );
  }
}

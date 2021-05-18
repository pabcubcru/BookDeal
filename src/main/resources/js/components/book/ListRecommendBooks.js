import React, { Component } from "react";
import bookService from "../services/Book";
import userFavouriteBook from "../services/UserFavouriteBook";
import "../ListBooks.css";

export default class List extends Component {
  constructor() {
    super();
    this.state = {
      books: [],
      pages: [],
      actualPage: 0,
      numTotalPages: 0,
      isAdded: [],
      images: [],
    };
  }

  async componentDidMount() {
    const page = this.props.match.params.page;
    const res = await bookService.recommendBooks(page);

    this.setState({
      books: res.books,
      pages: res.pages,
      actualPage: parseInt(page),
      numTotalPages: parseInt(res.numTotalPages),
      isAdded: res.isAdded,
      images: res.urlImages,
    });
  }

  render() {
    return (
      <div>
        <h1 style={{ float: "left", color: "black" }}>
          <b>Libros recomendados para usted</b>
        </h1>
        <br></br>
        <br></br>
        <br></br>
        {this.state.books.length == 0 ? (
          <div>
            <p>
              <b>
                No se encuentra ningún libro para recomendarle. Añada más
                géneros preferidos a su perfil. O añada libros a favoritos.{" "}
                <a class="btn btn-primary" href="/profile">
                  Ir a perfil
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
            <br></br>
            <br></br>
          </div>
        ) : (
          <center>
            <p style={{ textAlign: "justify" }}>
              <b>
                Estos resultados se muestran según sus gustos, es decir, según
                sus libros subidos, sus libros favoritos y sus géneros
                preferidos. Mientras más libros y géneros preferidos añada, los
                resultados serán mas precisos.
              </b>
            </p>

            {this.state.books.length != 0 && this.state.pages.length > 1 ? (
              <center>
                {this.state.actualPage != 0 ? (
                  <span>
                    <a class="btn btn-primary" href={"/books/recommend/0"}>
                      {String("<<")}
                    </a>
                    <a
                      style={{ margin: "5px" }}
                      class="btn btn-primary"
                      href={
                        "/books/recommend/" +
                        parseInt(this.state.actualPage - 1)
                      }
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
                      href={"/books/recommend/" + page}
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
                      href={
                        "/books/recommend/" +
                        parseInt(this.state.actualPage + 1)
                      }
                    >
                      {String(">")}
                    </a>
                    <a
                      class="btn btn-primary"
                      href={
                        "/books/recommend/" +
                        parseInt(this.state.numTotalPages - 1)
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
                  <div class="book-card__title">
                    {book.title}
                    {this.state.isAdded[i] == false ? (
                      <a
                        onClick={() =>
                          this.addFavouriteBook(book.id, this.state.actualPage)
                        }
                        style={{ float: "right" }}
                      >
                        <img
                          style={{ height: "25px", width: "25px" }}
                          src="https://i.ibb.co/WktpFGx/No-Favorito.png"
                        ></img>
                      </a>
                    ) : (
                      <a
                        onClick={() => {
                          this.deleteFavouriteBook(
                            book.id,
                            book.title,
                            this.state.actualPage
                          );
                        }}
                        style={{ float: "right" }}
                      >
                        <img
                          style={{ height: "25px", width: "25px" }}
                          src="https://i.ibb.co/xXKJXKS/Favorito.png"
                        ></img>
                      </a>
                    )}
                  </div>
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
                <a class="btn btn-primary" href={"/books/recommend/0"}>
                  {String("<<")}
                </a>
                <a
                  class="btn btn-primary"
                  style={{ margin: "5px" }}
                  href={
                    "/books/recommend/" + parseInt(this.state.actualPage - 1)
                  }
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
                  href={"/books/recommend/" + page}
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
                  href={
                    "/books/recommend/" + parseInt(this.state.actualPage + 1)
                  }
                >
                  {String(">")}
                </a>
                <a
                  class="btn btn-primary"
                  href={
                    "/books/recommend/" + parseInt(this.state.numTotalPages - 1)
                  }
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

  async addFavouriteBook(id, actualPage) {
    const res = await userFavouriteBook.addFavouriteBook(id);
    window.location.replace("/books/recommend/" + actualPage);
  }

  async deleteFavouriteBook(id, title, actualPage) {
    const conf = confirm(
      "¿Está seguro de que quiere eliminar '" + title + "' de favoritos?"
    );
    if (conf) {
      const res = await userFavouriteBook.deleteFavouriteBook(id);
      window.location.replace("/books/recommend/" + actualPage);
    }
  }
}

import React, { Component } from "react";
import bookService from "../services/Book";
import userService from "../services/User";
import userFavouriteBook from "../services/UserFavouriteBook";
import "./Get.css";
import "./Images.css";

export default class Get extends Component {
  constructor() {
    super();
    this.state = {
      book: "",
      username: "",
      genres: "",
      isAdded: "",
      alreadyRequest: "",
      hasRequestAccepted: "",
      images: [],
      urlImages: [],
      hasMoreImages: "",
    };
  }

  async componentDidMount() {
    const id = this.props.match.params.id;
    const b = await bookService.getBook(id);

    const username = await userService.getUsername();

    this.setState({
      book: b.book,
      username: username.username,
      genres: b.book.genres,
      isAdded: b.isAdded,
      alreadyRequest: b.alreadyRequest,
      hasRequestAccepted: b.hasRequestAccepted,
      images: b.images,
      urlImages: b.urlImages,
      hasMoreImages: b.hasMoreImages,
    });
  }

  render() {
    return (
      <div>
        <div class="cover">
          <div class="book">
            <label for="page-1" class="book__page book__page--1">
              <img src={this.state.urlImages[0]} />
            </label>

            <label for="page-2" class="book__page book__page--4">
              <img
                src={
                  this.state.urlImages[1] == null
                    ? this.state.urlImages[0]
                    : this.state.urlImages[1]
                }
              />
            </label>

            <input type="radio" name="page" id="page-1" />

            <input type="radio" name="page" id="page-2" />
            <label class="book__page book__page--2">
              <div class="book__page-front">
                <div class="page__content">
                  <h1 class="page__content-book-title">
                    {this.state.book.title}
                  </h1>
                  <h2 class="page__content-author">{this.state.book.author}</h2>

                  {this.state.book.originalTitle != "" &&
                  this.state.book.originalTitle != this.state.book.title ? (
                    <p class="page__content-credits">
                      Título original
                      <span>{this.state.book.originalTitle}</span>
                    </p>
                  ) : (
                    <p></p>
                  )}
                  <p class="page__content-credits">
                    Editorial
                    <span>{this.state.book.publisher}</span>
                  </p>

                  <p class="page__content-credits">
                    Géneros
                    <span>
                      {this.state.genres
                        .replaceAll("_", " ")
                        .replaceAll(",", ", ")}
                    </span>
                  </p>

                  <p class="page__content-credits">
                    Año de publicación
                    <span>{this.state.book.publicationYear}</span>
                  </p>

                  <p class="page__content-credits">
                    ISBN
                    <span>{this.state.book.isbn}</span>
                  </p>

                  <p class="page__content-credits">
                    Precio
                    <span>{this.state.book.price} €</span>
                  </p>

                  <p class="page__content-credits">
                    Estado
                    <span>{this.state.book.status}</span>
                  </p>

                  <p class="page__content-credits">
                    Publicado por
                    <span>{this.state.book.username}</span>
                  </p>

                  <div class="page__content-copyright">   
                    <i
                      class="fa fa-arrow-circle-right fa-2x"
                      style={{ float: "right" }}
                    ></i>
                    <p style={{ float: "right" }}>Ver sinopsis .</p>
                  </div>
                </div>
              </div>
              <div class="book__page-back">
                <div class="page__content">
                  <h1 class="page__content-title">Sinopsis</h1>
                  <div class="page__content-blockquote">
                    <p class="page__content-blockquote-text">
                      {this.state.book.description}
                    </p>
                  </div>

                  <div class="page__number">2</div>
                </div>
              </div>
            </label>
          </div>
        </div>
        <center>
          <br></br>
          <br></br>
          <h2>
            <b>Todas las imágenes</b>
          </h2>
          <div class="allImages">
            {this.state.images.map((image, i) => {
              return (
                <center>
                  <ul class="galeria">
                    <li>
                      <a href={String("#img" + i)}>
                        <img src={image.urlImage} />
                      </a>
                    </li>
                  </ul>
                  <div class="modal" id={String("img" + i)}>
                    <br></br>
                    <br></br>
                    <br></br>
                    <div class="imagen">
                      <a
                        href={
                          i <= 0
                            ? "#img" + String(this.state.images.length - 1)
                            : "#img" + String(i - 1)
                        }
                      >
                        {String("<")}
                      </a>
                      <a href={"#img" + i}>
                        <img src={image.urlImage} />
                      </a>
                      <a
                        href={
                          i >= this.state.images.length - 1
                            ? "#img0"
                            : "#img" + String(i + 1)
                        }
                      >
                        {String(">")}
                      </a>
                    </div>
                    <a class="cerrar" href="">
                      x
                    </a>
                  </div>
                </center>
              );
            })}
          </div>
          <hr></hr>
        </center>

        <center>
          {this.state.username == this.state.book.username ? (
            <center>
              <br></br>
              <button
                style={{
                  backgroundImage: "url(https://i.ibb.co/YRy9kHC/paper.jpg)",
                  margin: "10px",
                  color: "black",
                }}
                onClick={() => this.goToEdit(this.state.book.id)}
                class="btn btn-primary"
                disabled={this.state.hasRequestAccepted == true}
              >
                <b>Editar</b>
              </button>
              <button
                onClick={() =>
                  this.deleteBook(this.state.book.id, this.state.images)
                }
                style={{
                  color: "red",
                  backgroundImage: "url(https://i.ibb.co/YRy9kHC/paper.jpg)",
                }}
                class="btn btn-primary"
                disabled={this.state.hasRequestAccepted == true}
              >
                <b>Eliminar</b>
              </button>
              {this.state.hasRequestAccepted ? (
                <p class="text-danger">
                  <b>Tiene una petición ACEPTADA</b>
                </p>
              ) : (
                <p></p>
              )}
            </center>
          ) : (
            <p></p>
          )}

          {this.state.username != null &&
          this.state.username != this.state.book.username ? (
            <center>
              {!this.state.isAdded ? (
                <center>
                  <a
                    class="btn btn-primary"
                    style={{
                      backgroundImage:
                        "url(https://i.ibb.co/YRy9kHC/paper.jpg)",
                    }}
                    onClick={() => this.addFavouriteBook(this.state.book.id)}
                  >
                    <img
                      style={{ height: "30px", width: "30px" }}
                      src="https://i.ibb.co/WktpFGx/No-Favorito.png"
                    ></img>{" "}
                    <b>Añadir a favoritos</b>
                  </a>
                  <br></br>
                </center>
              ) : (
                <center>
                  <a
                    class="btn btn-primary"
                    style={{
                      backgroundImage:
                        "url(https://i.ibb.co/YRy9kHC/paper.jpg)",
                    }}
                    onClick={() =>
                      this.deleteFavouriteBook(
                        this.state.book.id,
                        this.state.book.title
                      )
                    }
                  >
                    <img
                      style={{ height: "30px", width: "30px" }}
                      src="https://i.ibb.co/xXKJXKS/Favorito.png"
                    ></img>{" "}
                    <b>Eliminar de favoritos</b>
                  </a>
                  <br></br>
                </center>
              )}
            </center>
          ) : (
            <p></p>
          )}

          {this.state.username != this.state.book.username &&
          this.state.username != null ? (
            !this.state.alreadyRequest ? (
              <center>
                {!this.state.hasRequestAccepted ? (
                  <a
                    href={"/requests/" + this.state.book.id + "/add"}
                    style={{
                      color: "black",
                      marginTop: "20px",
                      backgroundImage:
                        "url(https://i.ibb.co/YRy9kHC/paper.jpg)",
                    }}
                    class="btn btn-primary"
                  >
                    <b>Realizar petición</b>
                  </a>
                ) : (
                  <center>
                    <button
                      style={{
                        color: "black",
                        marginTop: "20px",
                        backgroundImage:
                          "url(https://i.ibb.co/YRy9kHC/paper.jpg)",
                      }}
                      class="btn btn-primary"
                      disabled
                    >
                      <b>Realizar petición</b>
                    </button>
                    <p style={{ color: "red" }}>
                      <b>Ya tiene una petición ACEPTADA</b>
                    </p>
                  </center>
                )}
              </center>
            ) : (
              <button
                style={{
                  background: "#099C01",
                  color: "green",
                  margin: "20px",
                  backgroundImage: "url(https://i.ibb.co/YRy9kHC/paper.jpg)",
                }}
                class="btn btn-primary"
                disabled
              >
                <b>Petición realizada</b>
              </button>
            )
          ) : (
            <p></p>
          )}
        </center>
      </div>
    );
  }

  async deleteBook(id, images) {
    const conf = confirm(
      "¿Está seguro de que quiere eliminarlo? Esta acción no es reversible."
    );
    if (conf) {
      const res = await bookService.delete(id, images);
      window.location.replace("/books/me/0");
    }
  }

  async addFavouriteBook(id) {
    const res = await userFavouriteBook.addFavouriteBook(id);
    window.location.replace("/books/" + id);
  }

  async deleteFavouriteBook(id, title) {
    const conf = confirm(
      "¿Está seguro de que quiere eliminar '" + title + "' de favoritos?"
    );
    if (conf) {
      const res = await userFavouriteBook.deleteFavouriteBook(id);
      window.location.replace("/books/" + id);
    }
  }

  async goToEdit(id) {
    window.location.replace("/books/" + id + "/edit");
  }
}

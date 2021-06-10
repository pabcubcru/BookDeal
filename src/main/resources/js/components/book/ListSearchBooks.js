import React, { Component } from "react";
import bookService from "../services/Book";
import userService from "../services/User";
import userFavouriteBook from "../services/UserFavouriteBook";
import searchService from "../services/Search";
import "./Pagination.css";
import "../ListBooks.css";
import "./Search.css";

export default class List extends Component {
  constructor() {
    super();
    this.state = {
      books: [],
      isAdded: [],
      username: "",
      pages: [],
      actualPage: 0,
      query: "",
      searchResult: true,
      images: [],
      title: "",
      noResults: "",
      selectSearch: "book",
      provinces: [],
      showBooks: false,
      fieldNumber1: "",
      fieldNumber2: "",
      fieldText: "",
      errorMessages: [],
      errorField: [],
      titles: [],
    };
  }

  async componentDidMount() {
    const page = this.props.match.params.page;
    let search = null;

    if (page != null) {
      const query = this.props.match.params.query;
      const type = this.props.match.params.type;
      if (page) {
        this.setState({ actualPage: parseInt(page) });
      } else {
        page = 0;
      }

      const q = String(query);
      let title = "";
      let noResults = "";
      if (q.includes("-")) {
        const qr = q.split("-");
        title = "Resultados entre " + qr[0] + " y " + qr[1];
        noResults =
          "No se encontraron resultados entre " + qr[0] + " y " + qr[1];
      } else {
        title = "Resultados para '" + query + "'";
        noResults = "No se encontraron resultados para '" + query + "'";
      }

      const res = await searchService.searchBook(query, page, type);

      search = res.search;

      const username = await userService.getUsername();

      this.setState({
        books: res.books,
        query: query,
        isAdded: res.isAdded,
        pages: res.pages,
        numTotalPages: parseInt(res.numTotalPages),
        searchResult: res.searchResult,
        images: res.urlImages,
        showBooks: true,
        title: title,
        noResults: noResults,
        username: username.username,
        fieldText: query,
      });
    } else {
      const ls = await searchService.findLast();
      search = ls.search;
    }

    if (search != null) {
      this.setState({
        selectSearch: search.type,
        fieldNumber1: search.number1,
        fieldNumber2: search.number2,
        fieldText: search.text,
      });
    }

    const p = await userService.getProvinces();
    const provinces = p.provinces;

    const username = await userService.getUsername();

    this.setState({ provinces: provinces, username: username.username });
  }

  render() {
    return (
      <div>
        <div class="search-input">
          <div class="col-3">
            <select
              style={{ float: "left" }}
              class="form-control chosen-select"
              id="selectStatus"
              value={this.state.selectSearch}
              onChange={(event) =>
                this.setState({
                  selectSearch: event.target.value,
                  errorMessages: [],
                  errorField: [],
                  fieldNumber1: "",
                  fieldNumber2: "",
                  fieldText: "",
                })
              }
            >
              <option value="" style={{ fontWeight: "bold" }} disabled>
                Libro
              </option>
              <option value="book">Campo de texto</option>
              <option
                value="publicationYear"
                disabled={this.state.username == null}
              >
                Año de publicación
              </option>
              <option value="rangeYears" disabled={this.state.username == null}>
                Rango de años
              </option>
              <option
                value="rangePrices"
                disabled={this.state.username == null}
              >
                Rango de precios
              </option>
              <option value="" style={{ fontWeight: "bold" }} disabled>
                Localización
              </option>
              <option value="postalCode" disabled={this.state.username == null}>
                Código postal
              </option>
              <option value="province" disabled={this.state.username == null}>
                Provincia
              </option>
              {this.state.username == null ? (
                <option value="" style={{ color: "red" }} disabled>
                  Inicie sesión para usar todas
                </option>
              ) : (
                <p></p>
              )}
            </select>
          </div>

          {this.state.selectSearch == "book" ? (
            <div class="col">
              <input
                class="form-control"
                value={this.state.fieldText}
                type="search"
                list="titles"
                placeholder="Título, ISBN, autor, géneros, editorial, estado, ..."
                onChange={(event) => this.searchTitles(event.target.value)}
              />
              <datalist id="titles">
                {this.state.titles.map((title) => {
                  const spl = title.split("//");
                  return <option value={spl[0]}>{spl[1]}</option>;
                })}
                {this.state.titles.length == 0 &&
                this.state.fieldText.length > 0 ? (
                  <option
                    value="No se encontraron coincidencias"
                    disabled
                  ></option>
                ) : (
                  <p></p>
                )}
              </datalist>
              {this.state.errorField.indexOf("text") != -1 ? (
                <p class="text-danger">
                  <b>
                    {
                      this.state.errorMessages[
                        this.state.errorField.indexOf("text")
                      ]
                    }
                  </b>
                </p>
              ) : (
                <p></p>
              )}
            </div>
          ) : (
            <p></p>
          )}

          {this.state.selectSearch == "publicationYear" ? (
            <div class="col">
              <input
                class="form-control"
                value={this.state.fieldNumber1}
                type="number"
                placeholder="Introduzca el año de publicación"
                onChange={(event) =>
                  this.setState({ fieldNumber1: event.target.value })
                }
              />
              {this.state.errorField.indexOf("number1") != -1 ? (
                <p class="text-danger">
                  <b>
                    {
                      this.state.errorMessages[
                        this.state.errorField.indexOf("number1")
                      ]
                    }
                  </b>
                </p>
              ) : (
                <p></p>
              )}
            </div>
          ) : (
            <p></p>
          )}

          {this.state.selectSearch == "rangeYears" ? (
            <div class="range-years">
              <div class="col">
                <input
                  class="form-control"
                  value={this.state.fieldNumber1}
                  type="number"
                  placeholder="Introduzca el año inicial"
                  onChange={(event) =>
                    this.setState({ fieldNumber1: event.target.value })
                  }
                />
                {this.state.errorField.indexOf("number1") != -1 ? (
                  <p class="text-danger">
                    <b>
                      {
                        this.state.errorMessages[
                          this.state.errorField.indexOf("number1")
                        ]
                      }
                    </b>
                  </p>
                ) : (
                  <p></p>
                )}
              </div>
              <div class="col">
                <input
                  class="form-control"
                  value={this.state.fieldNumber2}
                  type="number"
                  placeholder="Introduzca el año final"
                  onChange={(event) =>
                    this.setState({ fieldNumber2: event.target.value })
                  }
                />
                {this.state.errorField.indexOf("number2") != -1 ? (
                  <p class="text-danger">
                    <b>
                      {
                        this.state.errorMessages[
                          this.state.errorField.indexOf("number2")
                        ]
                      }
                    </b>
                  </p>
                ) : (
                  <p></p>
                )}
              </div>
            </div>
          ) : (
            <p></p>
          )}

          {this.state.selectSearch == "rangePrices" ? (
            <div class="range-years">
              <div class="col">
                <input
                  class="form-control"
                  value={this.state.fieldNumber1}
                  type="number"
                  placeholder="Introduzca el precio inicial (€)"
                  onChange={(event) =>
                    this.setState({ fieldNumber1: event.target.value })
                  }
                />
                {this.state.errorField.indexOf("number1") != -1 ? (
                  <p class="text-danger">
                    <b>
                      {
                        this.state.errorMessages[
                          this.state.errorField.indexOf("number1")
                        ]
                      }
                    </b>
                  </p>
                ) : (
                  <p></p>
                )}
              </div>
              <div class="col">
                <input
                  class="form-control"
                  value={this.state.fieldNumber2}
                  type="number"
                  placeholder="Introduzca el precio final (€)"
                  onChange={(event) =>
                    this.setState({ fieldNumber2: event.target.value })
                  }
                />
                {this.state.errorField.indexOf("number2") != -1 ? (
                  <p class="text-danger">
                    <b>
                      {
                        this.state.errorMessages[
                          this.state.errorField.indexOf("number2")
                        ]
                      }
                    </b>
                  </p>
                ) : (
                  <p></p>
                )}
              </div>
            </div>
          ) : (
            <p></p>
          )}

          {this.state.selectSearch == "postalCode" ? (
            <div class="col">
              <input
                class="form-control"
                value={this.state.fieldText}
                type="number"
                placeholder="Introduzca el código postal de 5 dígitos"
                onChange={(event) =>
                  this.setState({ fieldText: String(event.target.value) })
                }
              />
              {this.state.errorField.indexOf("text") != -1 ? (
                <p class="text-danger">
                  <b>
                    {
                      this.state.errorMessages[
                        this.state.errorField.indexOf("text")
                      ]
                    }
                  </b>
                </p>
              ) : (
                <p></p>
              )}
            </div>
          ) : (
            <p></p>
          )}

          {this.state.selectSearch == "province" ? (
            <div class="col">
              <select
                class="form-control chosen-select"
                id="selectProvince"
                value={this.state.fieldText}
                onChange={(event) =>
                  this.setState({ fieldText: event.target.value })
                }
              >
                <option value="">Despliega para ver las opciones</option>
                {this.state.provinces.map((province) => {
                  return (
                    <option value={province}>
                      {province.replaceAll("_", " ")}
                    </option>
                  );
                })}
              </select>
              {this.state.errorField.indexOf("text") != -1 ? (
                <p class="text-danger">
                  <b>
                    {
                      this.state.errorMessages[
                        this.state.errorField.indexOf("text")
                      ]
                    }
                  </b>
                </p>
              ) : (
                <p></p>
              )}
            </div>
          ) : (
            <div>
              <p></p>
              <br></br>
            </div>
          )}
          <button
            style={{ marginRight: "15px", height: "38px" }}
            class="btn btn-primary"
            type="button"
            onClick={() => this.postSearch()}
            disabled={this.state.selectSearch == ""}
          >
            {" "}
            Buscar <i class="fa fa-search"></i>
          </button>
        </div>

        {this.state.showBooks ? (
          <div>
            <hr></hr>
            <h1 style={{ float: "left", color: "black" }}>
              <b>{this.state.title}</b>
            </h1>
            <br></br>
            <br></br>
            <br></br>
            {this.state.books.length == 0 ||
            this.state.searchResult == false ? (
              <div>
                <p>
                  <b>{this.state.noResults}</b>
                </p>
                {this.state.books.length > 0 ? (
                  <h2 style={{ float: "left", color: "black" }}>
                    <b>Algunas recomendaciones</b>
                  </h2>
                ) : (
                  <p></p>
                )}
                <br></br>
                <br></br>
                <br></br>
              </div>
            ) : (
              <center>
                {this.state.books.length != 0 && this.state.pages.length > 1 ? (
                  <center>
                    <br></br>
                    {this.state.actualPage != 0 ? (
                      <span>
                        <a
                          class="btn btn-primary"
                          href={
                            "/search/" +
                            this.state.selectSearch +
                            "/0/" +
                            this.state.query
                          }
                        >
                          <b>{String("<<")}</b>
                        </a>
                        <a
                          style={{ margin: "5px" }}
                          class="btn btn-primary"
                          href={
                            "/search/" +
                            this.state.selectSearch +
                            "/" +
                            parseInt(this.state.actualPage - 1) +
                            "/" +
                            this.state.query
                          }
                        >
                          <b>{String("<")}</b>
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
                          href={
                            "/search/" +
                            this.state.selectSearch +
                            "/" +
                            page +
                            "/" +
                            this.state.query
                          }
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
                            "/search/" +
                            this.state.selectSearch +
                            "/" +
                            parseInt(this.state.actualPage + 1) +
                            "/" +
                            this.state.query
                          }
                        >
                          <b>{String(">")}</b>
                        </a>
                        <a
                          class="btn btn-primary"
                          href={
                            "/search/" +
                            this.state.selectSearch +
                            "/" +
                            parseInt(this.state.numTotalPages - 1) +
                            "/" +
                            this.state.query
                          }
                        >
                          <b>{String(">>")}</b>
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
                          <a
                            style={{ zIndex: "-10" }}
                            href={"/books/" + book.id}
                          >
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
                        {this.state.username != null ? (
                          this.state.isAdded[i] == false ? (
                            <a
                              onClick={() =>
                                this.addFavouriteBook(
                                  book.id,
                                  this.state.query,
                                  this.state.actualPage
                                )
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
                                  this.state.query,
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
                          )
                        ) : (
                          <p></p>
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
                    <a
                      class="btn btn-primary"
                      href={
                        "/search/" +
                        this.state.selectSearch +
                        "/0/" +
                        this.state.query
                      }
                    >
                      {String("<<")}
                    </a>
                    <a
                      class="btn btn-primary"
                      style={{ margin: "5px" }}
                      href={
                        "/search/" +
                        this.state.selectSearch +
                        "/" +
                        parseInt(this.state.actualPage - 1) +
                        "/" +
                        this.state.query
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
                      href={
                        "/search/" +
                        this.state.selectSearch +
                        "/" +
                        page +
                        "/" +
                        this.state.query
                      }
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
                        "/search/" +
                        this.state.selectSearch +
                        "/" +
                        parseInt(this.state.actualPage + 1) +
                        "/" +
                        this.state.query
                      }
                    >
                      {String(">")}
                    </a>
                    <a
                      class="btn btn-primary"
                      href={
                        "/search/" +
                        this.state.selectSearch +
                        "/" +
                        parseInt(this.state.numTotalPages - 1) +
                        "/" +
                        this.state.query
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
        ) : (
          <div>
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
            <br></br>
          </div>
        )}
      </div>
    );
  }

  async addFavouriteBook(id, query, page) {
    const res = await userFavouriteBook.addFavouriteBook(id);
    window.location.replace(
      "/search/" + this.state.selectSearch + "/" + page + "/" + query
    );
  }

  async deleteFavouriteBook(id, title, query, page) {
    const conf = confirm(
      "¿Está seguro de que quiere eliminar '" + title + "' de favoritos?"
    );
    if (conf) {
      const res = await userFavouriteBook.deleteFavouriteBook(id);
      window.location.replace(
        "/search/" + this.state.selectSearch + "/" + page + "/" + query
      );
    }
  }

  async postSearch() {
    const res = await searchService.postSearch(this.state);
    if (res.success) {
      window.location.replace(
        "/search/" + this.state.selectSearch + "/0/" + res.query
      );
    } else {
      const errFields = [];
      const errMess = [];
      const error = res.errors;
      error.map((itemerror) => {
        errFields.push(itemerror.field);
        errMess.push(itemerror.defaultMessage);
      });
      this.setState({ errorField: errFields, errorMessages: errMess });
    }
  }

  async searchTitles(query) {
    const q = query.trim();
    this.setState({ fieldText: query.replaceAll("  ", " ") });
    if (q.length > 0 && q.length <= 80) {
      let res = await searchService.searchTitles(q);
      this.setState({ titles: res.titles });
    }
  }
}

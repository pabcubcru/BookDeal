import React, { Component } from "react";
import bookService from "../services/Book";
import requestService from "../services/Request";

export default class Form extends Component {
  constructor() {
    super();
    this.state = {
      fieldIdBook1: "",
      fieldComment: "",
      fieldIdBook2: "",
      fieldPay: "",
      fieldAction: "",
      book: "",
      books: [],
      errorField: [],
      errorMessages: [],
      noBooks: true,
      sell: null,
    };
  }

  async componentDidMount() {
    const id = this.props.match.params.id;

    const b = await bookService.getBook(id);

    const bk = await bookService.listMyBooksForChange();

    this.setState({
      book: b.book,
      fieldIdBook2: id,
      books: bk.books,
      fieldPay: b.book.price,
    });

    if (bk.books.length >= 1) {
      this.setState({ fieldIdBook1: bk.books[0].id, noBooks: false });
    }
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
        <h3 style={{ color: "#007bff" }}>
          Realizar petición a {this.state.book.title}
        </h3>
        <p class="text-danger">*Obligatorio</p>

        <div class="form-group row">
          <label for="firstName" class="col-sm-3 col-form-label">
            ¿Qué quiere hacer?<sup class="text-danger">*</sup>
          </label>
          <div class="col-sm-9">
            <select
              class="form-control"
              id="selectAction"
              value={this.state.fieldAction}
              onChange={(event) =>
                this.setState({
                  fieldAction: event.target.value,
                  sell:
                    event.target.value == ""
                      ? null
                      : event.target.value == "COMPRA"
                      ? true
                      : false,
                  errorField: [],
                  errorMessages: [],
                })
              }
            >
              <option value="">Despliega para ver opciones</option>
              <option value="COMPRA">COMPRA</option>
              <option value="INTERCAMBIO">INTERCAMBIO</option>
            </select>
            {this.state.errorField.indexOf("action") != -1 ? (
              <p class="text-danger">
                {
                  this.state.errorMessages[
                    this.state.errorField.indexOf("action")
                  ]
                }
              </p>
            ) : (
              <p></p>
            )}
          </div>
        </div>

        {this.state.sell != null ? (
          this.state.sell == false ? (
            <div class="form-group row">
              <label for="firstName" class="col-sm-3 col-form-label">
                Seleccione su libro<sup class="text-danger">*</sup>
              </label>
              <div class="col-sm-9">
                <select
                  class="form-control chosen-select"
                  id="selectBook"
                  value={this.state.fieldIdBook1}
                  onChange={(event) =>
                    this.setState({ fieldIdBook1: event.target.value })
                  }
                  disabled={this.state.noBooks}
                >
                  {this.state.books.map((book) => {
                    return <option value={book.id}>{book.title}</option>;
                  })}
                </select>
                {this.state.errorField.indexOf("idBook1") != -1 ? (
                  <p class="text-danger">
                    {
                      this.state.errorMessages[
                        this.state.errorField.indexOf("idBook1")
                      ]
                    }
                  </p>
                ) : (
                  <p></p>
                )}
                {this.state.noBooks ? (
                  <div>
                    <p class="text-danger">
                      *No tiene libros para intercambiar.{" "}
                      <a href="/books/new" class="btn btn-primary">
                        ¡Añade uno!
                      </a>
                    </p>
                  </div>
                ) : (
                  <p></p>
                )}
              </div>
            </div>
          ) : (
            <div>
              <div class="form-group row">
                <label for="firstName" class="col-sm-3 col-form-label">
                  Precio original:
                </label>
                <div class="col-sm-9">
                  <p>{this.state.book.price}€</p>
                </div>
              </div>
              <div class="form-group row">
                <label for="firstName" class="col-sm-3 col-form-label">
                  ¿Cuánto pagaría?<sup class="text-danger">*</sup>
                </label>
                <div class="col-sm-9">
                  <input
                    type="number"
                    class="form-control"
                    value={this.state.fieldPay}
                    onChange={(event) =>
                      this.setState({ fieldPay: event.target.value })
                    }
                  />
                  {this.state.errorField.indexOf("pay") != -1 ? (
                    <p class="text-danger">
                      {
                        this.state.errorMessages[
                          this.state.errorField.indexOf("pay")
                        ]
                      }
                    </p>
                  ) : (
                    <p></p>
                  )}
                </div>
              </div>
            </div>
          )
        ) : (
          <p></p>
        )}

        <div class="form-group row">
          <label for="firstName" class="col-sm-3 col-form-label">
            Comentario adicional
          </label>
          <div class="col-sm-9">
            <input
              type="text"
              class="form-control"
              value={this.state.fieldComment}
              onChange={(event) =>
                this.setState({ fieldComment: event.target.value })
              }
              disabled={this.state.noBooks && this.state.sell == false}
            />
            {this.state.errorField.indexOf("comment") != -1 ? (
              <p class="text-danger">
                {
                  this.state.errorMessages[
                    this.state.errorField.indexOf("comment")
                  ]
                }
              </p>
            ) : (
              <p></p>
            )}
          </div>
        </div>

        <div class="form-group row">
          <div class="col-sm-6">
            <br></br>
            <button
              onClick={() => this.onClickSave()}
              class="btn btn-primary"
              style={{ float: "left" }}
              type="submit"
              disabled={
                this.state.noBooks &&
                (this.state.sell == false || this.state.sell == null)
              }
            >
              Enviar
            </button>
          </div>
        </div>
      </div>
    );
  }

  async onClickSave() {
    const res = await requestService.create(this.state);
    if (res.success) {
      window.location.replace("/books/" + this.state.book.id);
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
}

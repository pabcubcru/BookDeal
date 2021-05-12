import React, { Component } from "react";
import userService from "../services/User";
import bookService from "../services/Book";
import { Link } from "react-router-dom";

export default class Form extends Component {
  constructor() {
    super();
    this.state = {
      fieldName: "",
      fieldEmail: "",
      fieldPhone: "+34",
      fieldBirthDate: "",
      fieldUsername: "",
      fieldCity: "",
      fieldProvince: "",
      fieldPostCode: "",
      fieldGenres: "",
      fieldPassword: "",
      fieldConfirmPassword: "",
      fieldCheckbok: false,
      errorField: [],
      errorMessages: [],
      provinces: [],
      genres: [],
      fieldGen: [],
    };
  }

  async componentDidMount() {
    const p = await userService.getProvinces();
    const provinces = p.provinces;

    const genres = await bookService.getGenres();
    const gen = genres.genres;
    this.setState({ provinces: provinces, genres: gen });
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
        <h1 style={{ color: "#007bff" }}>Registro</h1>
        <p class="text-danger">*Obligatorio</p>
        <div class="form-group row">
          <label for="firstName" class="col-sm-3 col-form-label">
            Nombre y apellidos<sup class="text-danger">*</sup>
          </label>
          <div class="col-sm-9">
            <input
              type="text"
              class="form-control"
              value={this.state.fieldName}
              onChange={(event) =>
                this.setState({ fieldName: event.target.value })
              }
            />
            {this.state.errorField.indexOf("name") != -1 ? (
              <p class="text-danger">
                {
                  this.state.errorMessages[
                    this.state.errorField.indexOf("name")
                  ]
                }
              </p>
            ) : (
              <p></p>
            )}
          </div>
        </div>

        <div class="form-group row">
          <label for="firstName" class="col-sm-3 col-form-label">
            Nombre de usuario<sup class="text-danger">*</sup>
          </label>
          <div class="col-sm-9">
            <input
              type="text"
              class="form-control"
              value={this.state.fieldUsername}
              onChange={(event) =>
                this.setState({ fieldUsername: event.target.value })
              }
            />
            {this.state.errorField.indexOf("username") != -1 ? (
              <p class="text-danger">
                {
                  this.state.errorMessages[
                    this.state.errorField.indexOf("username")
                  ]
                }
              </p>
            ) : (
              <p></p>
            )}
          </div>
        </div>

        <div class="form-group row">
          <label for="firstName" class="col-sm-3 col-form-label">
            Email<sup class="text-danger">*</sup>
          </label>
          <div class="col-sm-9">
            <input
              type="email"
              class="form-control"
              placeholder="email@email.com"
              value={this.state.fieldEmail}
              onChange={(event) =>
                this.setState({ fieldEmail: event.target.value })
              }
            />
            {this.state.errorField.indexOf("email") != -1 ? (
              <p class="text-danger">
                {
                  this.state.errorMessages[
                    this.state.errorField.indexOf("email")
                  ]
                }
              </p>
            ) : (
              <p></p>
            )}
          </div>
        </div>

        <div class="form-group row">
          <label for="firstName" class="col-sm-3 col-form-label">
            Fecha de nacimiento<sup class="text-danger">*</sup>
          </label>
          <div class="col-sm-9">
            <input
              type="date"
              class="form-control"
              placeholder="dd/MM/yyyy"
              value={this.state.fieldBirthDate}
              onChange={(event) =>
                this.setState({ fieldBirthDate: event.target.value })
              }
            />
            {this.state.errorField.indexOf("birthDate") != -1 ? (
              <p class="text-danger">
                {
                  this.state.errorMessages[
                    this.state.errorField.indexOf("birthDate")
                  ]
                }
              </p>
            ) : (
              <p></p>
            )}
          </div>
        </div>

        <div class="form-group row">
          <label for="firstName" class="col-sm-3 col-form-label">
            Teléfono<sup class="text-danger">*</sup>
          </label>
          <div class="col-sm-9">
            <input
              type="tel"
              class="form-control"
              placeholder="+34123456789"
              value={this.state.fieldPhone}
              onChange={(event) =>
                this.setState({
                  fieldPhone: event.target.value.replace(" ", ""),
                })
              }
            />
            {this.state.errorField.indexOf("phone") != -1 ? (
              <p class="text-danger">
                {
                  this.state.errorMessages[
                    this.state.errorField.indexOf("phone")
                  ]
                }
              </p>
            ) : (
              <p></p>
            )}
          </div>
        </div>

        <div class="form-group row">
          <label for="firstName" class="col-sm-3 col-form-label">
            Provincia<sup class="text-danger">*</sup>
          </label>
          <div class="col-sm-9">
            <select
              class="form-control"
              id="selectProvince"
              onChange={(event) =>
                this.setState({ fieldProvince: event.target.value })
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
            {this.state.errorField.indexOf("province") != -1 ? (
              <p class="text-danger">
                {
                  this.state.errorMessages[
                    this.state.errorField.indexOf("province")
                  ]
                }
              </p>
            ) : (
              <p></p>
            )}
          </div>
        </div>

        <div class="form-group row">
          <label for="firstName" class="col-sm-3 col-form-label">
            Código postal<sup class="text-danger">*</sup>
          </label>
          <div class="col-sm-9">
            <input
              type="text"
              class="form-control"
              placeholder="41012"
              value={this.state.fieldPostCode}
              onChange={(event) =>
                this.setState({ fieldPostCode: event.target.value })
              }
            />
            {this.state.errorField.indexOf("postCode") != -1 ? (
              <p class="text-danger">
                {
                  this.state.errorMessages[
                    this.state.errorField.indexOf("postCode")
                  ]
                }
              </p>
            ) : (
              <p></p>
            )}
          </div>
        </div>

        <div class="form-group row">
          <label for="firstName" class="col-sm-3 col-form-label">
            Seleccione sus géneros preferidos (3 mínimo)
            <sup class="text-danger">*</sup>
          </label>
          <div class="col-sm-9">
            <select
              class="form-control chosen-select"
              id="selectGenres"
              value={this.state.fieldGen}
              onClick={(event) => this.editGenres(event.target.value)}
              multiple
            >
              {this.state.genres.map((genre) => {
                return (
                  <option value={genre}>{genre.replaceAll("_", " ")}</option>
                );
              })}
            </select>
            {this.state.errorField.indexOf("genres") != -1 ? (
              <p class="text-danger">
                {
                  this.state.errorMessages[
                    this.state.errorField.indexOf("genres")
                  ]
                }
              </p>
            ) : (
              <p></p>
            )}
          </div>
        </div>

        <div class="form-group row">
          <label for="firstName" class="col-sm-3 col-form-label">
            Contraseña<sup class="text-danger">*</sup>
          </label>
          <div class="col-sm-9">
            <input
              type="password"
              class="form-control"
              value={this.state.fieldPassword}
              onChange={(event) =>
                this.setState({ fieldPassword: event.target.value })
              }
            />
            {this.state.errorField.indexOf("password") != -1 ? (
              <p class="text-danger">
                {
                  this.state.errorMessages[
                    this.state.errorField.indexOf("password")
                  ]
                }
              </p>
            ) : (
              <p></p>
            )}
          </div>
        </div>

        <div class="form-group row">
          <label for="firstName" class="col-sm-3 col-form-label">
            Confirma contraseña<sup class="text-danger">*</sup>
          </label>
          <div class="col-sm-9">
            <input
              type="password"
              class="form-control"
              value={this.state.fieldConfirmPassword}
              onChange={(event) =>
                this.setState({ fieldConfirmPassword: event.target.value })
              }
            />
            {this.state.errorField.indexOf("confirmPassword") != -1 ? (
              <p class="text-danger">
                {
                  this.state.errorMessages[
                    this.state.errorField.indexOf("confirmPassword")
                  ]
                }
              </p>
            ) : (
              <p></p>
            )}
          </div>
        </div>

        <div class="form-group row">
          <label for="firstName" class="col-sm-3 col-form-label" />
          <div class="col-sm-9">
            <input
              value={this.state.fieldCheckbok}
              onChange={(event) =>
                this.setState({ fieldCheckbok: !this.state.fieldCheckbok })
              }
              class="form-check-input"
              type="checkbox"
            />{" "}
            Acepto los términos y condiciones<sup class="text-danger">*</sup>
            {this.state.errorField.indexOf("accept") != -1 ? (
              <p class="text-danger">
                {
                  this.state.errorMessages[
                    this.state.errorField.indexOf("accept")
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
            <button
              onClick={() => this.onClickSave()}
              class="btn btn-primary"
              type="submit"
            >
              Registrarme
            </button>
          </div>
        </div>

        <hr />
        <p>
          ¿Ya estás registrado? <Link to="/login">Iniciar sesión</Link>
        </p>
      </div>
    );
  }

  async onClickSave() {
    const res = await userService.create(this.state);
    if (res.success) {
      window.location.replace("/");
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

  async editGenres(element) {
    const gen = this.state.fieldGen;
    if (!gen.includes(element)) {
      gen.push(element);
    } else {
      gen.splice(gen.indexOf(element), 1);
    }
    this.setState({ fieldGen: gen });
    this.setState({ fieldGenres: gen.join(",") });
  }
}

import React, { Component } from 'react';
import userService from "../services/User";
import bookService from "../services/Book";

export default class Form extends Component {

  constructor(){
    super();
    this.state = {
        id:"",
        fieldName:"",
        fieldEmail:"",
        fieldPhone:"",
        fieldBirthDate:"",
        fieldUsername: "",
        fieldCity:"",
        fieldProvince:"",
        fieldPostCode:"",
        fieldPassword: "",
        fieldConfirmPassword: "",
        fieldGenres:"",
        errorField:[],
        errorMessages:[],
        messageCorrectUser:"",
        messageCorrectPassword:"",
        provinces:[],
        genres:[],
        fieldGen:[]
    }
  }

  async componentDidMount() {
    const usern = await userService.getUsername()
    const username = usern.username
    const res = await userService.getUser(username)
    const p = await userService.getProvinces()
    const prov = p.provinces

    const genres = await bookService.getGenres()
    const gen = genres.genres
    const genrs = res.user.genres.split(",")
    if(res.success){
        this.setState({
            id: res.user.id,
            fieldName: res.user.name,
            fieldEmail: res.user.email,
            fieldPhone: res.user.phone,
            fieldBirthDate: res.user.birthDate,
            fieldUsername: res.user.username,
            fieldProvince: res.user.province,
            fieldCity: res.user.city,
            fieldPostCode: res.user.postCode,
            fieldGenres: res.user.genres,
            provinces:prov,
            genres:gen,
            fieldGen:genrs
        })
    } /*else {
        window.location.replace("/error")
    }*/
  }

  render() {
    return (
      
      <div style={{backgroundImage: "url(https://i.pinimg.com/originals/8d/23/06/8d2306b98839234e49ce96a8b76e20ae.jpg)", 
      backgroundSize: "cover" , padding: "50px", borderRadius: '5px', fontWeight: "bold", marginLeft: "-100"}}>
        <h1 style={{color: "#007bff"}}>Mi perfil</h1>
        <p class='text-danger'>*Obligatorio</p>
        <div class="form-group row">
          <label for="firstName" class="col-sm-3 col-form-label">Nombre y apellidos<sup class='text-danger'>*</sup></label>
          <div class="col-sm-9">
            <input type="text" class="form-control"
              value={this.state.fieldName} 
              onChange={(event)=>this.setState({fieldName:event.target.value})}/>
              {this.state.errorField.indexOf("name") != -1 ? 
                <p class='text-danger'>{this.state.errorMessages[this.state.errorField.indexOf("name")]}</p>
              :
                <p></p>
              }
          </div>
        </div>

        <div class="form-group row">
          <label for="firstName" class="col-sm-3 col-form-label">Nombre de usuario<sup class='text-danger'>*</sup></label>
          <div class="col-sm-9">
            <input type="text" class="form-control" 
              value={this.state.fieldUsername} 
              onChange={(event)=>this.setState({fieldUsername:event.target.value})} readOnly/>
          </div>
        </div>

				<div class="form-group row">
            <label for="firstName" class="col-sm-3 col-form-label">Email<sup class='text-danger'>*</sup></label>
          <div class="col-sm-9">
            <input type="email" class="form-control" placeholder="email@email.com"  
              value={this.state.fieldEmail} 
              onChange={(event)=>this.setState({fieldEmail:event.target.value})}/>
              {this.state.errorField.indexOf("email") != -1 ? 
                <p class='text-danger'>{this.state.errorMessages[this.state.errorField.indexOf("email")]}</p>
              :
                <p></p>
              }
          </div>
        </div>

				<div class="form-group row">
            <label for="firstName" class="col-sm-3 col-form-label">Fecha de nacimiento<sup class='text-danger'>*</sup></label>
          <div class="col-sm-9">
            <input type="date" class="form-control" placeholder="dd/MM/yyyy"  
              value={this.state.fieldBirthDate} 
              onChange={(event)=>this.setState({fieldBirthDate:event.target.value})}/>
              {this.state.errorField.indexOf("birthDate") != -1 ? 
                <p class='text-danger'>{this.state.errorMessages[this.state.errorField.indexOf("birthDate")]}</p>
              :
                <p></p>
              }
          </div>
        </div>

				<div class="form-group row">
            <label for="firstName" class="col-sm-3 col-form-label">Teléfono<sup class='text-danger'>*</sup></label>
          <div class="col-sm-9">
            <input type="tel" class="form-control" placeholder="+34123456789"  
              value={this.state.fieldPhone} 
              onChange={(event)=>this.setState({fieldPhone:event.target.value.replace(" ", "")})}/>
              {this.state.errorField.indexOf("phone") != -1 ? 
                <p class='text-danger'>{this.state.errorMessages[this.state.errorField.indexOf("phone")]}</p>
              :
                <p></p>
              }
          </div>
        </div>

        <div class="form-group row">
            <label for="firstName" class="col-sm-3 col-form-label">Provincia<sup class='text-danger'>*</sup></label>
          <div class="col-sm-9">
            <select class="form-control" id="selectProvince" value={this.state.fieldProvince} onChange={(event) => this.setState({fieldProvince:event.target.value})}>
            <option value="">Despliega para ver las opciones</option>
            {this.state.provinces.map((province) => {
              return (
                <option value={province}>{province}</option>
              )
            })}
            </select>
              {this.state.errorField.indexOf("province") != -1 ? 
                <p class='text-danger'>{this.state.errorMessages[this.state.errorField.indexOf("province")]}</p>
              :
                <p></p>
              }
          </div>
        </div>

        <div class="form-group row">
            <label for="firstName" class="col-sm-3 col-form-label">Código postal<sup class='text-danger'>*</sup></label>
          <div class="col-sm-9">
            <input type="text" class="form-control" placeholder="41012"  
              value={this.state.fieldPostCode} 
              onChange={(event)=>this.setState({fieldPostCode:event.target.value})}/>
              {this.state.errorField.indexOf("postCode") != -1 ? 
                <p class='text-danger'>{this.state.errorMessages[this.state.errorField.indexOf("postCode")]}</p>
              :
                <p></p>
              }
          </div>
        </div>

        <div class="form-group row">
            <label for="firstName" class="col-sm-3 col-form-label">Seleccione sus géneros preferidos<sup class='text-danger'>*</sup></label>
          <div class="col-sm-9">
          <select class="form-control chosen-select" id="selectGenres" value={this.state.fieldGen} onClick={(event) => this.editGenres(event.target.value)} multiple>
            {this.state.genres.map((genre) => {
              return (
                <option value={genre}>{genre.replaceAll("_", " ")}</option>
              )
            })}
            </select>
            {this.state.errorField.indexOf("genres") != -1 ? 
                <p class='text-danger'>{this.state.errorMessages[this.state.errorField.indexOf("genres")]}</p>
              :
                <p></p>
              }
          </div>
        </div>

        {<p style={{color: "#099C01"}}>{this.state.messageCorrectUser}</p>}

        <div class="form-group row">
            <div class="col-sm-6" >
        <button onClick={()=>this.onClickSave()} class="btn btn-primary" type="submit">Actualizar</button>
            </div>
        </div>

        <hr/>
        <h1 style={{color: "#007bff"}}>Cambiar contraseña</h1>
        <br></br>
        <div class="form-group row">
            <label for="firstName" class="col-sm-3 col-form-label">Contraseña<sup class='text-danger'>*</sup></label>
          <div class="col-sm-9">
            <input type="password" class="form-control" placeholder="Debe contener entre 8 y 20 carácteres"
              value={this.state.fieldPassword} 
              onChange={(event)=>this.setState({fieldPassword: event.target.value})}/>
              {this.state.errorField.indexOf("password") != -1 ? 
                <p class='text-danger'>{this.state.errorMessages[this.state.errorField.indexOf("password")]}</p>
              :
                <p></p>
              }
          </div>
        </div>

        <div class="form-group row">
            <label for="firstName" class="col-sm-3 col-form-label">Confirma contraseña<sup class='text-danger'>*</sup></label>
          <div class="col-sm-9">
            <input type="password" class="form-control" placeholder="Debe contener entre 8 y 20 carácteres"
              value={this.state.fieldConfirmPassword} 
              onChange={(event)=>this.setState({fieldConfirmPassword: event.target.value})}/>
              {this.state.errorField.indexOf("confirmPassword") != -1 ? 
                <p class='text-danger'>{this.state.errorMessages[this.state.errorField.indexOf("confirmPassword")]}</p>
              :
                <p></p>
              }
          </div>
        </div>

        {<p style={{color: "#099C01"}}>{this.state.messageCorrectPassword}</p>}


        <div class="form-group row">
            <div class="col-sm-6" >
        <button onClick={()=>this.onClickSave()} class="btn btn-primary" type="submit" disabled={this.state.fieldPassword.length < 8 || this.state.fieldPassword.length > 20
        || this.state.fieldConfirmPassword.length < 8 || this.state.fieldConfirmPassword.length > 20}>Cambiar</button>
            </div>
        </div>

      </div>
      
    )
  }

    async onClickSave() {
        const res = await userService.edit(this.state)
        if (res.success) {
          if(this.state.fieldPassword != "" && this.state.fieldConfirmPassword != ""){
            this.setState({messageCorrectPassword: "*La contraseña se ha cambiado con éxito.", 
            errorFieldPassword:[], messageCorrectUser:"", fieldPassword:"", fieldConfirmPassword:"", errorField:[], errorMessages:[]})
          } else {
            this.setState({messageCorrectUser: "*Los datos se han actualizado con éxito.", 
            messageCorrectPassword:"", errorField:[], errorMessages:[]})
            //window.location.replace("/profile")
          }
        } else {
          const errFields = []
          const errMess = []
          const error = res.errors
          error.map((itemerror)=>{
          errFields.push(itemerror.field)
          errMess.push(itemerror.defaultMessage)
          })
          this.setState({errorField:errFields, errorMessages:errMess})
        }
	}

  async editGenres(element) {
    const gen = this.state.fieldGen
    if(!gen.includes(element)) {
      gen.push(element)
    } else {
      gen.splice(gen.indexOf(element), 1)
    }
    this.setState({fieldGen:gen})
    this.setState({fieldGenres:gen.join(",")})
  }
}


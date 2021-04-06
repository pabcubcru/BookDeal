import React, { Component } from 'react';
import userService from "../services/User"
import { Link } from "react-router-dom";

export default class Form extends Component {

  constructor(){
    super();
    this.state = {
      fieldName:"",
      fieldEmail:"",
      fieldPhone:"+34",
      fieldBirthDate:"",
      fieldUsername: "",
      fieldCity:"",
      fieldProvince:"Álava",
      fieldPostCode:"",
      fieldPassword: "",
      fieldConfirmPassword:"",
      fieldCheckbok: false,
      errorField:[],
      provinces:[]
    }
  }

  async componentDidMount() {
    const p = await userService.getProvinces()
    const res = p.provinces
    this.setState({provinces:res})
  }

  render() {
    return (
      
      <div style={{backgroundImage: "url(https://i.pinimg.com/originals/8d/23/06/8d2306b98839234e49ce96a8b76e20ae.jpg)", 
      backgroundSize: "cover" , padding: "50px", fontWeight: "bold", marginLeft: "-100"}}>
        <h1 style={{color: "#007bff"}}>Registro</h1>
        <p class='text-danger'>*Obligatorio</p>
        <div class="form-group row">
          <label for="firstName" class="col-sm-3 col-form-label">Nombre y apellidos<sup class='text-danger'>*</sup></label>
          <div class="col-sm-9">
            <input type="text" class="form-control"
              value={this.state.fieldName} 
              onChange={(event)=>this.setState({fieldName:event.target.value})}/>
          </div>
        </div>

        <div class="form-group row">
          <label for="firstName" class="col-sm-3 col-form-label">Nombre de usuario<sup class='text-danger'>*</sup></label>
          <div class="col-sm-9">
            <input type="text" class="form-control" 
              value={this.state.fieldUsername} 
              onChange={(event)=>this.setState({fieldUsername:event.target.value})}/>
          </div>
        </div>

				<div class="form-group row">
            <label for="firstName" class="col-sm-3 col-form-label">Email<sup class='text-danger'>*</sup></label>
          <div class="col-sm-9">
            <input type="email" class="form-control" placeholder="email@email.com"  
              value={this.state.fieldEmail} 
              onChange={(event)=>this.setState({fieldEmail:event.target.value})}/>
          </div>
        </div>

				<div class="form-group row">
            <label for="firstName" class="col-sm-3 col-form-label">Fecha de nacimiento<sup class='text-danger'>*</sup></label>
          <div class="col-sm-9">
            <input type="date" class="form-control" placeholder="dd/MM/yyyy"  
              value={this.state.fieldBirthDate} 
              onChange={(event)=>this.setState({fieldBirthDate:event.target.value})}/>
          </div>
        </div>

				<div class="form-group row">
            <label for="firstName" class="col-sm-3 col-form-label">Teléfono<sup class='text-danger'>*</sup></label>
          <div class="col-sm-9">
            <input type="tel" class="form-control" placeholder="+34 123456789"  
              value={this.state.fieldPhone} 
              onChange={(event)=>this.setState({fieldPhone:event.target.value})}/>
          </div>
        </div>

        <div class="form-group row">
            <label for="firstName" class="col-sm-3 col-form-label">Provincia<sup class='text-danger'>*</sup></label>
          <div class="col-sm-9">
            <select class="form-control" id="selectProvince" onChange={(event) => this.setState({fieldProvince:event.target.value})}>
            {this.state.provinces.map((province) => {
              return (
                <option value={province}>{province}</option>
              )
            })}
            </select>
          </div>
        </div>

        <div class="form-group row">
            <label for="firstName" class="col-sm-3 col-form-label">Ciudad<sup class='text-danger'>*</sup></label>
          <div class="col-sm-9">
            <input type="text" class="form-control" placeholder="Sevilla"  
              value={this.state.fieldCity} 
              onChange={(event)=>this.setState({fieldCity:event.target.value})}/>
          </div>
        </div>

        <div class="form-group row">
            <label for="firstName" class="col-sm-3 col-form-label">Código postal<sup class='text-danger'>*</sup></label>
          <div class="col-sm-9">
            <input type="text" class="form-control" placeholder="41012"  
              value={this.state.fieldPostCode} 
              onChange={(event)=>this.setState({fieldPostCode:event.target.value})}/>
          </div>
        </div>

        <div class="form-group row">
            <label for="firstName" class="col-sm-3 col-form-label">Contraseña<sup class='text-danger'>*</sup></label>
          <div class="col-sm-9">
            <input type="password" class="form-control"
              value={this.state.fieldPassword} 
              onChange={(event)=>this.setState({fieldPassword: event.target.value})}/>
          </div>
        </div>

        <div class="form-group row">
            <label for="firstName" class="col-sm-3 col-form-label">Confirma contraseña<sup class='text-danger'>*</sup></label>
          <div class="col-sm-9">
            <input type="password" class="form-control"
              value={this.state.fieldConfirmPassword} 
              onChange={(event)=>this.setState({fieldConfirmPassword: event.target.value})}/>
          </div>
        </div>

        <div class="form-group row">
          <label for="firstName" class="col-sm-3 col-form-label"/>
          <div class="col-sm-9">
          <input value={this.state.fieldCheckbok} 
          onChange={(event)=>this.setState({fieldCheckbok: !this.state.fieldCheckbok})} 
          class="form-check-input" type="checkbox"/> Acepto los términos y condiciones<sup class='text-danger'>*</sup>
          </div>
        </div>


        {
          this.state.errorField.map((itemerror) => {
            return(
              <p class='text-danger'>*{itemerror}</p>
            )
          })
        }

				<div class="form-group row">
					<div class="col-sm-6" >
		      	<button onClick={()=>this.onClickSave()} class="btn btn-primary" type="submit">Registrarme</button>
					</div>
				</div>

        <hr/>
          <p>¿Ya estás registrado? <Link to="/login">Iniciar sesión</Link></p>
      </div>
      
    )
  }

  async onClickSave() {
		const res = await userService.create(this.state)

    if(this.state.fieldPassword == "" && this.state.fieldConfirmPassword == ""){
      const dataError = []
			dataError.push("La contraseña es un campo requerido.");
			this.setState({errorField:dataError});
    } else if(this.state.fieldPassword != this.state.fieldConfirmPassword){
      const dataError = []
			dataError.push("Las contraseñas no coinciden.");
			this.setState({errorField:dataError});
    } else if(this.state.fieldCheckbok==false){
      const dataError = []
			dataError.push("Debe aceptar los términos y condiciones.");
			this.setState({errorField:dataError});
    } else {
      if (res.success) {
        window.location.replace("/")
      } else if (res.status==400) {
        const dataError = []
        const error = res.data.errors
        error.map((itemerror)=>{
        dataError.push(itemerror.defaultMessage)
        })
        this.setState({errorField:dataError})
      } else {
        const dataError = []
        dataError.push(res.message);
        this.setState({errorField:dataError});
      }
    }
	}
}


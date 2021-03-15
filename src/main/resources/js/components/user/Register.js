import React, { Component } from 'react';
import userService from "../services/User"
import { Link } from "react-router-dom";

export default class Form extends Component {

  constructor(){
    super();
    this.state = {
      fieldName:"",
      fieldEmail:"",
      fieldPhone:"",
      fieldBirthDate:"",
      fieldUsername: "",
      fieldPassword: "",
      fieldConfirmPassword:"",
      fieldCheckbok: false,
      errorField:[]
    }
  }

  render() {
    return (
      
      <div style={{backgroundImage: "url(https://i.pinimg.com/originals/8d/23/06/8d2306b98839234e49ce96a8b76e20ae.jpg)", 
      backgroundSize: "cover" , padding: "50px", fontWeight: "bold", marginLeft: "-100"}}>
        <h1 style={{color: "#007bff"}}>Registro</h1>
        <br></br>
        <div class="form-group row">
          <label for="firstName" class="col-sm-2 col-form-label">Nombre</label>
          <div class="col-sm-10">
            <input type="text" class="form-control"
              value={this.state.fieldName} 
              onChange={(event)=>this.setState({fieldName:event.target.value})}/>
          </div>
        </div>

        <div class="form-group row">
          <label for="firstName" class="col-sm-2 col-form-label">Nombre de usuario</label>
          <div class="col-sm-10">
            <input type="text" class="form-control" 
              value={this.state.fieldUsername} 
              onChange={(event)=>this.setState({fieldUsername:event.target.value})}/>
          </div>
        </div>

				<div class="form-group row">
            <label for="firstName" class="col-sm-2 col-form-label">Email</label>
          <div class="col-sm-10">
            <input type="email" class="form-control" placeholder="email@email.com"  
              value={this.state.fieldEmail} 
              onChange={(event)=>this.setState({fieldEmail:event.target.value})}/>
          </div>
        </div>

				<div class="form-group row">
            <label for="firstName" class="col-sm-2 col-form-label">Fecha de nacimiento</label>
          <div class="col-sm-10">
            <input type="date" class="form-control" placeholder="dd/MM/yyyy"  
              value={this.state.fieldBirthDate} 
              onChange={(event)=>this.setState({fieldBirthDate:event.target.value})}/>
          </div>
        </div>

				<div class="form-group row">
            <label for="firstName" class="col-sm-2 col-form-label">Teléfono</label>
          <div class="col-sm-10">
            <input type="text" class="form-control" placeholder="123456789"  
              value={this.state.fieldPhone} 
              onChange={(event)=>this.setState({fieldPhone:event.target.value})}/>
          </div>
        </div>

        <div class="form-group row">
            <label for="firstName" class="col-sm-2 col-form-label">Contraseña</label>
          <div class="col-sm-10">
            <input type="password" class="form-control"
              value={this.state.fieldPassword} 
              onChange={(event)=>this.setState({fieldPassword: event.target.value})}/>
          </div>
        </div>

        <div class="form-group row">
            <label for="firstName" class="col-sm-2 col-form-label">Confirma contraseña</label>
          <div class="col-sm-10">
            <input type="password" class="form-control"
              value={this.state.fieldConfirmPassword} 
              onChange={(event)=>this.setState({fieldConfirmPassword: event.target.value})}/>
          </div>
        </div>

        <div class="form-group row">
          <label for="firstName" class="col-sm-2 col-form-label"/>
          <div class="col-sm-10">
          <input value={this.state.fieldCheckbok} 
          onChange={(event)=>this.setState({fieldCheckbok: this.state.fieldCheckbok==true ? false : true})} 
          class="form-check-input" type="checkbox"/> Acepto los términos y condiciones
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
    if(this.state.fieldPassword != this.state.fieldConfirmPassword){
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
        this.setState({errorField:dataError})
        })
      } else {
        const dataError = []
        dataError.push(res.message);
        this.setState({errorField:dataError});
      }
    }
	}
}


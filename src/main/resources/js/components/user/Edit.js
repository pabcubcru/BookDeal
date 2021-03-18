import React, { Component } from 'react';
import userService from "../services/User"

export default class Form extends Component {

  constructor(){
    super();
    this.state = {
        id:0,
        fieldName:"",
        fieldEmail:"",
        fieldPhone:"",
        fieldBirthDate:"",
        fieldUsername: "",
        fieldPassword: "",
        fieldConfirmPassword: "",
        errorFieldUser:[],
        errorFieldPassword:[],
        messageCorrectUser:"",
        messageCorrectPassword:""
    }
  }

  async componentDidMount() {
    const usern = await userService.getUsername()
    const username = usern.username
    const res = await userService.getUser(username)
    if(res.success){
        this.setState({
            id: res.user.id,
            fieldName: res.user.name,
            fieldEmail: res.user.email,
            fieldPhone: res.user.phone,
            fieldBirthDate: res.user.birthDate,
            fieldUsername: res.user.username,
        })
    } /*else {
        window.location.replace("/error")
    }*/
  }

  render() {
    return (
      
      <div style={{backgroundImage: "url(https://i.pinimg.com/originals/8d/23/06/8d2306b98839234e49ce96a8b76e20ae.jpg)", 
      backgroundSize: "cover" , padding: "50px", fontWeight: "bold", marginLeft: "-100"}}>
        <h1 style={{color: "#007bff"}}>Mi perfil</h1>
        <p class='text-danger'>*Obligatorio</p>
        <div class="form-group row">
          <label for="firstName" class="col-sm-2 col-form-label">Nombre y apellidos<sup class='text-danger'>*</sup></label>
          <div class="col-sm-10">
            <input type="text" class="form-control"
              value={this.state.fieldName} 
              onChange={(event)=>this.setState({fieldName:event.target.value})} required/>
          </div>
        </div>

        <div class="form-group row">
          <label for="firstName" class="col-sm-2 col-form-label">Nombre de usuario<sup class='text-danger'>*</sup></label>
          <div class="col-sm-10">
            <input type="text" class="form-control" 
              value={this.state.fieldUsername} 
              onChange={(event)=>this.setState({fieldUsername:event.target.value})} readOnly/>
          </div>
        </div>

				<div class="form-group row">
            <label for="firstName" class="col-sm-2 col-form-label">Email<sup class='text-danger'>*</sup></label>
          <div class="col-sm-10">
            <input type="email" class="form-control" placeholder="email@email.com"  
              value={this.state.fieldEmail} 
              onChange={(event)=>this.setState({fieldEmail:event.target.value})}/>
          </div>
        </div>

				<div class="form-group row">
            <label for="firstName" class="col-sm-2 col-form-label">Fecha de nacimiento<sup class='text-danger'>*</sup></label>
          <div class="col-sm-10">
            <input type="date" class="form-control" placeholder="dd/MM/yyyy"  
              value={this.state.fieldBirthDate} 
              onChange={(event)=>this.setState({fieldBirthDate:event.target.value})}/>
          </div>
        </div>

				<div class="form-group row">
            <label for="firstName" class="col-sm-2 col-form-label">Teléfono<sup class='text-danger'>*</sup></label>
          <div class="col-sm-10">
            <input type="text" class="form-control" placeholder="+34 123456789"  
              value={this.state.fieldPhone} 
              onChange={(event)=>this.setState({fieldPhone:event.target.value})}/>
          </div>
        </div>

        {
        this.state.errorFieldUser.map((itemerror) => {
            return(
              <p class='text-danger'>*{itemerror}</p>)
            })
        }

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
            <label for="firstName" class="col-sm-2 col-form-label">Contraseña</label>
          <div class="col-sm-10">
            <input type="password" class="form-control"  
              value={this.state.fieldPassword} 
              onChange={(event)=>this.setState({fieldPassword:event.target.value})}/>
          </div>
        </div>

        <div class="form-group row">
            <label for="firstName" class="col-sm-2 col-form-label">Confirma contraseña</label>
          <div class="col-sm-10">
            <input type="password" class="form-control"
              value={this.state.fieldConfirmPassword} 
              onChange={(event)=>this.setState({fieldConfirmPassword:event.target.value})}/>
          </div>
        </div>

        {
        this.state.errorFieldPassword.map((itemerror) => {
            return(
              <p class='text-danger'>*{itemerror}</p>)
            })
        }

        {<p style={{color: "#099C01"}}>{this.state.messageCorrectPassword}</p>}


        <div class="form-group row">
            <div class="col-sm-6" >
        <button onClick={()=>this.onClickSave()} class="btn btn-primary" type="submit">Cambiar</button>
            </div>
        </div>

      </div>
      
    )
  }

    async onClickSave() {
        const res = await userService.edit(this.state)
        if(this.state.fieldPassword != "" || this.state.fieldConfirmPassword != ""){
          if(this.state.fieldPassword.length < 8 || this.state.fieldPassword.length > 20) {
            const dataError = []
			      dataError.push("La contraseña debe contener entre 8 y 20 carácteres.");
			      this.setState({errorFieldPassword:dataError});
          } else if(this.state.fieldPassword != this.state.fieldConfirmPassword) {
            const dataError = []
            dataError.push("Las contraseñas no coinciden.");
            this.setState({errorFieldPassword:dataError});
          } else if(res.success) {
            this.setState({messageCorrectPassword: "La contraseña se ha cambiado con éxito.", 
            errorFieldPassword:[], messageCorrectUser:"", fieldPassword:"", fieldConfirmPassword:""})
            //window.location.replace("/profile")
          }
        } else if (res.success) {
            this.setState({messageCorrectUser: "Los datos se han modificado con éxito.", messageCorrectPassword:""})
            //window.location.replace("/profile")
        } else if (res.status==400) {
            const dataError = []
            const error = res.data.errors
            error.map((itemerror)=>{
            dataError.push(itemerror.defaultMessage)})
            this.setState({errorFieldUser:dataError})
        } else {
            const dataError = []
            dataError.push(res.message);
            this.setState({errorFieldUser:dataError});
        }
	}
}


import React, { Component } from 'react';
import bookService from "../services/Book";
import { Link } from "react-router-dom";

export default class Form extends Component {

  constructor(){
    super();
    this.state = {
      fieldTitle:"",
      fieldOriginalTitle:"",
      fieldIsbn:"",
      fieldPublicationYear:"",
      fieldPublisher: "",
      fieldGenres:"",
      fieldAuthor:"",
      fieldDescription:"",
      fieldImage: "",
      fieldAction:"",
      fieldPrice: "",
      errorField:[]
    }
  }

  render() {
    return (
      
      <div style={{backgroundImage: "url(https://i.pinimg.com/originals/8d/23/06/8d2306b98839234e49ce96a8b76e20ae.jpg)", 
      backgroundSize: "cover" , padding: "50px", fontWeight: "bold", marginLeft: "-100"}}>
        <h1 style={{color: "#007bff"}}>Añadir libro</h1>
        <p class='text-danger'>*Obligatorio</p>
        <div class="form-group row">
          <label for="firstName" class="col-sm-3 col-form-label">Título<sup class='text-danger'>*</sup></label>
          <div class="col-sm-9">
            <input type="text" class="form-control"
              value={this.state.fieldTitle} 
              onChange={(event)=>this.setState({fieldTitle:event.target.value})}/>
          </div>
        </div>

        <div class="form-group row">
          <label for="firstName" class="col-sm-3 col-form-label">Título original</label>
          <div class="col-sm-9">
            <input type="text" class="form-control" 
              value={this.state.fieldOriginalTitle} 
              onChange={(event)=>this.setState({fieldOriginalTitle:event.target.value})}/>
          </div>
        </div>

				<div class="form-group row">
            <label for="firstName" class="col-sm-3 col-form-label">ISBN<sup class='text-danger'>*</sup></label>
          <div class="col-sm-9">
            <input type="email" class="form-control" 
              value={this.state.fieldIsbn} 
              onChange={(event)=>this.setState({fieldIsbn:event.target.value})}/>
          </div>
        </div>

				<div class="form-group row">
            <label for="firstName" class="col-sm-3 col-form-label">Año de publicación<sup class='text-danger'>*</sup></label>
          <div class="col-sm-9">
            <input type="number" class="form-control" placeholder="yyyy"  
              value={this.state.fieldPublicationYear} 
              onChange={(event)=>this.setState({fieldPublicationYear:event.target.value})}/>
          </div>
        </div>

				<div class="form-group row">
            <label for="firstName" class="col-sm-3 col-form-label">Editorial<sup class='text-danger'>*</sup></label>
          <div class="col-sm-9">
            <input type="text" class="form-control"  
              value={this.state.fieldPublisher} 
              onChange={(event)=>this.setState({fieldPublisher:event.target.value})}/>
          </div>
        </div>

        <div class="form-group row">
            <label for="firstName" class="col-sm-3 col-form-label">Generos<sup class='text-danger'>*</sup></label>
          <div class="col-sm-9">
          <input type="text" class="form-control"  
              value={this.state.fieldGenres} 
              onChange={(event)=>this.setState({fieldGenres:event.target.value})}/>
          </div>
        </div>

        <div class="form-group row">
            <label for="firstName" class="col-sm-3 col-form-label">Autor<sup class='text-danger'>*</sup></label>
          <div class="col-sm-9">
            <input type="text" class="form-control"
              value={this.state.fieldAuthor} 
              onChange={(event)=>this.setState({fieldAuthor:event.target.value})}/>
          </div>
        </div>

        <div class="form-group row">
            <label for="firstName" class="col-sm-3 col-form-label">Descripción<sup class='text-danger'>*</sup></label>
          <div class="col-sm-9">
            <input type="text" class="form-control"
              value={this.state.fieldDescription} 
              onChange={(event)=>this.setState({fieldDescription:event.target.value})}/>
          </div>
        </div>

        <div class="form-group row">
            <label for="firstName" class="col-sm-3 col-form-label">Imagen<sup class='text-danger'>*</sup></label>
          <div class="col-sm-9">
            <input type="file" class="form-control"
              value={this.state.fieldImage} 
              onChange={(event)=>this.setState({fieldImage: event.target.value})}/>
          </div>
        </div>

        <div class="form-group row">
            <label for="firstName" class="col-sm-3 col-form-label">¿Qué quiere hacer?<sup class='text-danger'>*</sup></label>
          <div class="col-sm-9">
          <select class="form-control" id="selectAction" onChange={(event) => this.setState({fieldAction:event.target.value})}>
            <option value="VENDER">VENDER</option>
            <option value="INTERCAMBIAR">INTERCAMBIAR</option>
            </select>
          </div>
        </div>

        <div class="form-group row">
            <label for="firstName" class="col-sm-3 col-form-label">Precio</label>
          <div class="col-sm-9">
            <input type="text" class="form-control"
              value={this.state.fieldPrice} 
              onChange={(event)=>this.setState({fieldPrice:event.target.value})}/>
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
		      	<button onClick={()=>this.onClickSave()} class="btn btn-primary" type="submit">Crear</button>
					</div>
				</div>
      </div>
      
    )
  }

  async onClickSave() {
		const res = await bookService.create(this.state)

      if (res.success) {
        window.location.replace("/")
      }
    }
}


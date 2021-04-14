import React, { Component } from 'react';
import bookService from "../services/Book";

export default class Form extends Component {

  constructor(){
    super();
    this.state = {
      id:0,
      fieldTitle:"",
      fieldOriginalTitle:"",
      fieldIsbn:"",
      fieldPublicationYear:"",
      fieldPublisher: "",
      fieldGenres:"",
      fieldAuthor:"",
      fieldDescription:"",
      fieldImage: "",
      fieldStatus: "",
      fieldAction:"",
      fieldPrice: "",
      errorField:[],
      genres:[],
      fieldGen:[],
      alreadyRequest:false
    }
  }

  async componentDidMount() {
    const genres = await bookService.getGenres()
    const res = genres.genres

    const id = this.props.match.params.id;
    const b = await bookService.getBookToEdit(id)
    const genrs = b.book.genres.split(",")

    if(b.success) {
      this.setState({
        genres: res,
        id: b.book.id,
        fieldTitle: b.book.title,
        fieldOriginalTitle: b.book.originalTitle,
        fieldIsbn: b.book.isbn,
        fieldPublicationYear: b.book.publicationYear,
        fieldPublisher: b.book.publisher,
        fieldGenres: b.book.genres,
        fieldAuthor: b.book.author,
        fieldDescription: b.book.description,
        fieldImage: b.book.image,
        fieldStatus: b.book.status,
        fieldAction: b.book.action,
        fieldPrice: b.book.price,
        fieldGen: genrs,
        correctBook:"",
        alreadyRequest:b.alreadyRequest
      })
    }
  }

  render() {
    return (
      
      <div style={{backgroundImage: "url(https://i.pinimg.com/originals/8d/23/06/8d2306b98839234e49ce96a8b76e20ae.jpg)", 
      backgroundSize: "cover" , padding: "50px", fontWeight: "bold", marginLeft: "-100"}}>
        <h1 style={{color: "#007bff"}}>Editar {this.state.fieldTitle}</h1>
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
            <input type="number" class="form-control" max="2021"
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
          <select class="form-control chosen-select" id="selectGenres" value={this.state.fieldGen} onClick={(event) => this.editGenres(event.target.value)} multiple>
            {this.state.genres.map((genre) => {
              return (
                <option value={genre}>{genre.replaceAll("_", " ")}</option>
              )
            })}
            </select>
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
            <label for="firstName" class="col-sm-3 col-form-label">Sinopsis<sup class='text-danger'>*</sup></label>
          <div class="col-sm-9">
            <textarea type="text" class="form-control"
              value={this.state.fieldDescription} 
              onChange={(event)=>this.setState({fieldDescription:event.target.value})}/>
          </div>
        </div>

        <div class="form-group row">
            <label for="firstName" class="col-sm-3 col-form-label"> URL de imagen<sup class='text-danger'>*</sup></label>
          <div class="col-sm-9">
            <input type="url" class="form-control"
              value={this.state.fieldImage} 
              onChange={(event)=>this.setState({fieldImage: event.target.value})} multiple/>
          </div>
        </div>

        <div class="form-group row">
            <label for="firstName" class="col-sm-3 col-form-label">Estado<sup class='text-danger'>*</sup></label>
          <div class="col-sm-9">
          <select class="form-control" id="selectStatus" value={this.state.fieldStatus} onChange={(event) => this.setState({fieldStatus:event.target.value})}>
            <option value="NUEVO">NUEVO</option>
            <option value="POCO USADO">POCO USADO</option>
            <option value="NORMAL">NORMAL</option>
            <option value="MUY USADO">MUY USADO</option>
            <option value="DAÑADO">DAÑADO</option>
            <option value="MUY DAÑADO">MUY DAÑADO</option>
          </select>
          </div>
        </div>

        <div class="form-group row">
            <label for="firstName" class="col-sm-3 col-form-label">¿Qué quiere hacer?<sup class='text-danger'>*</sup></label>
          <div class="col-sm-9">
          <select class="form-control" id="selectAction" value={this.state.fieldAction} onChange={(event) => this.setState({fieldAction:event.target.value})} disabled={this.state.alreadyRequest}>
            <option value="INTERCAMBIO">INTERCAMBIO</option>
            <option value="VENTA">VENTA</option>
          </select>
          {this.state.alreadyRequest ?
            <p class='text-danger'>*Tiene una petición en proceso.</p>
          :
            <p></p>
          }
          </div>
        </div>

        <div class="form-group row" >
          <label for="firstName" class="col-sm-3 col-form-label">Precio</label> 
          <div class="col-sm-9">
            <input id="price" type="number" class="form-control"
              value={this.state.fieldPrice} 
              onChange={(event)=> this.setState({fieldPrice:event.target.value})}/>
          </div>
        </div>
                                   
                    

        {
          this.state.errorField.map((itemerror) => {
            return(
              <p class='text-danger'>*{itemerror}</p>
            )
          })
        }

        {<p style={{color: "#099C01"}}>{this.state.correctBook}</p>}

				<div class="form-group row">
					<div class="col-sm-6" >
		      	<button onClick={()=>this.onClickSave()} class="btn btn-primary" type="submit">Actualizar</button>
					</div>
				</div>
      </div>
      
    )
  }

  async onClickSave() {
		const res = await bookService.edit(this.state)

    if (this.state.fieldAction == "VENTA" && this.state.fieldPrice == null) {
      this.setState({correctBook:""})
      const dataError = []
      dataError.push("El precio es un campo requerido.");
      this.setState({errorField:dataError});
    } else {
      if (res.success) {
        this.setState({correctBook:"*Se ha actualizado correctamente.", errorField:[]})
        //window.location.replace("/")
      } else if(res.status == 400) {
        this.setState({correctBook:""})
        const dataError = []
        const error = res.data.errors
        error.map((itemerror)=>{
        dataError.push(itemerror.defaultMessage)
        })
        this.setState({errorField:dataError}) 
      }
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


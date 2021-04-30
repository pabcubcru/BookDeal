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
      errorMessages:[],
      genres:[],
      fieldGen:[],
      titleCopy:""
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
        titleCopy:b.book.title
      })
    }
  }

  render() {
    return (
      
      <div style={{backgroundImage: "url(https://i.pinimg.com/originals/8d/23/06/8d2306b98839234e49ce96a8b76e20ae.jpg)", 
      backgroundSize: "cover" , padding: "50px", fontWeight: "bold", borderRadius: '5px', marginLeft: "-100"}}>
        <h1 style={{color: "#007bff"}}>Editar {this.state.titleCopy}</h1>
        <p class='text-danger'>*Obligatorio</p>
        <div class="form-group row">
          <label for="firstName" class="col-sm-3 col-form-label">Título<sup class='text-danger'>*</sup></label>
          <div class="col-sm-9">
            <input type="text" class="form-control"
              value={this.state.fieldTitle} 
              onChange={(event)=>this.setState({fieldTitle:event.target.value})}/>
              {this.state.errorField.indexOf("title") != -1 ? 
                <p class='text-danger'>{this.state.errorMessages[this.state.errorField.indexOf("title")]}</p>
              :
                <p></p>
              }
          </div>
        </div>

        <div class="form-group row">
          <label for="firstName" class="col-sm-3 col-form-label">Título original</label>
          <div class="col-sm-9">
            <input type="text" class="form-control" 
              value={this.state.fieldOriginalTitle} 
              onChange={(event)=>this.setState({fieldOriginalTitle:event.target.value})}/>
              {this.state.errorField.indexOf("originalTitle") != -1 ? 
                <p class='text-danger'>{this.state.errorMessages[this.state.errorField.indexOf("originalTitle")]}</p>
              :
                <p></p>
              }
          </div>
        </div>

				<div class="form-group row">
            <label for="firstName" class="col-sm-3 col-form-label">ISBN<sup class='text-danger'>*</sup></label>
          <div class="col-sm-9">
            <input type="text" class="form-control" 
              value={this.state.fieldIsbn} placeholder="0-123-45678-9 ó 012-3-456-78901-2"
              onChange={(event)=>this.setState({fieldIsbn:event.target.value.replace(" ", "-").replace("--", "-")})}/>
              {this.state.errorField.indexOf("isbn") != -1 ? 
                <p class='text-danger'>{this.state.errorMessages[this.state.errorField.indexOf("isbn")]}</p>
              :
                <p></p>
              }
          </div>
        </div>

				<div class="form-group row">
            <label for="firstName" class="col-sm-3 col-form-label">Año de publicación<sup class='text-danger'>*</sup></label>
          <div class="col-sm-9">
            <input type="number" class="form-control"
              value={this.state.fieldPublicationYear} 
              onChange={(event)=>this.setState({fieldPublicationYear:event.target.value})}/>
              {this.state.errorField.indexOf("publicationYear") != -1 ? 
                <p class='text-danger'>{this.state.errorMessages[this.state.errorField.indexOf("publicationYear")]}</p>
              :
                <p></p>
              }
          </div>
        </div>

				<div class="form-group row">
            <label for="firstName" class="col-sm-3 col-form-label">Editorial<sup class='text-danger'>*</sup></label>
          <div class="col-sm-9">
            <input type="text" class="form-control"  
              value={this.state.fieldPublisher} 
              onChange={(event)=>this.setState({fieldPublisher:event.target.value})}/>
              {this.state.errorField.indexOf("publisher") != -1 ? 
                <p class='text-danger'>{this.state.errorMessages[this.state.errorField.indexOf("publisher")]}</p>
              :
                <p></p>
              }
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
            {this.state.errorField.indexOf("genres") != -1 ? 
                <p class='text-danger'>{this.state.errorMessages[this.state.errorField.indexOf("genres")]}</p>
              :
                <p></p>
              }
          </div>
        </div>

        <div class="form-group row">
            <label for="firstName" class="col-sm-3 col-form-label">Autor<sup class='text-danger'>*</sup></label>
          <div class="col-sm-9">
            <input type="text" class="form-control"
              value={this.state.fieldAuthor} 
              onChange={(event)=>this.setState({fieldAuthor:event.target.value})}/>
              {this.state.errorField.indexOf("author") != -1 ? 
                <p class='text-danger'>{this.state.errorMessages[this.state.errorField.indexOf("author")]}</p>
              :
                <p></p>
              }
          </div>
        </div>

        <div class="form-group row">
            <label for="firstName" class="col-sm-3 col-form-label">Sinopsis<sup class='text-danger'>*</sup></label>
          <div class="col-sm-9">
            <textarea type="text" class="form-control"
              value={this.state.fieldDescription} 
              onChange={(event)=>this.setState({fieldDescription:event.target.value})}/>
              {this.state.errorField.indexOf("description") != -1 ? 
                <p class='text-danger'>{this.state.errorMessages[this.state.errorField.indexOf("description")]}</p>
              :
                <p></p>
              }
          </div>
        </div>

        <div class="form-group row">
            <label for="firstName" class="col-sm-3 col-form-label"> URL de imagen<sup class='text-danger'>*</sup></label>
          <div class="col-sm-9">
            <input type="url" class="form-control"
              value={this.state.fieldImage} 
              onChange={(event)=>this.setState({fieldImage: event.target.value})}/>
              {this.state.errorField.indexOf("image") != -1 ? 
                <p class='text-danger'>{this.state.errorMessages[this.state.errorField.indexOf("image")]}</p>
              :
                <p></p>
              }
          </div>
        </div>

        <div class="form-group row">
            <label for="firstName" class="col-sm-3 col-form-label">Estado<sup class='text-danger'>*</sup></label>
          <div class="col-sm-9">
          <select class="form-control" id="selectStatus" value={this.state.fieldStatus} onChange={(event) => this.setState({fieldStatus:event.target.value})}>
            <option value="NUEVO">NUEVO</option>
            <option value="POCO USADO">POCO USADO</option>
            <option value="NORMAL">BUEN ESTADO</option>
            <option value="MUY USADO">MUY USADO</option>
            <option value="DAÑADO">DAÑADO</option>
            <option value="MUY DAÑADO">MUY DAÑADO</option>
          </select>
          {this.state.errorField.indexOf("status") != -1 ? 
                <p class='text-danger'>{this.state.errorMessages[this.state.errorField.indexOf("status")]}</p>
              :
                <p></p>
              }
          </div>
        </div>
         
        <div class="form-group row" >
        <label for="firstName" class="col-sm-3 col-form-label">Precio<sup class='text-danger'>*</sup></label> 
        <div class="col-sm-9">
          <input id="price" type="number" class="form-control"
            value={this.state.fieldPrice} 
            onChange={(event)=> this.setState({fieldPrice:event.target.value})} />
            {this.state.errorField.indexOf("price") != -1 ? 
              <p class='text-danger'>{this.state.errorMessages[this.state.errorField.indexOf("price")]}</p>
            :
              <p></p>
            }
        </div>
      </div>

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

      if (res.success) {
        window.location.replace("/books/me/0")
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


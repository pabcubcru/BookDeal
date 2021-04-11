import React, { Component } from 'react';
import bookService from "../services/Book";
import requestService from "../services/Request";

export default class Form extends Component {

  constructor(){
    super();
    this.state = {
      fieldIdBook1:"",
      fieldComment:"",
      fieldIdBook2:"",
      book:"",
      books:[],
      errorField:[],
      noBooks:true
    }
  }

  async componentDidMount() {
    const id = this.props.match.params.id;

    const b = await bookService.getBook(id)

    const bk = await bookService.listMyBooksForChange()

    this.setState({book:b.book, books:bk.books, fieldIdBook2:id})

    if(bk.books.length >= 1) {
      this.setState({fieldIdBook1:bk.books[0].id, noBooks:false})
    }
  }

  render() {
    return (
      
      <div style={{backgroundImage: "url(https://i.pinimg.com/originals/8d/23/06/8d2306b98839234e49ce96a8b76e20ae.jpg)", 
      backgroundSize: "cover" , padding: "50px", fontWeight: "bold", marginLeft: "-100"}}>
        <h3 style={{color: "#007bff"}}>Petición de {this.state.book.action == "INTERCAMBIO" ? "INTERCAMBIO" : "COMPRA por " + this.state.book.price + "€"} para {this.state.book.title}</h3>
        <p class='text-danger'>*Obligatorio</p>

        {this.state.book.action == "INTERCAMBIO" ?
            <div class="form-group row">
                <label for="firstName" class="col-sm-3 col-form-label">Seleccione su libro<sup class='text-danger'>*</sup></label>
                <div class="col-sm-9">
                    <select class="form-control chosen-select" id="selectBook" value={this.state.fieldIdBook1} onChange={(event) => this.setState({fieldIdBook1:event.target.value})} 
                    disabled={this.state.noBooks && this.state.book.action == "INTERCAMBIO"}> 
                        {this.state.books.map((book) => {
                        return (
                            <option value={book.id}>{book.title}</option>
                        )
                        })}
                    </select>
                </div>
            </div>
        :
            <p></p>
        }

        <div class="form-group row">
          <label for="firstName" class="col-sm-3 col-form-label">Comentario adicional</label>
          <div class="col-sm-9">
            <input type="text" class="form-control"
              value={this.state.fieldComment} 
              onChange={(event)=>this.setState({fieldComment:event.target.value})} disabled={this.state.noBooks && this.state.book.action == "INTERCAMBIO"}/>
          </div>
        </div>
                                    
        {
          this.state.errorField.map((itemerror) => {
            return(
              <p class='text-danger'>*{itemerror}</p>
            )
          })
        }

        {this.state.noBooks && this.state.book.action == "INTERCAMBIO" ?
          <p class='text-danger'>*No tiene libros para intercambiar. <a href="/books/new" class="btn btn-primary">Sube uno ahora!</a></p>
        :
          <p></p>
        }

        <div class="form-group row">
            <div class="col-sm-6" >
        <button onClick={()=>this.onClickSave()} class="btn btn-primary" style={{float:"right"}} type="submit" disabled={this.state.noBooks && this.state.book.action == "INTERCAMBIO"}>Enviar</button>
            </div>
        </div>
      </div>
      
    )
  }

  async onClickSave() {
	  const res = await requestService.create(this.state)

    if (this.state.book.action == "INTERCAMBIO" && this.state.fieldIdBook1 == "") {
      const dataError = []
      dataError.push("El libro a intercambiar es un campo requerido.");
      this.setState({errorField:dataError});
    } else {
      if (res.success) {
        window.location.replace("/")
      }
    }
  }
}


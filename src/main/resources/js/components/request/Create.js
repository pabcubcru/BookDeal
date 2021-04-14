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
      fieldPay:"",
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

    if(b.book.action == "VENTA") {
      this.setState({fieldPay:b.book.price})
    }

    if(bk.books.length >= 1) {
      this.setState({fieldIdBook1:bk.books[0].id, noBooks:false})
    }
  }

  render() {
    return (
      
      <div style={{backgroundImage: "url(https://i.pinimg.com/originals/8d/23/06/8d2306b98839234e49ce96a8b76e20ae.jpg)", 
      backgroundSize: "cover" , padding: "50px", fontWeight: "bold", marginLeft: "-100"}}>
        <h3 style={{color: "#007bff"}}>Petición de {this.state.book.action == "INTERCAMBIO" ? "INTERCAMBIO" : "COMPRA (" + this.state.book.price + "€)"} para {this.state.book.title}</h3>
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
        <div class="form-group row">
          <label for="firstName" class="col-sm-3 col-form-label">¿Cuánto pagaría?<sup class='text-danger'>*</sup></label>
          <div class="col-sm-9">
            <input type="number" class="form-control"
              value={this.state.fieldPay} 
              onChange={(event)=>this.setState({fieldPay:event.target.value})}/>
          </div>
        </div>
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
              <br></br>
        <button onClick={()=>this.onClickSave()} class="btn btn-primary" style={{float:"left"}} type="submit" disabled={this.state.noBooks && this.state.book.action == "INTERCAMBIO"}>Enviar</button>
            </div>
        </div>
      </div>
      
    )
  }

  async onClickSave() {
	  if (this.state.book.action == "INTERCAMBIO" && this.state.fieldIdBook1 == "") {
      const dataError = []
      dataError.push("El libro a intercambiar es un campo requerido.");
      this.setState({errorField:dataError});
    } else if(this.state.book.action == "VENTA" && this.state.fieldPay == ""){
      const dataError = []
      dataError.push("El precio que estaría dispuesto a pagar es un campo requerido.");
      this.setState({errorField:dataError});
    } else {
      const res = await requestService.create(this.state)
      if (res.success) {
        window.location.replace("/books/"+this.state.book.id)
      }
    }
  }
}


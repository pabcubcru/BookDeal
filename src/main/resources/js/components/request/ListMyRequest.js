import React, { Component } from 'react';
import requestService from "../services/Request";
import userService from "../services/User";

export default class List extends Component {

  constructor() {
    super();
    this.state = {
      requests: [],
      books1:[],
      books2:[],
      users:[]
    }
  }
    
  async componentDidMount() {
    const res = await requestService.listMyRequests()

    this.setState({requests:res.requests, books1:res.books1, books2:res.books2, users:res.users})

  }

  async getUser(username) {
    const user = await userService.getUser(username)
    return user.user
  }

    render() {
        return (
            <div>
                {this.state.requests.map((request, i) => {
                    return(
                    <div style={{backgroundImage: "url(https://i.pinimg.com/originals/8d/23/06/8d2306b98839234e49ce96a8b76e20ae.jpg)", 
                    backgroundSize: "auto auto", padding: "30px", paddingTop:"20px", marginBlock:"30px", margin:"0px 20px 20px 0px", width: '510px', height: '460px', display:"inline-grid"}}>

                    {request.action == "INTERCAMBIO" ?
                      <center><h5><strong>INTERCAMBIO: {this.state.books1[i].title} por {this.state.books2[i].title}</strong></h5></center>
                    :
                      <center><h5><strong>COMPRA: {this.state.books2[i].title}</strong></h5></center>
                    }

                    {request.action == "INTERCAMBIO" ?
                      <center><a style={{color:"black"}} href={"/books/"+this.state.books1[i].id}><img style={{height:"100px", width:"100px"}} src={this.state.books1[i].image} 
                      style={{padding: '10px', margin:"0px 0px 0px 0px", width: '120px'}}></img><strong>  ⇄  </strong></a><a href={"/books/"+this.state.books2[i].id}><img style={{height:"100px", width:"100px"}} src={this.state.books2[i].image} 
                      style={{padding: '10px', margin:"0px 0px 0px 0px", width: '120px'}}></img></a><br></br></center>
                    :
                    <center><a href={"/books/"+this.state.books2[i].id}><img style={{height:"100px", width:"100px"}} src={this.state.books2[i].image} 
                    style={{padding: '10px', margin:"0px 0px 0px 0px", width: '120px'}}></img></a>
                    <br></br>
                    <p><strong>Precio: </strong>{this.state.books2[i].price} €</p>  
                    </center>
                    }
                    <center>
                    <p><strong>Comentario adicional: </strong>{request.comment}</p>
                    {request.status != "ACEPTADA" ?
                      <p><strong>Petición enviada a: </strong>{request.username2}</p>
                    :
                      <p></p>
                    }
                    
                    <p><strong>Estado: </strong>{request.status}</p>
                    
                    </center>
                    
                    {request.status == "PENDIENTE" ? 
                      <center><a onClick={() => this.cancelRequest(request.id)} style={{background:"red", color:"white"}} class="btn btn-primary"><strong>Cancelar</strong></a></center>
                    :
                      request.status != "ACEPTADA" ?
                        <center><a onClick={() => this.deleteRequest(request.id)} style={{background:"red", color:"white"}} class="btn btn-primary"><strong>Eliminar</strong></a></center>
                      :
                        <center><p><strong><i><u>Datos de contacto con {request.username2}</u></i></strong></p>
                        <p><strong>Número de teléfono: </strong>{this.state.users[i].phone}</p>
                        <p><strong> Email: </strong>{this.state.users[i].email}</p></center>
                    }
                    </div>)
                })}

                {this.state.requests.length == 0 ?
                <p><strong>¿No tienes todavía peticiones? <a href="/" class="btn btn-primary">Realiza una!</a></strong></p>
                :
                <p></p>}
            </div>
          );
    }

    async cancelRequest(id) {
      const conf = confirm("¿Está seguro de que quiere cancelarla? Esta acción no es reversible.")
      if(conf) {
        const res = await requestService.cancel(id)
        window.location.replace("/requests/me")
      }
    }

    async deleteRequest(id) {
      const conf = confirm("¿Está seguro de que quiere eliminarla? Esta acción no es reversible.")
      if(conf) {
        const res = await requestService.delete(id)
        window.location.replace("/requests/me")
      }
    }

}

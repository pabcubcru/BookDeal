import React, { Component } from 'react';
import requestService from "../services/Request";

export default class List extends Component {

  constructor(){
    super();
    this.state = {
      requests: [],
      books1:[],
      books2:[]
    }
  }
    
  async componentDidMount() {
    const res = await requestService.listReceivedRequests()

    this.setState({requests:res.requests, books1:res.books1, books2:res.books2})
  }

    render() {
        return (
            <div>
                {this.state.requests.map((request, i) => {
                    return(
                    <div style={{backgroundImage: "url(https://i.pinimg.com/originals/8d/23/06/8d2306b98839234e49ce96a8b76e20ae.jpg)", 
                    backgroundSize: "auto auto", padding: "30px", paddingTop:"20px", marginBlock:"30px", margin:"0px 20px 20px 0px", width: '510px', height: '450px', display:"inline-grid"}}>

                    {request.action == "INTERCAMBIO" ?
                      <center><h5><strong>INTERCAMBIO: {this.state.books2[i].title} por {this.state.books1[i].title}</strong></h5></center>
                    :
                      <center><h5><strong>COMPRA: {this.state.books2[i].title}</strong></h5></center>
                    }

                    {request.action == "INTERCAMBIO" ?
                      <center><a style={{color:"black"}} href={"/books/"+this.state.books2[i].id}><img style={{height:"100px", width:"100px"}} src={this.state.books2[i].image} 
                      style={{padding: '10px', margin:"0px 0px 0px 0px", width: '120px'}}></img><strong>  ⇄  </strong></a><a href={"/books/"+this.state.books1[i].id}><img style={{height:"100px", width:"100px"}} src={this.state.books1[i].image} 
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
                    <p><strong>Petición enviada por: </strong>{request.username1}</p>
                    <p><strong>Estado: </strong>{request.status}</p>
                    
                    </center>
                    
                    {request.status == "PENDIENTE" ? 
                      <center><a onClick={() => this.acceptRequest(request.id, this.state.books2[i].title)} style={{background:"#099C01",color:"white", margin:"10px"}} class="btn btn-primary"><strong>Aceptar</strong></a>
                      <a onClick={() => this.rejectRequest(request.id)} style={{background:"red", color:"white"}} class="btn btn-primary"><strong>Rechazar</strong></a></center>
                    :
                      <p></p>
                    }
                    </div>)
                })}

                {this.state.requests.length == 0 ?
                <p><strong>¿No has recibido peticiones todavía? <a href="/books/new" class="btn btn-primary">Sube un libro!</a></strong></p>
                :
                <p></p>}
            </div>
          );
    }

    async acceptRequest(id, title) {
      const conf = confirm("¿Está seguro de que quiere aceptarla? Las otras peticiones a " + title + " se rechazarán.")
      if(conf) {
        const res = await requestService.accept(id)
        window.location.replace("/requests/received")
      }
    }

    async rejectRequest(id) {
      const conf = confirm("¿Está seguro de que quiere rechazarla? Esta acción no es reversible.")
      if(conf) {
        const res = await requestService.reject(id)
        window.location.replace("/requests/received")
      }
    }
}

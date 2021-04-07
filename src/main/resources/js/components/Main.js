import React, { Component } from 'react';
import ReactDOM from 'react-dom';

import Nav from "./Nav"
import RegisterUser from "./user/Register";
import LoginUser from "./user/Login";
import LoginerrorUser from "./user/Login-error";
import EditUser from "./user/Edit";
import Home from "./Home";
import CreateBook from "./book/Create";
import ListAllBooks from "./book/ListAllBooks";
import ListMyBooks from "./book/ListMyBooks";
import EditBook from "./book/Edit";
import GetBook from "./book/Get";
import ListFavouritesBooks from "./userFavouriteBook/ListFavouritesBooks";

import {
    BrowserRouter as Router,
    Switch,
    Route
} from 'react-router-dom';

export default class Main extends Component{
    render(){
        return(
            <Router>
                <main>
                    <Nav/>
                    <Switch>
                        <Route path="/register" exact component={RegisterUser}/>
                        <Route path="/login" exact component={LoginUser}/>
                        <Route path="/login-error" exact component={LoginerrorUser}/>
                        <Route path="/profile" exact component={EditUser}/>
                        <Route path="/books/new" exact component={CreateBook}/>
                        <Route path="/" exact component={ListAllBooks}/>
                        <Route path="/books/me" exact component={ListMyBooks}/>
                        <Route path="/books/:id/edit" exact component={EditBook}/>
                        <Route path="/books/:id" exact component={GetBook}/>
                        <Route path="/favourites" exact component={ListFavouritesBooks}/>
                    </Switch>
                </main>
            </Router>
        )
    }
}

ReactDOM.render(<Main/>, document.getElementById("main-page"));
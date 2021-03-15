import React, { Component } from 'react';
import ReactDOM from 'react-dom';

import Nav from "./Nav"
import Register from "./user/Register";
import Login from "./user/Login";
import Loginerror from "./user/Login-error";

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
                        <Route path="/register" exact component={Register}/>
                        <Route path="/login" exact component={Login}/>
                        <Route path="/login-error" exact component={Loginerror}/>
                    </Switch>
                </main>
            </Router>
        )
    }
}

ReactDOM.render(<Main/>, document.getElementById("main-page"));
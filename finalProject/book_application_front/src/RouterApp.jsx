import React from 'react';
import Book from './Book'
import {BrowserRouter as Router, Route, Switch} from 'react-router-dom'
import BookDetail from './BookDetail'
import NotFound from './errors/NotFound';
import ErrorHandler from './errors/ErrorHandler';
import User from "./User";
import ReadBook from "./ReadBook";
import AuthorDetail from "./AuthorDetail";
import Author from "./Author";

export default class RouterApp extends React.Component {
    render() {
        return (
            <Router>
                <ErrorHandler>
                    <>
                        <Switch>
                            <Route path='/' exact component={Book}/>
                            <Route path="/book" exact component={Book}/>
                            <Route path="/user" exact component={User}/>
                            <Route path="/book/:id" component={BookDetail}/>
                            <Route path="/author/:id" component={AuthorDetail}/>
                            <Route path="/author" component={Author}/>
                            <Route path='/404' component={NotFound} />
                            <Route path='/read/:id' exact component={ReadBook}/>
                            <Route component={NotFound}/>
                        </Switch>
                    </>
                </ErrorHandler>
            </Router>
        )
    }
};
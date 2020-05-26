import React from 'react';
import Book from './Book'
import {BrowserRouter as Router, Route, Switch} from 'react-router-dom'
import BookDetail from './BookDetail'
import NotFound from './errors/NotFound';
import ErrorHandler from './errors/ErrorHandler';

export default class RouterApp extends React.Component {
    render() {
        return (
            <Router>
                <ErrorHandler>
                    <>
                        <Switch>
                            <Route path='/' exact component={Book}/>
                            <Route path="/book" exact component={Book}/>
                            <Route path="/book/:id" component={BookDetail}/>
                            <Route path='/404' component={NotFound} />
                            <Route component={NotFound}/>
                        </Switch>
                    </>
                </ErrorHandler>
            </Router>
        )
    }
};
import React from 'react';
import ReactDOM from 'react-dom';
import {App} from "./App";
import {Provider} from 'react-redux';
import {applyMiddleware, createStore} from "redux";
import thunk from "redux-thunk";

const store = createStore(
    (state = {}) => state,
    applyMiddleware(thunk)
);

ReactDOM.render(
    <Provider store={store}>
        <App/>
    </Provider>, document.getElementById('root'));

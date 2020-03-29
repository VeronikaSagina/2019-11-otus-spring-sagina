import '../css/book.css'
import '../css/books.css'
import React from "react";
import {Link} from 'react-router-dom';

export default class NotFound extends React.Component {
    render() {
        return (
                <div className="book_text">
                    <h3>404 Страница не найдена</h3>
                    <div>
                        <Link className="input_for_edit book_button" to="/book">вернуться к списку книг</Link>
                    </div>
            </div>
        );
    }
};

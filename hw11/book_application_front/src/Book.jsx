import React from 'react'
import axios from 'axios';
import BookModal from "./BookModal";
import "./css/style.css"
import "./css/books.css"

export default class Book extends React.Component {
    constructor(props) {
        super(props);
        this.state = {books: []};
    }

    showBookDetail(book) {
        this.getBookById(book.id)
            .then(book => this.props.history.push(`/book/${book.id}`, {book}));
    }

    getBookById(id) {
        return axios.get(`/book/${id}`)
            .then(promise => promise.data)
            .catch(message => console.log(message));
    }

    componentDidMount() {
        fetch(`/book`)
            .then(response => response.json())
            .then(books => this.setState({books}))
            .catch(message => console.log(message));
    }

    deleteBookClicked(id) {
        this.deleteBook(id)
            .then(() => this.showBooks());
    }

    deleteBook(id) {
        return axios.delete(`/book/${id}/delete`)
            .catch(message => console.log(message));
    }

    showBooks = () => {
        axios.get(`/book`)
            .then(promise => promise.data)
            .then(books => this.setState({books}))
            .catch(message => console.log(message));
    };

    render() {
        return (
            <React.Fragment>
                <div className="book_background">
                    <div><h3>Список книг</h3></div>
                    <div className="table_area">
                        <table>
                            <thead>
                            <tr>
                                <th>Название</th>
                                <th>Автор</th>
                                <th/>
                            </tr>
                            </thead>
                            <tbody>
                            {
                                this.state.books.map((book, i) => (
                                    <tr key={i}>
                                        <td>
                                            <button onClick={() => this.showBookDetail(book)}>{book.name}</button>
                                        </td>
                                        <td>{book.author.name}</td>
                                        <td>
                                            <button onClick={() => this.deleteBookClicked(book.id)}>удалить</button>
                                        </td>
                                    </tr>
                                ))
                            }
                            </tbody>
                        </table>
                        <BookModal name="Добавить книгу" className="book_button" updateParentState={this.showBooks}/>
                    </div>
                </div>
            </React.Fragment>
        )
    }
};

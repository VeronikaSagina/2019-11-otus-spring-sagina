import React from "react";
import axios from "axios";
import {API_URL} from "./ApiPath";
import AuthorModal from "./AuthorModal";
import parse from 'html-react-parser';
import styles from './styles/Component.module.css';
import NavBar from "./NavigationBar";
import {bodyClass, btnTopClass} from "./styles/styles";

export default class AuthorDetail extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            id: this.props.match.params.id,
            author: this.props.location && this.props.location.state && this.props.location.state.author || {},
            books: []
        };
    }

    componentDidMount() {
        fetch(`${API_URL}/author/${this.state.id}`)
            .then(response => {
                if (response.ok) {
                    return response.json()
                }
                throw new Error()
            })
            .then(author => {
                this.setState({author});
                return author
            })
            .then(author => axios.get(`${API_URL}/book`, {params: {authorId: author.id}}))
            .then(promise => promise.data)
            .then(books => this.setState({books}))
            .catch(message => console.log(message));
    }

    goToAllAuthors = () => {
        this.props.history.push(`/author/`);
    };

    refreshAuthor = () => {
        axios.get(`${API_URL}/author/${this.state.id}`)
            .then(promise => promise.data)
            .then(author => this.setState({author}))
            .catch(message => console.log(message));
    };

    showBookDetail(book) {
        this.props.history.push(`/book/${book.id}`, {book});
    }

    showBooks = () => {
        let books = this.state.books;
        console.log('books ' + books);
        return books.length === 0
            ? <dd><p className={styles.background_grey_padding15}>Не добавлено ни одной книги</p></dd>
            : books.map((book, i) => (
                <dd key={i}>
                    <button className={styles.background_grey_padding15}
                            onClick={() => this.showBookDetail(book)}>{book.name}</button>
                </dd>
            ))
    };

    render() {
        return (
            <React.Fragment>
                <div className={styles.background_color}>
                    <div className={styles.All_body}>
                        <NavBar/>
                        <div className={bodyClass}>
                            <div className={styles.button_area}>
                                <button className={btnTopClass} onClick={this.goToAllAuthors}>Назад</button>
                                <AuthorModal author={this.state.author} name="Редактировать" className={btnTopClass}
                                             updateParentState={this.refreshAuthor}/>
                            </div>
                            <div className={styles.h3_block}>
                                <h3 className={styles.h3_style}>{this.state.author.name}</h3>
                            </div>
                            <hr className={styles.hr_line}/>
                            <p className={styles.description}>{parse(this.state.author.description)}</p>
                            <hr className={styles.hr_line}/>
                            <dl>
                                <dt className={styles.book_text}>Список книг:</dt>
                                {this.showBooks()}</dl>
                        </div>
                    </div>
                </div>
            </React.Fragment>
        );
    }
};
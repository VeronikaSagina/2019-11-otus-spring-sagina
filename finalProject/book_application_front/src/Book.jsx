import React from 'react'
import axios from 'axios';
import BookModal from "./BookModal";
import {API_URL} from "./ApiPath";
import AuthorList from "./AuthorList";
import styles from './styles/Component.module.css';
import NavBar from "./NavigationBar";
import {bodyClass, btnClass, tableRow} from "./styles/styles";
import {Input} from "./Types";
import {adminRole, sessionRole, sessionToken} from "./SessionUser";

export default class Book extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            books: [],
            params: {
                author: null,
                query: ""
            }
        };
    }

    componentDidMount() {
        fetch(`${API_URL}/book`)
            .then(response => response.json())
            .then(books => this.setState({books}))
            .catch(message => console.log(message));
    }

    showBookDetail(book) {
        this.getBookById(book.id)
            .then(book => this.props.history.push(`/book/${book.id}`, {book}));
    }

    getBookById(id) {
        return axios.get(`${API_URL}/book/${id}`)
            .then(promise => promise.data)
            .catch(message => console.log(message));
    }

    deleteBookClicked(id) {
        this.deleteBook(id)
            .then(() => this.showBooks(this.state.params.query, this.state.params.author));
    }

    deleteBook(id) {
        return axios.delete(`${API_URL}/book/${id}`, {headers: {"Authorization": `${sessionToken}`}})
            .catch(message => console.log(message));
    }

    showBooks(query, author) {
        let authorId = author === null ? "" : author.id;
        axios.get(`${API_URL}/book`,
            {params: {query: query, authorId: authorId}})
            .then(promise => promise.data)
            .then(books => this.setState({books}))
            .catch(message => console.log(message));
    }

    setQuery = (e) => {
        let param = e.target.value;
        this.setState(state => ({
            params: {...state.params, query: param}
        }), () => console.log('params query: ' + this.state.params.query));
        this.showBooks(param, this.state.params.author)
    };

    sendEmail = (bookId) => {
        axios.post(`${API_URL}/book/${bookId}/send`, [], {headers: {"Authorization": `${sessionToken}`}})
            .catch(message => console.log(message));
    };

    setAuthor = (e) => {
        let author = e.value;
        this.setState(state => ({
                params: {...state.params, author: author}
            }),
            () => console.log('params author: ' + this.state.params.author.name));
        this.showBooks(this.state.params.query, author)
    };

    bookDel(book) {
        if (sessionRole === adminRole) {
            return <td className={styles.td_}>
                <button onClick={() => this.deleteBookClicked(book.id)}>удалить
                </button>
            </td>
        }
    }

    bookRek(book) {
        if (sessionRole === adminRole) {
            return <td className={styles.td_}>
                <button onClick={() => this.sendEmail(book.id)}>отправить рекомендацию</button>
            </td>
        }
    }

    getBookModal() {
        if (sessionToken !== null) {
            return <BookModal name="Добавить книгу" className={btnClass}
                              updateParentState={
                                  () => this.showBooks(this.state.params.query, this.state.params.author)
                              }/>
        }
    }

    render() {
        return (
            <React.Fragment>
                {console.log("token " + sessionToken)}
                {console.log("role " + sessionRole)}
                <div className={styles.background_color}>
                    <div className={styles.All_body}>
                        <NavBar/>
                        <div className={bodyClass}>
                            <div className={styles.h3_style}><h3>Список книг</h3></div>
                            <form className={styles.button_area}>
                                <Input
                                    title={'фильтр по названию'}
                                    hidden={false}
                                    name={'query'}
                                    type={'text'}
                                    value={this.state.params.query}
                                    handleChange={this.setQuery}
                                    placeholder={'введите название'}/>
                                <AuthorList handleSubmit={this.setAuthor} currentAuthor='' title='фильтр по автору'/>
                            </form>
                            <div className={styles.table_area}>
                                <hr className={styles.hr_strong}/>

                                <table className={styles.table_}>
                                    <tbody>
                                    {this.state.books.map((book, i) => (
                                        <tr className={tableRow} key={i}>
                                            <td>
                                                <button onClick={() => this.showBookDetail(book)}
                                                        className={styles.name_button}>
                                                    <div className={styles.space_between}>
                                                        <div>{book.name}</div>
                                                        <div>{book.author.name}</div>
                                                    </div>
                                                </button>
                                            </td>
                                            {this.bookDel(book)}
                                            {this.bookRek(book)}
                                        </tr>
                                    ))}
                                    </tbody>
                                </table>
                                {this.getBookModal()}
                            </div>
                        </div>
                    </div>
                </div>
            </React.Fragment>
        )
    }
};

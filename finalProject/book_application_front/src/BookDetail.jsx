import React from "react";
import BookComment from "./Comment";
import BookModal from "./BookModal";
import axios from "axios";
import {API_URL} from "./ApiPath";
import styles from './styles/Component.module.css';
import NavBar from "./NavigationBar";
import {bodyClass, btnDownClass, btnDownClassMargin, btnTopClass, btnTopNotAuthorizedClass} from "./styles/styles";
import {sessionToken} from "./SessionUser";

export default class BookDetail extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            id: this.props.match.params.id,
            book: this.props.location && this.props.location.state && this.props.location.state.book || {
                author: {},
                genres: [],
                fileIds: []
            }
        };
    }

    componentDidMount() {
        console.log("props " + this.state.id);
        fetch(`${API_URL}/book/${this.state.id}`)
            .then(response => {
                if (response.ok) {
                    return response.json()
                }
                throw new Error()
            })
            .then(book => this.setState({book}))
            .catch(message => console.log(message));
    }

    goToAllBooks = () => {
        this.props.history.push(`/book/`);
    };

    refreshBook = () => {
        axios.get(`${API_URL}/book/${this.state.id}`)
            .then(promise => promise.data)
            .then(book => this.setState({book}))
            .catch(message => console.log(message));
    };

    downloadFile = (fileId) => {
        if (sessionToken === null) {
            return;
        }
        fetch(`${API_URL}/book/download/${fileId}`)
            .then(response => {
                const filename = response.headers.get('Content-Disposition').split('filename=')[1];
                response.blob().then(blob => {
                    let url = window.URL.createObjectURL(blob);
                    let a = document.createElement('a');
                    a.href = url;
                    a.download = filename;
                    a.click();
                });
            });
    };

    readBook(fileId) {
        this.props.history.push(`/read/${fileId}`, {fileId});
    }

    getDownload() {
        if (sessionToken === null) {
            return <div className={styles.center}>
                <button className={btnDownClassMargin}>Скачивание доступно только зарегистрированным пользователям</button>
            </div>
        }
        return (
            <div className={styles.center}>
                <button className={btnDownClass}
                        onClick={() => this.downloadFile(this.state.book.fileIds[0])}>Скачать
                </button>
            </div>
        )
    }

    render() {
        let {id, name, author, description, genres} = this.state.book;
        return (
            <React.Fragment>
                <div className={styles.background_color}>
                    <div className={styles.All_body}>
                        <NavBar/>
                        <div className={bodyClass}>
                            <div className={styles.button_area}>
                                <button className={btnTopClass} onClick={() => this.props.history.go(-1)}>Назад</button>
                                {
                                    sessionToken === null
                                        ? <p className={btnTopNotAuthorizedClass}
                                        >Редактирование доступно только зарегистрированным пользователям</p>
                                        : <BookModal book={this.state.book} name="Редактировать" className={btnTopClass}
                                                     updateParentState={this.refreshBook}/>
                                }
                            </div>
                            <div className={styles.h3_block}>
                                <h3 className={styles.h3_style}>{author.name}</h3>
                                <h3 className={styles.h3_style}>{name}</h3>
                            </div>
                            <dl className={styles.center}>{
                                genres.map((genre, i) => (<dd className={styles.list} key={i}>{genre.name}</dd>))
                            }
                            </dl>
                            <hr className={styles.hr_line}/>
                            <p className={styles.description}>{description}</p>
                            <hr className={styles.hr_line}/>
                            <div className={styles.center}>
                                {this.getDownload()}
                            </div>
                            <div className={styles.center}>
                                <button className={btnDownClass}
                                        onClick={() => this.readBook(this.state.book.fileIds[0])}>Читать
                                </button>
                            </div>
                            <BookComment id={this.state.id}/>
                        </div>
                    </div>
                </div>
            </React.Fragment>
        );
    }
};
import React from 'react'
import axios from "axios";
import {Input, Textarea} from "./Types";
import Genre from "./Genre";
import AuthorList from "./AuthorList";
import {API_URL} from "./ApiPath";
import styles from './styles/Component.module.css';
import {sessionToken} from "./SessionUser";

export default class BookModal extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            isOpen: false,
            name: props.name,
            file: '',
            book: props.book || {
                id: '',
                name: '',
                author: {},
                description: '',
                genres: []
            },
            className: props.className,
        };
        this.updateParentState = this.props.updateParentState;
    }

    toggleModal = () => {
        this.setState({
            isOpen: !this.state.isOpen
        });
    };

    editBook = () => {
        let {id, name, author, description, genres} = this.state.book;
        let _book = {id, name, author, description, genres: genres.map(g => g.id)};

        if (_book.id === '') {
            const formData = new FormData();
            formData.append('file', this.state.selectedFile);
            formData.append('book', new Blob([JSON.stringify(_book)], {type: 'application/json'}));
            axios.post(`${API_URL}/book`, formData,  { headers: {"Authorization" : `${sessionToken}`}})
                .then(() => this.updateParentState())
                .then(() => this.handleClearForm());
        } else {
            axios.patch(`${API_URL}/book`, _book,  { headers: {"Authorization" : `${sessionToken}`}})
                .then(() => this.updateParentState())
        }
        this.toggleModal();
    };

    handleClearForm() {
        this.setState(
            {
                selectedFile: null,
                book: {
                    id: '',
                    name: '',
                    author: {},
                    description: '',
                    genres: []
                }
            }
        );
    };

    handleGenreSubmit = (input) => {
        if (input) {
            this.setState(prevState => ({
                book: {
                    ...prevState.book, genres: input.map(it => it.value)
                }
            }), () => console.log(this.state.book));
        }
    };

    handleAuthorSubmit = (input) => {
        this.setState(prevState => ({
            book: {
                ...prevState.book, author: input.value
            }
        }), () => console.log(this.state.book));
    };

    handleSubmit = (event) => {
        const {value, name} = event.target;
        this.setState(prevState => ({
            book: {
                ...prevState.book, [name]: value
            }
        }), () => console.log(this.state.book))
    };

    handleFile = (e) => {
        this.setState({
            selectedFile: e.target.files[0]
        });
    };

    bookForm() {
        return <div className={styles.modal_style} id="modal">
            <form onSubmit={this.editBook} name="book">
                <Input
                    hidden={true}
                    readonly={true}
                    name={'id'}
                    type={'text'}
                    value={this.state.book.id}
                    placeholder={'id'}/>
                <Input
                    title={'название'}
                    hidden={false}
                    name={'name'}
                    type={'text'}
                    value={this.state.book.name}
                    handleChange={this.handleSubmit}
                    placeholder={'Введите название'}/>
                <Genre handleSubmit={this.handleGenreSubmit} currentGenre={this.state.book.genres}/>
                <AuthorList handleSubmit={this.handleAuthorSubmit} currentAuthor={this.state.book.author} title={'Список авторов'}/>
                <Textarea
                    title={'описание'}
                    hidden={false}
                    name={'description'}
                    value={this.state.book.description}
                    handleChange={this.handleSubmit}
                    placeholder={'Добавьте описание книги'}/>
                <div className={styles.inner_button_area}>
                    <input type="file" name="file" onChange={this.handleFile}/>
                </div>
                <div className={styles.inner_button_area}>
                    <button type="button" onClick={this.toggleModal}>закрыть</button>
                    <input type="submit" value="сохранить"/>
                </div>
            </form>
        </div>
    }

    checkAndShow() {
        if (!this.state.isOpen) {
            return null;
        }
        return this.bookForm()
    }

    render() {
        return (
            <React.Fragment>
                <div>
                    <button className={this.state.className} onClick={this.toggleModal}>{this.state.name}</button>
                    {this.checkAndShow()}
                </div>
            </React.Fragment>
        );
    }
};
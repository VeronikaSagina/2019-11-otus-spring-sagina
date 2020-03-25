import React from 'react'
import "./css/modal.css"
import "./css/book.css"
import axios from "axios";
import {Input, Textarea} from "./Types";
import Genre from "./Genre";
import Author from "./Author";

export default class BookModal extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            isOpen: false,
            name: props.name,
            book: props.book || {
                id: '',
                title: '',
                author: {},
                description: '',
                genres: []
            },
            className: props.className,
        };
        this.handleSubmit = this.handleSubmit.bind(this);
        this.handleGenreSubmit = this.handleGenreSubmit.bind(this);
        this.handleAuthorSubmit = this.handleAuthorSubmit.bind(this);
        this.toggleModal = this.toggleModal.bind(this);
        this.editBook = this.editBook.bind(this);
        this.updateParentState = this.props.updateParentState;
    }

    toggleModal() {
        this.setState({
            isOpen: !this.state.isOpen
        });
    }

    editBook() {
        let book = {
            id: this.state.book.id,
            name: this.state.book.name,
            author: this.state.book.author,
            description: this.state.book.description,
            genres: this.state.book.genres.map(g => g.id)
        };
        if (book.id === '') {
            axios.post(`/book/create`, book)
                .then(() => this.updateParentState());
            this.handleClearForm();
        } else {
            axios.put(`/book/update`, book)
                .then(() => this.updateParentState())
        }
        this.toggleModal();
    }

    handleClearForm() {
        this.setState(
            {
                book: {
                    id: '',
                    name: '',
                    author: '',
                    description: '',
                    genres: []
                }
            }
        );
    };

    handleGenreSubmit(input) {
        if (input) {
            this.setState(prevState => ({
                book: {
                    ...prevState.book, genres: input.map(it => it.value)
                }
            }), () => console.log(this.state.book));
        }
    };

    handleAuthorSubmit(input) {
        this.setState(prevState => ({
            book: {
                ...prevState.book, author: input.value
            }
        }), () => console.log(this.state.book));
    };

    handleSubmit(event) {
        let value = event.target.value;
        let name = event.target.name;
        this.setState(prevState => ({
            book: {
                ...prevState.book, [name]: value
            }
        }), () => console.log(this.state.book))
    };

    bookForm() {
        return <div className="modal_style" id="modal">
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
                <Author handleSubmit={this.handleAuthorSubmit} currentAuthor={this.state.book.author}/>
                <Textarea
                    title={'описание'}
                    hidden={false}
                    name={'description'}
                    value={this.state.book.description}
                    handleChange={this.handleSubmit}
                    placeholder={'Добавьте описание книги'}/>
                <div className="inner_button_area">
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
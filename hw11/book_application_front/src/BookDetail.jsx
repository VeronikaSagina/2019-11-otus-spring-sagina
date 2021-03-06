import React from "react";
import BookComment from "./Comment";
import BookModal from "./BookModal";
import "./css/book.css"
import "./css/style.css"
import axios from "axios";

export default class BookDetail extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            id: this.props.match.params.id,
            book: this.props.location && this.props.location.state && this.props.location.state.book || {
                author: {},
                genres: []
            }
        };
    }

    componentDidMount() {
        fetch(`/book/${this.state.id}`)
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
        axios.get(`/book/${this.state.id}`)
            .then(promise => promise.data)
            .then(book => this.setState({book}))
            .catch(message => console.log(message));
    };

    render() {
        let {id, name, author, description, genres} = this.state.book;
        return (
            <React.Fragment>
                <div className="book_background">
                    <div className="button_area">
                        <button className="book_button_top" onClick={this.goToAllBooks}>Назад</button>
                        <BookModal book={this.state.book} name="Редактировать" className="book_button_top"
                                   updateParentState={this.refreshBook}/>
                    </div>
                    <div className="h3_block">
                        <h3>{author.name}</h3>
                        <h3>{name}</h3>
                    </div>
                    <dl className="center">{
                       genres.map((genre, i) => (<dd className="list" key={i}>{genre.name}</dd>))
                    }
                    </dl>
                    <hr/>
                    <p className="description">{description}</p>
                    <hr/>
                    <BookComment id={id}/>
                </div>
            </React.Fragment>
        );
    }
};
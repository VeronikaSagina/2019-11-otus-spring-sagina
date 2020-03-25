import * as React from "react";
import axios from "axios";

import "./css/book.css"

export default class BookComment extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            id: this.props.id,
            value: '',
            comments: []
        };
        this.handleChange = this.handleChange.bind(this);
    }

    componentDidMount() {
        fetch(`/comment/list/${this.props.id}`)
            .then(response => response.json())
            .then(comments => this.setState({comments}))
            .catch(message => {
                console.log(message)
            });
    }

    handleChange(event) {
        this.setState({value: event.target.value});
    }

    showBookComments() {
        let comments = this.state.comments;
        return comments.length === 0
            ? <dd><p className="com">К этой книге пока нет коментариев...</p></dd>
            : comments.map((comment, i) => (
                <dd key={i}><p className="com">{comment.message}</p></dd>
            ))
    }

    updateListComments(bookId){
        axios.get(`/comment/list/${bookId}`)
            .then(response => response.data)
            .then(comments => {
                this.setState({comments});
                return comments
            })
            .then(() => this.showBookComments())
    }

    createBookComment = (event) => {
        event.preventDefault();
        if (this.state.value === '') {
            return;
        }
        let data = {
            bookId: this.props.id,
            message: this.state.value
        };
        axios.post(`/comment/create`, data)
            .then(() => this.updateListComments(data.bookId));
        this.setState({ value: '' });
        document.getElementById("comment_input").reset();
    };

    render() {
        return (
            <div>
                <div>
                    <dl>
                        <dt className="book_text">Комментарии:</dt>
                        {this.showBookComments()}
                    </dl>
                </div>
                <div>
                    <form id="comment_input" className="comment_form" onSubmit={this.createBookComment}>
                        <label>Добавить комментарий: </label>
                        <textarea rows="10" cols="100" placeholder="|_(•◡•)_|"
                                  onChange={this.handleChange}/>
                        <div className="book_button_down">
                            <input type="submit" value="Добавить комментарий"/>
                        </div>
                    </form>
                </div>
            </div>
        );
    }
}
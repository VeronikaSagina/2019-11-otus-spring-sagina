import * as React from "react";
import axios from "axios";
import styles from './styles/Component.module.css';
import {API_URL} from "./ApiPath";
import {sessionToken} from "./SessionUser";
import Popup from "reactjs-popup";
import PopupModal from "./PopupModal";

export default class BookComment extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            id: this.props.id,
            value: '',
            comments: []
        };
    }

    componentDidMount() {
        fetch(`${API_URL}/comment/list/${this.props.id}`)
            .then(response => response.json())
            .then(comments => this.setState({comments}))
            .catch(message => console.log(message));
    }

    handleChange = (event) => {
        this.setState({value: event.target.value});
    };

    showBookComments() {
        let comments = this.state.comments;
        return comments.length === 0
            ? <dd><p className={styles.background_grey_padding15}>К этой книге пока нет коментариев...</p></dd>
            : comments.map((comment, i) => (
                <dd key={i}>
                    <div className={styles.background_grey_padding15}>
                        <p>{comment.message}</p><p className={styles.margin10r}>{comment.user.login}</p>
                    </div>
                </dd>
            ))
    }

    updateListComments(bookId) {
        axios.get(`${API_URL}/comment/list/${bookId}`)
            .then(response => response.data)
            .then(comments => {
                this.setState({comments});
                return comments
            })
            .then(() => this.showBookComments())
    }

    popup(state) {
        return (
            <Popup modal trigger={sessionToken === null}>
                {close => <PopupModal close={close}
                                      message="чтоб оставлять комментарии ниеобходимо зарегистрироваться"/>}
            </Popup>
        )
    }

    createBookComment = (event) => {
        event.preventDefault();
        if (sessionToken === null) {
            return this.popup(true);
        }
        if (this.state.value === '') {
            return;
        }
        let data = {
            bookId: this.props.id,
            message: this.state.value
        };
        axios.post(`${API_URL}/comment`, data, {headers: {"Authorization": `${sessionToken}`}})
            .then(() => this.updateListComments(data.bookId));
        this.setState({value: ''});
        document.getElementById("comment_input").reset();
    };

    render() {
        return (
            <div>
                <div>
                    <dl>
                        <dt className={styles.book_text}>Комментарии:</dt>
                        {this.showBookComments()}
                    </dl>
                </div>
                <div>
                    {sessionToken === null
                        ? <div className={styles.message_text}><p>чтоб оставлять комментарии необходимо зарегистрироваться</p></div>
                        : <form id="comment_input" className={styles.comment_form} onSubmit={this.createBookComment}>
                            <label>Добавить комментарий: </label>
                            <textarea rows="10" cols="100" placeholder="|_(•◡•)_|"
                                      onChange={this.handleChange}/>
                            <div className={styles.book_button_down}>
                                <input type="submit" value="Добавить комментарий"/>
                            </div>
                        </form>
                    }
                </div>
            </div>
        );
    }
}
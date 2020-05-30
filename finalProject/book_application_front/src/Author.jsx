import React from 'react'
import axios from 'axios';
import {API_URL} from "./ApiPath";
import AuthorModal from "./AuthorModal";
import styles from './styles/Component.module.css';
import NavBar from "./NavigationBar";
import {bodyClass, btnClass, tableRow} from "./styles/styles";
import {adminRole, sessionRole, sessionToken} from "./SessionUser";

export default class Author extends React.Component {
    constructor(props) {
        super(props);
        this.state = {authors: [], query: ''};
    }

    componentDidMount() {
        fetch(`${API_URL}/author`)
            .then(response => response.json())
            .then(authors => this.setState({authors}))
            .catch(message => console.log(message));
    }

    showAuthorDetail(author) {
        this.getAuthorById(author.id)
            .then(author => this.props.history.push(`/author/${author.id}`, {author}));
    }

    getAuthorById(id) {
        return axios.get(`${API_URL}/author/${id}`)
            .then(promise => promise.data)
            .catch(message => console.log(message));
    }

    deleteAuthorClicked(id) {
        this.deleteAuthor(id)
            .then(() => this.showAuthors());
    }

    deleteAuthor(id) {
        return axios.delete(`${API_URL}/author/${id}`, {headers: {"Authorization": `${sessionToken}`}})
            .catch(message => console.log(message));
    }

    showAuthors = (query) => {
        axios.get(`${API_URL}/author`, {params: {name: query}})
            .then(promise => promise.data)
            .then(authors => this.setState({authors}))
            .catch(message => console.log(message));
    };

    getAuthorModal() {
        if (sessionToken !== null) {
            return <AuthorModal name="Добавить автора" className={btnClass}
                                updateParentState={this.showAuthors}/>
        }
    }

    render() {
        return (
            <React.Fragment>
                <div className={styles.background_color}>
                    <div className={styles.All_body}>
                        <NavBar/>
                        <div className={bodyClass}>
                            <h3 className={styles.h3_style}>Список авторов</h3>
                            <div>
                                <label>поиск по имени</label>
                                <input onChange={e => this.showAuthors(e.target.value)}/>
                            </div>
                            <div className={styles.table_area}>
                                <hr className={styles.hr_strong}/>
                                <table className={styles.table_}>
                                    <tbody>
                                    {this.state.authors.map((author, i) => (
                                        <tr className={tableRow} key={i}>
                                            <td>
                                                <button onClick={() => this.showAuthorDetail(author)}
                                                        className={styles.name_button}>
                                                    {sessionRole !== adminRole
                                                        ? <div className={styles.author_text}>{author.name}</div>
                                                        : <div className={styles.space_between}>
                                                            <div>{author.name}</div>
                                                        </div>
                                                    }
                                                </button>
                                            </td>
                                            {sessionRole !== adminRole
                                                ? <td/>
                                                : <td className={styles.td_}>
                                                    <button
                                                        onClick={() => this.deleteAuthorClicked(author.id)}>удалить
                                                    </button>
                                                </td>
                                            }
                                        </tr>
                                    ))
                                    }
                                    </tbody>
                                </table>
                                {this.getAuthorModal()}
                            </div>
                        </div>
                    </div>
                </div>
            </React.Fragment>
        )
    }
};

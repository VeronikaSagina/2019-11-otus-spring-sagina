import React from 'react'
import axios from 'axios';
import UserModal from "./UserModal";
import {API_URL} from "./ApiPath";
import styles from './styles/Component.module.css';
import NavBar from "./NavigationBar";
import {bodyClass, tableRow} from "./styles/styles";
import {sessionToken} from "./SessionUser";

export default class User extends React.Component {
    constructor(props) {
        super(props);
        this.state = {users: []};
    }

    /*  getUserById(id) {
          return axios.get(`${API_URL}/user/${id}`)
              .then(promise => promise.data)
              .catch(message => console.log(message))
              .then(user => this.props.history.push(`/user/${user.id}`, {user}));
      }*/

    componentDidMount() {
        this.showUsers()
    }

    lockedUser(id) {
        return axios.post(`${API_URL}/user/${id}/lock`,  { headers: {"Authorization" : `${sessionToken}`}})
            .then(() => this.showUsers())
            .catch(message => console.log(message));
    }

    showUsers() {
        axios.get(`${API_URL}/user`,  { headers: {"Authorization" : `${sessionToken}`}})
            .then(promise => promise.data)
            .then(users => this.setState({users}))
            .catch(message => console.log(message));
    }

    render() {
        return (
            <React.Fragment>
                <div className={styles.background_color}>
                    <div className={styles.All_body}>
                        <NavBar/>
                        <div className={bodyClass}>
                            <div className={styles.h3_style}><h3>Список пользователей</h3></div>
                            <div className={styles.table_area}>
                                <hr className={styles.hr_strong}/>
                                <table className={styles.table_}>
                                    <thead>
                                    <tr className={styles.th_}>
                                        <th>Логин</th>
                                        <th>Почта</th>
                                        <th>Роль</th>
                                        <th>Блокировка</th>
                                        <th/>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    {
                                        this.state.users.map((user, i) => (
                                            <tr className={tableRow} key={i}>
                                                <td className={styles.padding15}>{user.login}</td>
                                                <td>{user.email}</td>
                                                <td>{user.role}</td>
                                                <td>{user.locked === true ? 'заблокирован' : 'активен'}</td>
                                                <td><UserModal user={user} name="Редактировать"
                                                               updateParentState={this.showUsers}/></td>
                                            </tr>))
                                    }
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>
                </div>
            </React.Fragment>
        )
    }
};

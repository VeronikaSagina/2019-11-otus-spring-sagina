import React from 'react'
import axios from "axios";
import {Input} from "./Types";
import {API_URL} from "./ApiPath";
import styles from './styles/Component.module.css';
import {sessionToken} from "./SessionUser";

export default class UserModal extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            isOpen: false,
            name: props.name,
            user: props.user,
            className: props.className,
        };
        this.updateParentState = this.props.updateParentState;
    }

    toggleModal = () => {
        this.setState({
            isOpen: !this.state.isOpen
        });
    };

    editUser = () => {
        axios.patch(`${API_URL}/user`, this.state.user,  { headers: {"Authorization" : `${sessionToken}`}})
            .then(() => this.updateParentState());
    };

    handleClearForm() {
        this.setState(
            {
                user: {
                    id: '',
                    login: '',
                    email: '',
                    role: '',
                    locked: false
                }
            }
        );
    };

    handleSubmit = (event) => {
        const {value, name} = event.target;
        this.setState(prevState => ({
            user: {
                ...prevState.user, [name]: value
            }
        }), () => console.log(this.state.user))
    };


    bookForm() {
        return <div className={styles.modal_style} id="modal">
            <form onSubmit={this.editUser} name="user">
                <Input
                    hidden={true}
                    readonly={true}
                    name={'id'}
                    type={'text'}
                    value={this.state.user.id}
                    placeholder={'id'}/>
                <Input
                    title={'логин'}
                    hidden={false}
                    name={'login'}
                    type={'text'}
                    value={this.state.user.login}
                    handleChange={this.handleSubmit}
                    placeholder={'логин'}/>
                <Input
                    title={'почта'}
                    hidden={false}
                    name={'email'}
                    type={'text'}
                    value={this.state.user.email}
                    handleChange={this.handleSubmit}
                    placeholder={'почта'}/>
                <Input
                    title={'роль'}
                    hidden={false}
                    name={'role'}
                    type={'bool'}
                    value={this.state.user.role}
                    handleChange={this.handleSubmit}
                    placeholder={'роль'}/>
                <Input
                    title={'блокировка'}
                    hidden={false}
                    name={'locked'}
                    type={'text'}
                    value={this.state.user.locked}
                    handleChange={this.handleSubmit}
                    placeholder={'блокировка'}/>
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
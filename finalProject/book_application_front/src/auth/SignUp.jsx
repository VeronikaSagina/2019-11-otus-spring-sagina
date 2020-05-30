import React, {Component} from 'react';
import styles from '../styles/Component.module.css';
import {Input} from "../Types";
import axios from "axios";
import {API_URL} from "../ApiPath";
import Popup from "reactjs-popup";

export default class SignUp extends Component {
    constructor(props) {
        super(props);
        this.state = {
            firstAuth: props.firstAuth,
            login: "",
            password: "",
            email: "",
            consentToCommunication: true
        };
    }

    userFirstAuth = (user) => {
        axios.post(`${API_URL}/auth/first-login`, user)
            .then(data => {
                localStorage.setItem("token", data.data.jwt);
                console.log("token " + localStorage.getItem("token"))
            })
    };

    userLogin = (user) => {
        axios.post(`${API_URL}/auth`, user)
            .then(data => {
                localStorage.setItem("token", data.data.jwt);
                localStorage.setItem("role", data.data.role);
            })
    };

    handleChange = event => {
        this.setState({
            [event.target.name]: event.target.value
        });
    };

    handleCheckbox = event => {
        const value = event.target.checked;
        this.setState({
            [event.target.name]: value
        });
    };

    handleSubmit = (event) => {
        event.preventDefault();
        this.state.firstAuth ? this.userFirstAuth(this.state) : this.userLogin(this.state);
    };

    loginForm(close) {
        console.log("consentToCommunication " + this.state.consentToCommunication);
        return (
            <div className={styles.modal_popup}>
                <a className={styles.close} onClick={close}>
                    &times;
                </a>
                <form onSubmit={this.handleSubmit}>
                    <Input
                        title={'Имя пользователя'}
                        hidden={false}
                        name={'login'}
                        type={'text'}
                        value={this.state.login}
                        handleChange={this.handleChange}
                        placeholder={'Введите логин'}/>
                    <Input
                        title={'Пароль'}
                        hidden={false}
                        name={'password'}
                        type={'password'}
                        value={this.state.password}
                        handleChange={this.handleChange}
                        placeholder={'Введите пароль'}/>
                    {this.state.firstAuth
                        ? <Input
                            title={'Электронный адрес'}
                            hidden={false}
                            name={'email'}
                            type={'email'}
                            value={this.state.email}
                            handleChange={this.handleChange}
                            placeholder={'Введите email'}/>
                        : ''
                    }
                    {this.state.firstAuth
                        ? <Input
                            title={'Посылать рекомендации'}
                            hidden={false}
                            name={'consentToCommunication'}
                            type={'checkbox'}
                            value={this.state.consentToCommunication}
                            handleChange={this.handleCheckbox}
                            placeholder={"true"}/>
                        : ''
                    }
                    <div className={styles.inner_button_area}>
                        <button type="button" onClick={close}>закрыть</button>
                        <input type="submit" value="войти"/>
                    </div>
                </form>
            </div>
        )
    }

    render() {
        return (
            <React.Fragment>
                <div>
                    <Popup modal trigger={
                        <button className={styles.login_btn}>
                            {this.state.firstAuth ? 'Зарегистрироваться' : 'Войти с паролем'}
                        </button>}>
                        {close => this.loginForm(close)}
                    </Popup>
                </div>
            </React.Fragment>
        );
    }
}
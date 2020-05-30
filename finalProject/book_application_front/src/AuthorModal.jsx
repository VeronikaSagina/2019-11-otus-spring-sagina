import React from 'react'
import styles from './styles/Component.module.css';
import axios from "axios";
import {Input, Textarea} from "./Types";
import {API_URL} from "./ApiPath";
import {sessionToken} from "./SessionUser";

export default class AuthorModal extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            isOpen: false,
            name: props.name,
            author: props.author || {
                id: '',
                name: '',
                description: '',
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

    editAuthor = () => {
        if (this.state.author.id === '') {
            axios.post(`${API_URL}/author`, this.state.author, {headers: {"Authorization": `${sessionToken}`}})
                .then(() => this.updateParentState())
                .then(() => this.handleClearForm());
        } else {
            axios.patch(`${API_URL}/author`, this.state.author, {headers: {"Authorization": `${sessionToken}`}})
                .then(() => this.updateParentState())
        }
        this.toggleModal();
    };

    handleClearForm() {
        this.setState(
            {
                author: {
                    id: '',
                    name: '',
                    description: '',
                }
            }
        );
    };

    handleSubmit = (event) => {
        const {value, name} = event.target;
        this.setState(prevState => ({
            author: {
                ...prevState.author, [name]: value
            }
        }), () => console.log(this.state.author))
    };

    authorForm() {
        return <div className={styles.modal_style} id="modal">
            <form onSubmit={this.editAuthor} name="author">
                <Input
                    hidden={true}
                    readonly={true}
                    name={'id'}
                    type={'text'}
                    value={this.state.author.id}
                    placeholder={'id'}/>
                <Input
                    title={'Имя'}
                    hidden={false}
                    name={'name'}
                    type={'text'}
                    value={this.state.author.name}
                    handleChange={this.handleSubmit}
                    placeholder={'Введите имя'}/>
                <Textarea
                    title={'описание'}
                    hidden={false}
                    name={'description'}
                    value={this.state.author.description}
                    handleChange={this.handleSubmit}
                    placeholder={'Добавьте описание'}/>
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
        return this.authorForm()
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
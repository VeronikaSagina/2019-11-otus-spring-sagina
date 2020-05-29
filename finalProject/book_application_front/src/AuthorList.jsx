import React from 'react'
import {OneSelect} from "./Types";
import {API_URL} from "./ApiPath";

export default class AuthorList extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            currentAuthor: this.props.currentAuthor,
            title: this.props.title,
            authors: []
        }
    }

    componentDidMount() {
        fetch(`${API_URL}/author`)
            .then(response => response.json())
            .then(authors => this.setState({authors}))
            .catch(message => console.log(message));
    }

    render() {
        return <div>
            <OneSelect title={this.state.title}
                       name={'author'}
                       options={this.state.authors}
                       value={this.state.currentAuthor}
                       placeholder={'Выберите автора'}
                       handleChange={this.props.handleSubmit}/>
        </div>
    }
}
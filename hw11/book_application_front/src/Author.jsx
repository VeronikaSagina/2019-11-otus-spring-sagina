import React from 'react'
import {OneSelect} from "./Types";

export default class Author extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            currentAuthor: this.props.currentAuthor,
            authors: []
        }
    }

    componentDidMount() {
        fetch(`/author`)
            .then(response => response.json())
            .then(authors => this.setState({authors}))
            .catch(message => console.log(message));
    }

    render() {
        return <div>
            <OneSelect title={'Список авторов'}
                       name={'author'}
                       options={this.state.authors}
                       value={this.state.currentAuthor}
                       placeholder={'Выберите автора'}
                       handleChange={this.props.handleSubmit}/>
        </div>
    }
}
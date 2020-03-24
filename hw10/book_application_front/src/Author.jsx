import React from 'react'
import {OneSelect} from "./Types";

export default class Author extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            handleSubmit: this.props.handleSubmit,
            currentAuthor: this.props.currentAuthor,
            authors: []
        }
    }

    componentDidMount() {
        fetch(`/author`)
            .then(response => response.json())
            .then(authors => this.setState({authors}))
            .catch(message => {
                console.log(message)
            });
    }

    render() {
        return <div>
            <OneSelect title={'Список авторов'}
                       name={'author'}
                       options={this.state.authors}
                       value={this.state.currentAuthor}
                       placeholder={'Выберите автора'}
                       handleChange={this.state.handleSubmit}/>
        </div>
    }
}
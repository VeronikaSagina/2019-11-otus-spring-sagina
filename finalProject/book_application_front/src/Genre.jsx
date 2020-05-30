import React from 'react'
import {MultiplySelect} from "./Types";
import {API_URL} from "./ApiPath";

export default class Genre extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            currentGenres: this.props.currentGenre,
            genres: []
        }
    }

    componentDidMount() {
        fetch(`${API_URL}/genre`)
            .then(response => response.json())
            .then(genres => this.setState({genres}))
            .catch(message => console.log(message));
    }

    render() {
        return <div>
            <MultiplySelect title={'Список жанров'}
                            name={'genre'}
                            options={this.state.genres}
                            value={this.state.currentGenres}
                            placeholder={'Выберите жанры'}
                            handleChange={this.props.handleSubmit}/>
        </div>
    }
}
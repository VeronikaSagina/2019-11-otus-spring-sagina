import '../css/book.css'
import React from 'react';
import {Link} from 'react-router-dom';

export default class ErrorHandler extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            errorOccurred: false,
            error: {}
        };
    }

    componentDidCatch(error, info) {
        this.setState({errorOccurred: true, error: error});
    }

    render() {
        if (this.state.errorOccurred) {
            return (
                <div className="background">
                    <div className="book_text">
                        <h3>Упс! произошла ошибка...</h3>
                        <p>{this.state.error.message}</p>
                        <div>
                            <Link className="input_for_edit book_button_down" to="/book">вернуться к списку книг</Link>
                        </div>
                    </div>
                </div>
            );
        }
        return this.props.children;
    }
};

import React from 'react';
import {Link} from 'react-router-dom';
import classNames from "classnames";
import styles from "../styles/Component.module.css";

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
        const btnClass = classNames({[styles.input_for_edit]: true, [styles.book_button_down]: true});

        if (this.state.errorOccurred) {
            return (
                <div className="background">
                    <div className={styles.book_text}>
                        <h3>Упс! произошла ошибка...</h3>
                        <p>{this.state.error.message}</p>
                        <div>
                            <Link className={btnClass} to="/book">вернуться к списку книг</Link>
                        </div>
                    </div>
                </div>
            );
        }
        return this.props.children;
    }
};

import React from "react";
import {Link} from 'react-router-dom';
import classNames from "classnames";
import styles from "../styles/Component.module.css";

export default class NotFound extends React.Component {
    render() {
        const btnClass = classNames({[styles.input_for_edit]: true, [styles.book_button]: true});
        return (
                <div className={styles.book_text}>
                    <h3>404 Страница не найдена</h3>
                    <div>
                        <Link className={btnClass} to="/book">вернуться к списку книг</Link>
                    </div>
            </div>
        );
    }
};

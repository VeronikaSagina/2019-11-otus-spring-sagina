import React, {Component} from 'react';
import styles from "../styles/Component.module.css";

export default class LogoutUser extends Component {
    constructor(props) {
        super(props);
        this.state = {
            name: props.name,
        };
    }

    handleClick = event => {
        event.preventDefault();
        localStorage.removeItem("token");
        localStorage.removeItem("role");
    };

    render() {
        return (
            <div>
                {
                  <button className={styles.login_btn} onClick={this.handleClick}>{this.state.name}</button>
                }
            </div>
        );
    }
}


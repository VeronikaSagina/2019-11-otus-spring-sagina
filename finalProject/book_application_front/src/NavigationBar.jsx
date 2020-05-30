import React from "react";
import styles from "./styles/Component.module.css";
import SignUp from "./auth/SignUp";
import {sessionRole, sessionToken} from "./SessionUser";
import LogoutUser from "./auth/LogoutUser";

export default class NavBar extends React.Component {
    getUsers() {
        if (sessionRole === 'ROLE_ADMIN') {
            console.log("sessionRole " +sessionRole);
            return (
                <div>
                    <hr className={styles.hr_line100}/>
                    <dd className={styles.nav_text}><a className={styles.a_style} href={`/user`}>Список
                        пользователей</a>
                    </dd>
                </div>)
        }
    }

    render() {
        return <div className={styles.nav_style}>
            <dl>
                {this.getUsers()}
                <hr className={styles.hr_line100}/>
                <dd className={styles.nav_text}><a className={styles.a_style} href={`/book`}>Список книг</a></dd>
                <hr className={styles.hr_line100}/>
                <dd className={styles.nav_text}><a className={styles.a_style} href={`/author`}>Список авторов</a></dd>
                <hr className={styles.hr_line100}/>
            </dl>
            {sessionToken === null
                ? <div>
                    <SignUp firstAuth={false}/>
                    <SignUp firstAuth={true}/>
                </div>
                : <LogoutUser name="Выход"/>
            }
        </div>
    }
}

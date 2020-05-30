import React from "react";
import styles from './styles/Component.module.css';

export default ({ close, message }) => (
    <div className={styles.modal_popup}>
        <a className={styles.close} onClick={close}>
            &times;
        </a>
        <div className={styles.header}> Modal Title </div>
        <div className={styles.book_text}>
            {" "}
          {message}
        </div>
    </div>
);
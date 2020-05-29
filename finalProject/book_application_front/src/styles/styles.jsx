import classNames from "classnames";
import styles from "./Component.module.css";

export const btnClass = classNames({[styles.btn]: true, [styles.book_button]: true});
export const tableRow = classNames({[styles.tr_odd]: true, [styles.tr_even]: true});
export const bodyClass = classNames({[styles.Book_background]: true, [styles.Body_style]: true});
export const btnTopClass = classNames({[styles.btn]: true, [styles.book_button_top]: true});
export const btnTopNotAuthorizedClass = classNames([styles.p_style_nonAuth]);
export const btnDownClass = classNames({[styles.btn]: true, [styles.download_button]: true});
export const btnDownClassMargin = classNames({[styles.btn]: true, [styles.download_message]: true});
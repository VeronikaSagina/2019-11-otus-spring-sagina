import React from "react";
import Select from 'react-select'
import styles from './styles/Component.module.css';

export const Input = (props) => {
    return (
        <div className={styles.book_text}>
            <label hidden={props.hidden} htmlFor={props.name} className="form-label">{props.title}</label>
            <input
                readOnly={props.readonly}
                hidden={props.hidden}
                id={props.name}
                name={props.name}
                type={props.type}
                value={props.value}
                defaultChecked={true}
                onChange={props.handleChange}
                placeholder={props.placeholder}
            />
        </div>
    )
};

export const Textarea = (props) => {
    return (
        <div className={styles.book_text}>
            <label hidden={props.hidden} htmlFor={props.name}>{props.title}</label>
            <textarea rows="10" cols="100"
                      readOnly={props.readonly}
                      hidden={props.hidden}
                      id={props.name}
                      name={props.name}
                      value={props.value}
                      onChange={props.handleChange}
                      placeholder={props.placeholder}
            />
        </div>
    )
};

export const MultiplySelect = (props) => {
    const options = multi(props.options);
    return (
        <div className={styles.book_text}>
            <label htmlFor={props.name}>{props.title}</label>
            <Select
                name={props.name}
                defaultValue={multi(props.value)}
                onChange={props.handleChange}
                isMulti
                options={options}
            >
            </Select>
        </div>)
};

function multi(props) {
    return props.map((o) => {
        return one(o)
    });
}

function one(props) {
    return {
        label: props.name,
        value: props,
        key: props.id
    }
}

export const OneSelect = (props) => {
    const options = multi(props.options);
    return (
        <div className={styles.book_text}>
            <label htmlFor={props.name}>{props.title}</label>
            <Select
                name={props.name}
                defaultValue={one(props.value)}
                onChange={props.handleChange}
                options={options}>
            </Select>
        </div>)
};

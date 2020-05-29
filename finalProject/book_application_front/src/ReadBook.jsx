import styles from './styles/Component.module.css';
import React from "react";
import {API_URL} from "./ApiPath";
import axios from "axios";
import {Document, Page} from 'react-pdf/dist/entry.webpack';
import {bodyClass, btnTopClass} from "./styles/styles";

export default class ReadBook extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            fileId: this.props.location.state.fileId,
            numPages: null,
            pageNumber: 1,
            content: ""
        };
    }

    onDocumentLoadSuccess = ({numPages}) => {
        this.setState({numPages});
    };

    componentDidMount() {
        axios(`${API_URL}/book/read/${this.state.fileId}`, {
            method: 'GET',
            responseType: 'blob'
        }).then(response => {
            this.setState({content: new Blob([response.data], {type: 'application/pdf'})});
        }).catch(message => console.log(message));
    };

    goToPrevPage = () =>
        this.setState(state => ({pageNumber: state.pageNumber === 1 | state.pageNumber - 1}));
    goToNextPage = () =>
        this.setState(state => ({pageNumber: state.pageNumber + 1}));

    render() {
        const {pageNumber, numPages} = this.state;
        return (
            <div className={styles.background_color}>
                <div className={bodyClass}>
                    <div className={styles.displayed_center}>
                        <nav className={styles.inner_button_area}>
                            <button className={btnTopClass} onClick={this.goToPrevPage}>Предыдущая</button>
                            <button className={btnTopClass} onClick={this.goToNextPage}>Следующая</button>
                        </nav>
                        <p>Page {pageNumber} of {numPages}</p>
                        <Document
                            file={this.state.content}
                            onLoadSuccess={this.onDocumentLoadSuccess}>
                            <Page style={styles.page} pageNumber={pageNumber}/>
                        </Document>
                    </div>
                </div>
            </div>
        );
    }
}


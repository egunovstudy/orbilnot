import * as React from 'react';

import {toast, ToastContainer} from 'react-toastify';
import {getAuthorization, login, logout, removeAuthorization} from '../helpers/auth_helper';

import {Button, Form, FormGroup, Input, Label} from "reactstrap";
import {getUser, saveUser} from "../helpers/axios_helper";

export default class AppContent extends React.Component {
    emptyItem = {
        firstName: '',
        secondName: '',
        username: '',
        phone: '',
        email: '',
        id: undefined
    };

    constructor(props) {
        super(props);
        this.state = {
            item: this.emptyItem,
            info: {
                loginButtonText: "Login",
                edit: false
            }
        };
        this.handleChange = this.handleChange.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
    }

    handleChange(event) {
        let name = event.target.name;
        let item = this.state.item
        item[name] = event.target.value
        this.setState({item: item});
    }

    handleSubmit(event) {
        event.preventDefault();
        if (!this.state.info.edit) {
            this.toggleEditState();
        } else {
            this.loadAndCheckAuthorization()
                .then(
                    auth =>
                        saveUser(auth.access_token, this.state.item).then()
                )
            this.toggleEditState();
        }
    }

    toggleEditState() {
        this.setState({
            item: this.state.item,
            info: {
                edit: !this.state.info.edit,
                text: this.state.info.text
            }
        })
    }

    componentDidMount() {
        this.loadAndCheckAuthorization()
            .then(authorization => {
                return getUser(authorization.access_token)
                    .then(response => {
                        return response.data
                    })
            })
            .then(user => {
                    let item = this.state.item;
                    item.firstName = user.firstName
                    item.lastName = user.lastName
                    item.email = user.email
                    item.phone = user.phone
                    item.username = user.username
                    item.id = user.id
                    this.setState({item: item, info: {text: 'Logout'}})

                }
            )
            .catch(e => {
                this.setState({item: this.state.item, info: {text: 'Login'}})
                console.error(e)
            });
    }

    login = () => {
        login();
    }

    logout = () => {
        logout();
    }

    action = () => {
        getAuthorization().then((auth) => {
            if (auth) {
                this.setState({info: {text: 'Login'}})
                this.logout();
            } else {
                this.login();
            }
        })
    }

    loadAndCheckAuthorization = () => {
        return getAuthorization().then(authorization => {

            if (authorization) {
                if (authorization.expired) {
                    removeAuthorization();
                    toast.error("User expired")
                    throw new Error("User expired")
                } else {
                    toast.success('User has been successfully loaded from store.');
                    return authorization
                }
            } else {
                toast.error('You are not logged in.');
                throw new Error("You are not logged in")
            }
        });
    };

    render() {
        const item = this.state.item;
        return (
            <>
                <ToastContainer/>
                <header className="py-3 mb-4 border-bottom">
                    <div className="container d-flex flex-wrap justify-content-center">
                        <a href="/"
                           className="d-flex align-items-center mb-3 mb-lg-0 me-lg-auto link-body-emphasis text-decoration-none">
                            <span className="fs-4">Profile Information</span>
                        </a>
                    </div>
                </header>
                <Form onSubmit={this.handleSubmit}>
                    <FormGroup>
                        <Label for="name">username</Label>
                        <Input type="text" name="username" id="username" value={item.username || ''}
                               onChange={this.handleChange} disabled={true} autoComplete="username"/>
                    </FormGroup>
                    <FormGroup>
                        <Label for="name">First Name</Label>
                        <Input type="text" name="firstName" id="firstName" value={item.firstName || ''}
                               onChange={this.handleChange} disabled={!this.state.info.edit} autoComplete="firstName"/>
                    </FormGroup>
                    <FormGroup>
                        <Label for="name">Last Name</Label>
                        <Input type="text" name="lastName" id="lastName" value={item.lastName || ''}
                               onChange={this.handleChange} disabled={!this.state.info.edit} autoComplete="lastName"/>
                    </FormGroup>
                    <FormGroup>
                        <Label for="name">phone</Label>
                        <Input type="text" name="phone" id="phone" value={item.phone || ''}
                               onChange={this.handleChange} disabled={!this.state.info.edit} autoComplete="username"/>
                    </FormGroup>
                    <FormGroup>
                        <Label for="email">Email</Label>
                        <Input type="text" name="email" id="email" value={item.email || ''}
                               onChange={this.handleChange} disabled={!this.state.info.edit} autoComplete="email"/>
                    </FormGroup>
                    <FormGroup>
                        <Button color="primary" disabled={!this.state.item.id}
                                type="submit">{this.state.info.edit ? 'Save' : 'Edit'}</Button>
                        <Button className="btn btn-primary" style={{margin: '10px'}} onClick={this.action}>
                            {this.state.info.text}
                        </Button>
                    </FormGroup>
                </Form>
            </>
        );
    }
}

import React, { Component } from 'react';
import { Link } from 'react-router-dom';
import { Button, Container } from 'reactstrap';

class Home extends Component {
    render() {
        return (
            <Container fluid>
                <Button color="link"><Link to="/users">Users</Link></Button>
            </Container>
        );
    }
}
export default Home;
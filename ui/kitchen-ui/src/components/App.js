import * as React from 'react';
import './App.css';
import {Layout} from "./Layout/Layout";
import AppContent from "./AppContent";


export default function App() {
    return (
        <Layout>
            <AppContent/>
        </Layout>
    )
}
import React from 'react';
import ReactDOM from 'react-dom/client';
import './index.css';
import App from "./App";
import {createTheme, MantineProvider} from "@mantine/core";
import "@mantine/core/styles.css"

const rootElement = document.getElementById("root");
if (!rootElement) {
    throw new Error("Root element not found");
}

const root = ReactDOM.createRoot(rootElement);

const theme = createTheme({
        colors: {
            resilience: [
                '#f7ecff',
                '#e7d6fb',
                '#caaaf1',
                '#ac7ce8',
                '#9354e0',
                '#833bdb',
                '#7b2eda',
                '#6921c2',
                '#5d1cae',
                '#501599'
            ]
        }
    })
;

root.render(
    <MantineProvider theme={theme} defaultColorScheme={"dark"}>
        <React.StrictMode>
            <App/>
        </React.StrictMode>
    </MantineProvider>
);

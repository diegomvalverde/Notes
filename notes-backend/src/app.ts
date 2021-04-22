import * as express from 'express';
import * as morgan from 'morgan';
import indexRoutes from './routes/routes';
import {environment} from "./environment/environment";

const bodyParser = require('body-parser');
const app = express();

//Settings
app.set("port", process.env.port || environment.SERVER_PORT);

// allow to req.body

// parse application/x-www-form-urlencoded
app.use(bodyParser.urlencoded({ extended: false }));

// parse application/json
app.use(bodyParser.json());


//Middleware

app.use(morgan("dev"));

// Angular compatible, because angular needs cors
app.use(function (req, res, next) {
    // req.body;
    res.header("Access-Control-Allow-Origin", "*");
    res.header("Access-Control-Allow-Headers", "*");
    next();
});

app.use("/", indexRoutes);



export default app;

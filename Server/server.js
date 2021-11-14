const crypto = require('crypto')
const express = require('express')
const mongodb = require('mongodb')

const API_Key = '5CWqfG@,9!Enym[y%?D7B@AZCWi,dw-J'
const app = express()

// db

const users_db = 'minecraft_test'
const url = ``;
const MongoClient = new mongodb.MongoClient(url, {useNewUrlParser: true, useUnifiedTopology: true});

MongoClient.connect(function(err){
  if (err) {
    throw new Error("Failed connecting to mongo: " + err)
  }

  console.log("Connected successfully to mongo server");
})

// hash func

function hash_str(str) {
    return crypto.createHmac('sha256', str).digest('hex')
}

// db funcs

async function getAccount(user) {
    return await MongoClient.db(users_db).collection('users').findOne({
        username: user
    })
}

// api middlewares

async function authenticate(req, res, next) {
    if (req.headers['authorization'] == API_Key) {
        return next()
    }

    return res.status(401).json({
        success: false,
        message: 'Authorization required'
    })
}

// api 

app.use(express.json())

app.post('/register', authenticate, async function(req, res) {
    var isUserRegistered = await getAccount(req.body.user)

    if (isUserRegistered) {
        return res.status(400).json({
            success: false,
            message: 'This user already exist'
        })
    }
    
    MongoClient.db(users_db).collection('users').insertOne({
        uuid: req.body.uuid,
        pass: hash_str(req.body.pass),
        regdate: new Date()
    })

    return res.status(200).json({
        success: true
    })
})

app.post('/login', authenticate, async function(req, res) {
    var userData = await getAccount(req.query.uuid)

    if (!userData) {
        return res.status(404).json({
            success: false,
            message: 'This account does not exist'
        })
    } else if (userData.pass == hash_str(req.body.pass)) {
        return res.status(200).json({
            success: true,
            authorized: true
        })
    } else {
        return res.status(403).json({
            success: false,
            authorized: false
        })
    }
})

app.get('/user-exist', async function(req, res) {
    var userData = await getAccount(req.query.uuid)

    return res.status(userData && 200 || 404).json({
        success: true,
        data: req.headers.authorization == API_Key && userData || "You must provide API key, to get this data"
    })
})

app.get('/', (req, res, next) => {
    res.status(200).json({
        success: true,
        message: "ok"
    })
})

// listen

app.listen(80, () => {
    console.log('Server listening')
})
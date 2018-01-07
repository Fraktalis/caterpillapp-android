const functions = require('firebase-functions');    // Functions by default for firebase
const firebase = require("firebase");               // Used to instantiate a client App
const nodemailer = require('nodemailer');           // Used to send email via SMTP
const gcs = require('@google-cloud/storage')();     // Service to handle google firebase cloud storage
const generator = require('generate-password');     // A password generator
const parse = require('csv-parse/lib/sync');          // A CSV parser library
const admin = require('firebase-admin');            // The Firebase Admin SDK to access the Firebase Realtime Database.
const APP_NAME = 'Leafguard'


// Admin config
admin.initializeApp(functions.config().firebase);


// Client config
var config = {
    apiKey: "AIzaSyAZKz7HtB-cHaK3UAYWfWsczh3HHPw0DTQ",
    authDomain: "leafguard-firebase.firebaseapp.com",
    databaseURL: "https://leafguard-firebase.firebaseio.com",
    projectId: "leafguard-firebase",
    storageBucket: "leafguard-firebase.appspot.com",
    messagingSenderId: "1061771301460"
};

var clientApp = firebase.initializeApp(config, 'client');


// Email config
const gmailEmail = functions.config().gmail.email;
const gmailPassword = functions.config().gmail.password;
const mailTransport = nodemailer.createTransport({
    service: 'gmail',
    auth: {
        user: 'leafguard.core@gmail.com',
        password: 'leafguard781227'
    }
});

var headerMapping  = ["country","name","e-mail","partner_id","city","school_name","school_level","lattitude","longitude","still_in"];
var finalReport = {
    imported: [],
    ignored: [],
    error: []
};

exports.assertUpload = functions.storage.object().onChange( function (event) {
    const object = event.data; // The Storage object.
    const file = gcs.bucket(object.bucket).file(object.name);
    return file.download()
    .then(function (content) {
        console.log('Content loaded');
        return parse(content.toString('utf-8'));
    })
    .then(function (parsedContent) {
        console.log('Content parsed');
        var newAccounts = [];
        var header = parsedContent[0];
        var body = parsedContent.slice(1);
        var bodyMap = body.map(x => array_combine(headerMapping, x));
        var promisesArray = bodyMap.map(function (elem) {
            return clientApp.auth().fetchProvidersForEmail(elem['e-mail'])
                .then(function (providers) {
                    var accountExists = providers.length > 0;
                    if (accountExists) {
                        console.log('account exists ? ', accountExists);
                        finalReport.ignored.push(elem['e-mail']);
                        return true;
                    }
                    console.log('account doesn\'t exist, Creating.');
                    var pass = generator.generate({
                        length: 12,
                        numbers: true
                    });
                    return admin.auth().createUser({
                        email: elem['e-mail'],
                        password: pass,
                    }).catch(function (err) {
                        console.log("a promise failed to resolve:", err);
                        finalReport.error.push(err);
                        return promisesArray;
                    }).then(function (userRecord) {
                        newAccounts.push(elem['e-mail']);
                        var uid = userRecord.uid;
                        console.log("User with id : " + uid + " created with email : " + elem['e-mail']);
                        //TODO: Handle composed full name...
                        var fullName = elem["name"].split(" ");
                        elem["name"] = fullName[0];
                        elem["surname"] = fullName[1];

                        finalReport.imported.push(elem['e-mail']);

                        const mailOptions = {
                            from: gmailEmail,
                            to: elem['e-mail'],
                            subject: 'Welcome to ' + APP_NAME,
                            text: 'You have been granting access to leafguard application ! \n' +
                            'e-mail: ' + elem['e-mail'] + ' \n' +
                            'password: ' + pass + "\n \n" +
                            "Change it as soon as possible, and enjoy the application !"
                        };

                        return admin.database().ref('/users').child(uid).set(JSON.parse(JSON.stringify(elem)))
                            .then(function () {
                                return mailTransport.sendMail(mailOptions)
                                    .catch(function (err) {
                                        console.log("Error during mail sending: ", err)
                                        finalReport.error.push(err);
                                    });
                            })
                            .catch(function (err) {
                                finalReport.error.push(err);
                            });
                    });
                }).catch(function (err) {
                    return false;
                });

        });
        return Promise.all(promisesArray);
    })
    .catch(function (err) {
        console.log('A promise failed to resolve', err);
    })
    .then(function (whatever) {
        console.log("Saving report...");
        return admin.database().ref('/reports').push().set(finalReport);
    })
    .catch( function (err) {
        console.log(err);
    });
});

exports.cleanseUsers = functions.https.onRequest(function (req, res) {
    var pageToken;
    return admin.auth().listUsers(1000)
        .then(function (listUsersResult) {
            usersToDelete = [];
            listUsersResult.users.forEach(function (user) {
                if (user.metadata.lastSignInTime == null) {
                    usersToDelete.push(user.uid);
                }
            });
            console.log(usersToDelete.length);
            usersToDeletePromises = usersToDelete.map(function (userUid) {
                admin.database().ref('/users').child(userUid).remove();
                return admin.auth().deleteUser(userUid);
            });

            return Promise.all(usersToDeletePromises);
        });
    });


function array_combine (keys, values) { // eslint-disable-line camelcase
      //  discuss at: http://locutus.io/php/array_combine/
      // original by: Kevin van Zonneveld (http://kvz.io)
      // improved by: Brett Zamir (http://brett-zamir.me)
      //   example 1: array_combine([0,1,2], ['kevin','van','zonneveld'])
      //   returns 1: {0: 'kevin', 1: 'van', 2: 'zonneveld'}
      var newArray = {}
      var i = 0
      // input sanitation
      // Only accept arrays or array-like objects
      // Require arrays to have a count
      if (typeof keys !== 'object') {
        return false
      }
      if (typeof values !== 'object') {
        return false
      }
      if (typeof keys.length !== 'number') {
        return false
      }
      if (typeof values.length !== 'number') {
        return false
      }
      if (!keys.length) {
        return false
      }
      // number of elements does not match
      if (keys.length !== values.length) {
        return false
      }
      for (i = 0; i < keys.length; i++) {
        newArray[keys[i]] = values[i]
      }
      return newArray
}



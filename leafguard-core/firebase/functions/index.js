const functions = require('firebase-functions');
const gcs = require('@google-cloud/storage')();
var parse = require('csv-parse/lib/sync');

// The Firebase Admin SDK to access the Firebase Realtime Database.
const admin = require('firebase-admin');
admin.initializeApp(functions.config().firebase);

var headerMapping  = ["country","name","e-mail","partner_id","city","school_name","school_level","lattitude","longitude","still_in"];

exports.assertUpload = functions.storage.object().onChange( function (event) {
    const object = event.data; // The Storage object.
    const file = gcs.bucket(object.bucket).file(object.name);
    return file.download()
    .then(function (content) {
        console.log('promise 1');
        return parse(content.toString('utf-8'));
    })
    .then(function (parsedContent) {
        console.log('promise 2');
        var header = parsedContent[0];
        var body = parsedContent.slice(1);
        var bodyMap = body.map(x => array_combine(headerMapping, x));
        var promisesArray = bodyMap.map(function (elem) {
            console.log('promise 3');
            return admin.auth().createUser({
                email: elem['e-mail'],
                password: "leafguard",
            }).catch(function (err) {
                console.log(err);
            }).then(function (userRecord) {
                var uid = userRecord.uid;
                console.log("User with id : " + uid + " created with email : " + elem['e-mail']);
                var fullName = elem["name"].split(" ");
                elem["name"] = fullName[0];
                elem["surname"] = fullName[1];
                return admin.database().ref('/users').child(uid).set(JSON.parse(JSON.stringify(elem)));
            });
        });
        return Promise.all(promisesArray);
    })
    .catch(function (err) {
        console.log('A promise failed to resolve', err);
    })
    .then(function (whatever) {
        console.log("promise 4");
        return whatever;
    })
    .catch( function (err) {
        console.log(err);
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



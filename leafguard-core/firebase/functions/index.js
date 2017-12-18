const functions = require('firebase-functions');
const gcs = require('@google-cloud/storage')();
var parse = require('csv-parse');

// The Firebase Admin SDK to access the Firebase Realtime Database.
const admin = require('firebase-admin');
admin.initializeApp(functions.config().firebase);

var headerMapping  = ["country","name","e-mail","partner_id","city","school_name","school_level","lattitude","longitude","still_in"];

exports.assertUpload = functions.storage.object().onChange(event => {
    const object = event.data; // The Storage object.
    const file = gcs.bucket(object.bucket).file(object.name);
    file.download().then(function (content){
        if (content) {
            parse(content.toString('utf-8'), {}, function (err, output) {
                var header = output[0];
                var body = output.slice(1);
                var bodyMap = body.map(x => array_combine(headerMapping, x));
                bodyMap.forEach(function (elem) {
                    admin.auth().createUser({
                        email: elem['e-mail'],
                        password: "leafguard",
                    }).then(function(userRecord) {
                        var uid = userRecord.uid;
                        console.log("User with id : " + uid + " created with email : " + elem['e-mail']);
                        admin.database().ref('/users').child(uid).push({nouveau: true, email: elem['e-mail']});
                    });
/*
                    int i = 0;
                    for (index in elem) {

                    }
*/
                });
            });
        }
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



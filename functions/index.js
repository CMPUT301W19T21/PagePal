const functions = require('firebase-functions');
const admin = require('firebase-admin');

admin.initializeApp();

/**
 * Triggers when a user gets a notification.
 */
exports.sendNotification = functions.database.ref('/notifications/{uid}/{notificationTimestamp}')
    .onWrite(event => {
      
  
        const notificationBody = event.after._data.message;
        const isbn = event.after._data.bookIsbn;
        const owner = event.after._data.owner;
        const recipient = event.after._data.recipient;

        return admin.database().ref("/users/" + recipient).once('value').then(snap => {
            const token = snap.child("messagingToken").val();
                const payload = {
                    data: {
                        title: "PagePal",
                        body: notificationBody,
                        isbn: isbn,
                        owner: owner
                    }
                };
                return admin.messaging().sendToDevice(token, payload)

            })
            .then(function(response) {
                    return console.log("Notification successfully sent:", response);
                    })
                    .catch(function(error) {
                        console.log("Notification failed to send:", error);
                        });
            });
      
    

     